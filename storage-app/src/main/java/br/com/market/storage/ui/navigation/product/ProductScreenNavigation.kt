package br.com.market.storage.ui.navigation.product

import android.net.Uri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.bottomsheet.IEnumOptionsBottomSheet
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.screens.product.ProductScreen
import br.com.market.storage.ui.viewmodels.product.ProductViewModel
import java.util.*

internal const val productScreenRoute = "product"
internal const val argumentProductId = "productId"

fun NavGraphBuilder.productScreen(
    onBackClick: () -> Unit,
    onStorageButtonClick: () -> Unit = { },
    onBottomSheetLoadImageItemClick: (IEnumOptionsBottomSheet, (Uri) -> Unit) -> Unit
) {
    composable(route = "$productScreenRoute?$argumentBrandId={$argumentBrandId}&$argumentProductId={$argumentProductId}") {
        val viewModel = hiltViewModel<ProductViewModel>()

        ProductScreen(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onStorageButtonClick = onStorageButtonClick,
            onBottomSheetLoadImageItemClick = onBottomSheetLoadImageItemClick
        )
    }
}

fun NavController.navigateToProductScreen(brandId: UUID, productId: UUID, navOptions: NavOptions? = null) {
    navigate(route = "$productScreenRoute?$argumentBrandId={$brandId}&$argumentProductId={$productId}", navOptions = navOptions)
}

fun NavController.navigateToProductScreen(brandId: UUID, navOptions: NavOptions? = null) {
    navigate(route = "$productScreenRoute?$argumentBrandId={$brandId}", navOptions = navOptions)
}