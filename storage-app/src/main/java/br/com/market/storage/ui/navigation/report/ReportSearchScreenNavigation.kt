package br.com.market.storage.ui.navigation.report

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.report.ReportSearchScreen
import br.com.market.storage.ui.viewmodels.report.ReportSearchScreenViewModel

internal const val reportSearchScreenRoute = "reportSearchScreenRoute"
internal const val argumentDirectory = "argumentDirectory"

fun NavGraphBuilder.reportSearchScreen(
    onNavigateToBack: () -> Unit,
) {
    composable(route = "$reportSearchScreenRoute?$argumentDirectory={$argumentDirectory}") {
        val viewModel = hiltViewModel<ReportSearchScreenViewModel>()

        ReportSearchScreen(
            viewModel = viewModel,
            onBackClick = onNavigateToBack
        )
    }
}

fun NavController.navigateToReportSearchScreen(directory: String, navOptions: NavOptions? = null) {
    navigate(route = "$reportSearchScreenRoute?$argumentDirectory={$directory}", navOptions = navOptions)
}