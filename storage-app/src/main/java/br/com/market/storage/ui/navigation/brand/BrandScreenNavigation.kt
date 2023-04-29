package br.com.market.storage.ui.navigation.brand

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.brand.BrandScreen
import br.com.market.storage.ui.viewmodels.brand.BrandViewModel
import java.util.*

internal const val brandScreenRoute = "brand"
internal const val argumentBrandId = "brandId"

fun NavGraphBuilder.brandScreen(onBackClick: () -> Unit) {
    composable(route = "$brandScreenRoute?$argumentBrandId={$argumentBrandId}") {
        val brandViewModel = hiltViewModel<BrandViewModel>()

        BrandScreen(
            viewModel = brandViewModel,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToBrandScreen(brandId: UUID, navOptions: NavOptions? = null) {
    navigate(route = "$brandScreenRoute?$argumentBrandId={$brandId}", navOptions = navOptions)
}

fun NavController.navigateToBrandScreen(navOptions: NavOptions? = null) {
    navigate(route = brandScreenRoute, navOptions = navOptions)
}