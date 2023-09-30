package br.com.market.market.compose.components.screens.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.market.compose.components.screens.PDFViewer
import br.com.market.market.compose.components.screens.viewmodel.PDFViewerViewModel

internal const val pdfViewerScreenRoute = "pdfViewerScreenRoute"
internal const val pdfViewerUriArgument = "pdfViewerUriArgument"

fun NavGraphBuilder.pdfViewer(
    onBackClick: () -> Unit
) {
    composable(route = "$pdfViewerScreenRoute?$pdfViewerUriArgument={$pdfViewerUriArgument}") {
        val viewModel = hiltViewModel<PDFViewerViewModel>()

        PDFViewer(
            viewModel = viewModel,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToPDFViewer(uri: String, navOptions: NavOptions? = null) {
    navigate(route = "$pdfViewerScreenRoute?$pdfViewerUriArgument={$uri}", navOptions = navOptions)
}