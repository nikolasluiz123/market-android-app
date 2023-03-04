package br.com.market.storage.ui.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.storage.preferences.PreferencesKey
import br.com.market.storage.preferences.dataStore
import br.com.market.storage.ui.screens.SplashScreen
import kotlinx.coroutines.launch

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