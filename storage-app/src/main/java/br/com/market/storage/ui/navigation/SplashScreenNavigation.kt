package br.com.market.storage.ui.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.storage.ui.screens.SplashScreen
import br.com.market.storage.ui.viewmodels.SplashViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID


internal const val splashScreenRoute = "splashScreen"

/**
 * Função que realiza as definições da navegação para a
 * tela da SplashArt exibida enquanto decide-se para qual tela
 * levar o usuário.
 *
 * @param onNavigateToLogin Função para realizar a navegação para a tela de login
 * @param onNavigateToCategories Função para realizar a navegação para a tela de lista de categorias
 *
 * @author Nikolas Luiz Schmitt
 */
fun NavGraphBuilder.splashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    composable(route = splashScreenRoute) {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val dataStore = context.dataStore
        val viewModel = hiltViewModel<SplashViewModel>()

        SplashScreen {
            coroutineScope.launch {
                val token = dataStore.data.first().toPreferences()[PreferencesKey.TOKEN]

                if (token.isNullOrBlank()) {
                    onNavigateToLogin()
                }
                else if (viewModel.deviceRegistered()) {
                    onNavigateToCategories()
                }
                else {
                    if(dataStore.data.first()[PreferencesKey.TEMP_DEVICE_ID].isNullOrBlank()) {
                        dataStore.edit {
                            it[PreferencesKey.TEMP_DEVICE_ID] = UUID.randomUUID().toString()
                        }
                    }

                    onNavigateToAbout()
                }
            }
        }
    }
}