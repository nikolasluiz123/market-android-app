package br.com.market.storage.ui.navigation.lovs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.storage.ui.component.lov.ProductLov
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.navigateForResult
import br.com.market.storage.ui.viewmodels.product.ProductLovViewModel

internal const val productLovRoute = "productLov"
internal const val productLovNavResultCallbackKey = "productLovCallbackKey"

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