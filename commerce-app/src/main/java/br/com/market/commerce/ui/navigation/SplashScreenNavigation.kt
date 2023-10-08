package br.com.market.commerce.ui.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.commerce.ui.screens.SplashScreen
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


internal const val splashScreenRoute = "splashScreenRoute"

fun NavGraphBuilder.splashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToMainScreen: () -> Unit
) {
    composable(route = splashScreenRoute) {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val dataStore = context.dataStore

        SplashScreen(
            onAfterDelay = {
                coroutineScope.launch {
                    val token = dataStore.data.first().toPreferences()[PreferencesKey.TOKEN]

                    if (token.isNullOrEmpty()) {
                        onNavigateToLogin()
                    } else {
                        onNavigateToMainScreen()
                    }
                }
            }
        )
    }
}