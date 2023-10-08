package br.com.market.commerce.ui.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import br.com.market.core.extensions.popBackStackWithResult
import br.com.market.market.compose.components.navigation.inputNumber
import br.com.market.market.compose.components.navigation.inputNumberNavResultCallbackKey
import br.com.market.market.compose.components.navigation.inputPassword
import br.com.market.market.compose.components.navigation.inputPasswordNavResultCallbackKey
import br.com.market.market.compose.components.navigation.inputText
import br.com.market.market.compose.components.navigation.inputTextNavResultCallbackKey
import br.com.market.market.compose.components.navigation.navigateToInputNumber
import br.com.market.market.compose.components.navigation.navigateToInputPassword
import br.com.market.market.compose.components.navigation.navigateToInputText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

@Composable
fun CommerceAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    navController.addOnDestinationChangedListener { _, destinations, _ ->
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.SCREEN_NAME, destinations.label as String?)
        params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, destinations.label as String?)

        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
    }

    NavHost(
        navController = navController,
        startDestination = splashScreenRoute,
        modifier = modifier
    ) {

        splashScreen(
            onNavigateToLogin = {
                navController.navigateToLoginScreen(
                    navOptions {
                        popUpTo(splashScreenRoute) {
                            inclusive = true
                        }
                    }
                )
            },
            onNavigateToMainScreen = {
                navController.navigateToProductsScreen(
                    navOptions {
                        popUpTo(splashScreenRoute) {
                            inclusive = true
                        }
                    }
                )
            }
        )

        loginScreen(
            onRegisterClick = navController::navigateToRegisterClientScreen,
            onAuthenticateSuccess = {
                navController.navigateToProductsScreen(
                    navOptions {
                        popUpTo(loginScreenRoute) {
                            inclusive = true
                        }
                    }
                )
            }
        )

        registerClientScreen(
            onBackClick = navController::popBackStack,
            onNavigateToTextInput = navController::navigateToInputText,
            onNavigateToNumberInput = navController::navigateToInputNumber,
            onNavigateToPasswordInput = navController::navigateToInputPassword
        )

        productsScreen(
            onBackClick = navController::popBackStack
        )

        inputText(
            onBackClick = navController::popBackStack,
            onConfirmClick = {
                navController.popBackStackWithResult(inputTextNavResultCallbackKey, it)
            },
            onCancelClick = navController::popBackStack
        )

        inputNumber(
            onBackClick = navController::popBackStack,
            onConfirmClick = {
                navController.popBackStackWithResult(inputNumberNavResultCallbackKey, it)
            },
            onCancelClick = navController::popBackStack
        )

        inputPassword(
            onBackClick = navController::popBackStack,
            onConfirmClick = {
                navController.popBackStackWithResult(inputPasswordNavResultCallbackKey, it)
            },
            onCancelClick = navController::popBackStack
        )
    }

}