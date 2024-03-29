package br.com.market.storage.ui.navigation.product

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.callbacks.INumberInputNavigationCallback
import br.com.market.core.callbacks.ITextInputNavigationCallback
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.screens.product.ProductScreen
import br.com.market.storage.ui.viewmodels.product.ProductViewModel

internal const val productScreenRoute = "product"
internal const val argumentProductId = "productId"

fun NavGraphBuilder.productScreen(
    onBackClick: () -> Unit,
    onStorageButtonClick: (String, String, String) -> Unit,
    onProductImageClick: (String) -> Unit,
    onBottomSheetLoadImageLinkClick: ((ByteArray) -> Unit) -> Unit,
    textInputCallback: ITextInputNavigationCallback,
    numberInputCallback: INumberInputNavigationCallback
) {
    composable(route = "$productScreenRoute?$argumentCategoryId={$argumentCategoryId}&$argumentBrandId={$argumentBrandId}&$argumentProductId={$argumentProductId}") {
        val viewModel = hiltViewModel<ProductViewModel>()

        ProductScreen(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onStorageButtonClick = onStorageButtonClick,
            onProductImageClick = onProductImageClick,
            onBottomSheetLoadImageLinkClick = onBottomSheetLoadImageLinkClick,
            textInputCallback = textInputCallback,
            numberInputCallback = numberInputCallback

        )
    }
}

fun NavController.navigateToProductScreen(categoryId: String, brandId: String, productId: String, navOptions: NavOptions? = null) {
    navigate(
        route = "$productScreenRoute?$argumentCategoryId={$categoryId}&$argumentBrandId={$brandId}&$argumentProductId={$productId}",
        navOptions = navOptions
    )
}

fun NavController.navigateToProductScreen(categoryId: String, brandId: String, navOptions: NavOptions? = null) {
    navigate(route = "$productScreenRoute?$argumentCategoryId={$categoryId}&$argumentBrandId={$brandId}", navOptions = navOptions)
}