package br.com.market.core.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.filter.AdvancedFiltersScreenArgs
import br.com.market.core.filter.CommonAdvancedFilterItem
import br.com.market.core.filter.formatter.IAdvancedFilterFormatter
import br.com.market.core.gson.GsonTypeAdapterAdapter
import br.com.market.core.ui.components.filter.AdvancedFilterScreen
import br.com.market.core.ui.viewmodel.filter.AdvancedFilterViewModel
import com.google.gson.Gson

internal const val advancedFilterScreen = "advancedFilterScreen"
internal const val argumentFilters = "filters"

fun NavGraphBuilder.advancedFilterScreen(
    onItemClick: (CommonAdvancedFilterItem, (Any) -> Unit) -> Unit
) {
    composable(
        route = "$advancedFilterScreen?$argumentFilters={$argumentFilters}"
    ) {
        val viewModel = hiltViewModel<AdvancedFilterViewModel>()

        AdvancedFilterScreen(
            viewModel = viewModel,
            onItemClick = onItemClick
        )
    }
}

fun NavController.navigateToAdvancedFilterScreen(
    args: AdvancedFiltersScreenArgs,
    navOptions: NavOptions? = null
) {
    val gson = Gson()
        .newBuilder()
        .registerTypeAdapter(IAdvancedFilterFormatter::class.java, GsonTypeAdapterAdapter())
        .create()

    val json = gson.getAdapter(AdvancedFiltersScreenArgs::class.java).toJson(args)

    navigate(
        route = "$advancedFilterScreen?$argumentFilters={$json}",
        navOptions = navOptions
    )
}