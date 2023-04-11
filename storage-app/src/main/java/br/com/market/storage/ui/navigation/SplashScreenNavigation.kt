package br.com.market.storage.ui.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.storage.ui.screens.SplashScreen
import kotlinx.coroutines.launch

internal const val splashScreenRoute = "splash_screen"

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
    onNavigateToCategories: () -> Unit
) {
    composable(route = splashScreenRoute) {
        val coroutineScope = rememberCoroutineScope()
        val dataStore = LocalContext.current.dataStore

        SplashScreen {
            coroutineScope.launch {
                dataStore.data.collect { preferences ->
                    val token = preferences[PreferencesKey.TOKEN]
                    if (token.isNullOrBlank()) onNavigateToLogin() else onNavigateToCategories()
                }
            }
        }
    }
}