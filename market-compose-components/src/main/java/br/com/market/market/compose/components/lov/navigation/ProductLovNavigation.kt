package br.com.market.market.compose.components.lov.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.market.compose.components.lov.ProductLov
import br.com.market.market.compose.components.lov.viewmodel.ProductLovViewModel

internal const val productLovRoute = "productLov"
const val productLovNavResultCallbackKey = "productLovCallbackKey"
internal const val argumentBrandId = "argumentBrandId"

fun NavGraphBuilder.productLov(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = "$productLovRoute?$argumentCategoryId={$argumentCategoryId}&$argumentBrandId={$argumentBrandId}") {
        val productLovViewModel = hiltViewModel<ProductLovViewModel>()

        ProductLov(
            viewModel = productLovViewModel,
            onItemClick = onItemClick,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToProductLov(categoryId: String, brandId: String, callback: (String) -> Unit) {
    navigateForResult(
        key = productLovNavResultCallbackKey,
        route = "$productLovRoute?$argumentCategoryId={$categoryId}&$argumentBrandId={$brandId}",
        callback = callback
    )
}