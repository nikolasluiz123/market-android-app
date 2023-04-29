package br.com.market.storage.ui.navigation.lovs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.component.lov.BrandLov
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.viewmodels.brand.BrandLovViewModel
import java.util.*

internal const val brandLovRoute = "brandLov"
internal const val argumentBrandIdLovCallback = "brandIdLovCallback"

fun NavGraphBuilder.brandLov(
    onBackClick: () -> Unit,
    onItemClick: () -> Unit
) {
    composable(route = "$brandLovRoute?$argumentCategoryId={$argumentCategoryId}") {
        val brandViewModel = hiltViewModel<BrandLovViewModel>()

        BrandLov(
            viewModel = brandViewModel,
            onBackClick = onBackClick,
            onItemClick = onItemClick
        )
    }
}

fun NavController.navigateToBrandLov(categoryId: UUID, navOptions: NavOptions? = null) {
    navigate(route = "$brandLovRoute?$argumentCategoryId={$categoryId}", navOptions = navOptions)
}