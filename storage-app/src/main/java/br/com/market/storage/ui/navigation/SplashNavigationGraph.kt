package br.com.market.storage.ui.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.storage.ui.screens.SplashScreen
import kotlinx.coroutines.launch

/**
 * Função que realiza as definições da navegação para a
 * tela da SplashArt exibida enquanto decide-se para qual tela
 * levar o usuário.
 *
 * @param navController Controlador da navegação do compose.
 *
 * @author Nikolas Luiz Schmitt
 */
fun NavGraphBuilder.splashGraph(
    navController: NavHostController
) {
    composable(route = StorageAppDestinations.Splash.route) {
        val coroutineScope = rememberCoroutineScope()
        val dataStore = LocalContext.current.dataStore

        SplashScreen(
            onAfterDelay = {
                coroutineScope.launch {
                    dataStore.data.collect { preferences ->
                        val token = preferences[PreferencesKey.TOKEN]

                        if (token.isNullOrBlank()) {
                            navController.cleanNavigation(StorageAppDestinations.Login.route)
                        } else {
                            navController.cleanNavigation(StorageAppDestinations.StorageProducts.route)
                        }
                    }
                }
            }
        )
    }
}