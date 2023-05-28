package br.com.market.storage.ui.navigation

import android.net.Uri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.bottomsheet.IEnumOptionsBottomSheet
import br.com.market.storage.ui.screens.ImageViewerScreen
import br.com.market.storage.ui.viewmodels.ImageViewerViewModel

internal const val imageViewerScreenRoute = "imageViewer"
internal const val argumentProductImageId = "productImageId"

fun NavGraphBuilder.imageViewerScreen(
    onBackClick: () -> Unit,
    onAfterDeleteImage: () -> Unit,
    onBottomSheetLoadImageItemClick: (IEnumOptionsBottomSheet, (Uri) -> Unit) -> Unit,
    onAfterSaveProductImage: () -> Unit
) {
    composable(route = "$imageViewerScreenRoute?$argumentProductImageId={$argumentProductImageId}") {
        val imageViewerViewModel = hiltViewModel<ImageViewerViewModel>()

        ImageViewerScreen(
            viewModel = imageViewerViewModel,
            onBackClick = onBackClick,
            onAfterDeleteImage = onAfterDeleteImage,
            onBottomSheetLoadImageItemClick = onBottomSheetLoadImageItemClick,
            onAfterSaveProductImage = onAfterSaveProductImage
        )
    }
}

fun NavController.navigateToImageViewerScreen(productImageId: String, navOptions: NavOptions? = null) {
    navigate(route = "$imageViewerScreenRoute?$argumentProductImageId={$productImageId}", navOptions = navOptions)
}