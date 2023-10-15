package br.com.market.market.compose.components.lov.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.market.compose.components.lov.MarketLov
import br.com.market.market.compose.components.lov.viewmodel.MarketLovViewModel

internal const val marketLovRoute = "brandLov"
const val marketLovNavResultCallbackKey = "brandLovCallbackKey"

fun NavGraphBuilder.marketLov(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = marketLovRoute) {
        val viewModel = hiltViewModel<MarketLovViewModel>()

        MarketLov(
            viewModel = viewModel,
            onItemClick = onItemClick,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToMarketLov(callback: (String) -> Unit) {
    navigateForResult(
        key = marketLovNavResultCallbackKey,
        route = marketLovRoute,
        callback = callback
    )
}