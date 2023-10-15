package br.com.market.market.compose.components.lov.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.market.compose.components.lov.BrandLov
import br.com.market.market.compose.components.lov.viewmodel.BrandLovViewModel

internal const val brandLovRoute = "brandLov"
const val brandLovNavResultCallbackKey = "brandLovCallbackKey"
internal const val argumentCategoryId = "argumentCategoryId"

fun NavGraphBuilder.brandLov(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = "$brandLovRoute?$argumentCategoryId={$argumentCategoryId}") {
        val brandViewModel = hiltViewModel<BrandLovViewModel>()

        BrandLov(
            viewModel = brandViewModel,
            onItemClick = onItemClick,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToBrandLov(categoryId: String, callback: (String) -> Unit) {
    navigateForResult(
        key = brandLovNavResultCallbackKey,
        route = "$brandLovRoute?$argumentCategoryId={$categoryId}",
        callback = callback
    )
}