package br.com.market.commerce.ui.navigation.products

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.commerce.ui.screens.product.ProductsAdvancedFilterScreen
import br.com.market.commerce.ui.viewmodel.products.ProductsAdvancedFilterViewModel
import br.com.market.core.extensions.navigateForResult
import br.com.market.core.gson.adapter.LocalDateTimeAdapter
import br.com.market.localdataaccess.filter.ProductsScreenFilters
import com.google.gson.GsonBuilder
import java.time.LocalDateTime

internal const val productsAdvancedFilterScreen = "productsAdvancedFilterScreen"
internal const val productsAdvancedFilterNavResultCallbackKey = "productsAdvancedFilterNavResultCallbackKey"
internal const val argumentProductsAdvancedFilterJson = "argumentProductsAdvancedFilterJson"

fun NavGraphBuilder.productsAdvancedFiltersScreen(
) {
    composable(
        route = "$productsAdvancedFilterScreen?$argumentProductsAdvancedFilterJson={$argumentProductsAdvancedFilterJson}"
    ) {
        val viewModel = hiltViewModel<ProductsAdvancedFilterViewModel>()

        ProductsAdvancedFilterScreen(viewModel = viewModel)
    }
}

fun NavController.navigateToProductsAdvancedFilterScreen(filter: ProductsScreenFilters, callback: (ProductsScreenFilters) -> Unit) {
    val json = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()
        .getAdapter(ProductsScreenFilters::class.java)
        .toJson(filter)

    navigateForResult(
        key = productsAdvancedFilterNavResultCallbackKey,
        route = "$productsAdvancedFilterScreen?$argumentProductsAdvancedFilterJson={$json}",
        callback = callback
    )
}