package br.com.market.commerce.ui.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.market.core.ui.navigation.inputPasswordNavResultCallbackKey
import br.com.market.core.ui.navigation.inputPasswordScreen
import br.com.market.core.ui.navigation.inputTextScreen
import br.com.market.core.ui.navigation.inputTextScreenNavResultCallbackKey
import br.com.market.core.ui.navigation.navigateToInputNumber
import br.com.market.core.ui.navigation.navigateToInputPassword
import br.com.market.core.ui.navigation.navigateToInputText
import br.com.market.core.ui.navigation.numberAdvancedFilterScreen
import br.com.market.core.ui.navigation.numberAdvancedFilterScreenNavResultCallbackKey
import br.com.market.core.ui.navigation.popBackStackWithResult
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
            onNavigateToScreen = navController::navigateToLoginScreen
        )

        loginScreen(
            onRegisterClick = navController::navigateToRegisterClientScreen,
            onAuthenticateSuccess = {
                // navegar para a tela principal.
            }
        )

        registerClientScreen(
            onBackClick = navController::popBackStack,
            onNavigateToTextInput = navController::navigateToInputText,
            onNavigateToNumberInput = navController::navigateToInputNumber,
            onNavigateToPasswordInput = navController::navigateToInputPassword
        )

        inputTextScreen(
            onBackClick = navController::popBackStack,
            onConfirmClick = {
                navController.popBackStackWithResult(inputTextScreenNavResultCallbackKey, it)
            },
            onCancelClick = navController::popBackStack
        )

        numberAdvancedFilterScreen(
            onBackClick = navController::popBackStack,
            onConfirmClick = {
                navController.popBackStackWithResult(numberAdvancedFilterScreenNavResultCallbackKey, it)
            },
            onCancelClick = navController::popBackStack
        )

        inputPasswordScreen(
            onBackClick = navController::popBackStack,
            onConfirmClick = {
                navController.popBackStackWithResult(inputPasswordNavResultCallbackKey, it)
            },
            onCancelClick = navController::popBackStack
        )
    }

}