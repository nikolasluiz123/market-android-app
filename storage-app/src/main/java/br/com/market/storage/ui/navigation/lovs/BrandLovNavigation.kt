package br.com.market.storage.ui.navigation.lovs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.storage.ui.component.lov.BrandLov
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.viewmodels.brand.BrandLovViewModel
import java.util.*

internal const val brandLovRoute = "brandLov"
internal const val brandLovNavResultCallbackKey = "brandLovCallbackKey"

fun NavGraphBuilder.brandLov(
    onItemClick: (UUID) -> Unit,
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