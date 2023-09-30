package br.com.market.storage.ui.navigation.lovs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.storage.ui.component.lov.BrandLov
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.viewmodels.brand.BrandLovViewModel

internal const val brandLovRoute = "brandLov"
internal const val brandLovNavResultCallbackKey = "brandLovCallbackKey"

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