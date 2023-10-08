package br.com.market.commerce.ui.screens.product

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.commerce.ui.state.ProductsUIState
import br.com.market.commerce.ui.viewmodel.ProductsViewModel
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.list.PagedVerticalListWithEmptyState
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel,
) {
    val state by viewModel.uiState.collectAsState()

    ProductsScreen(
        state = state,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    state: ProductsUIState = ProductsUIState(),
) {
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Produtos"
            )
        }
    ) { padding ->
        ConstraintLayout(Modifier.padding(padding)) {
            PagedVerticalListWithEmptyState(pagingItems = state.products.collectAsLazyPagingItems()) {
                ProductListItem(
                    name = it.productName,
                    price = it.productPrice,
                    quantity = it.productQuantity,
                    quantityUnit = it.productQuantityUnit,
                    image = it.imageBytes!!,
                    active = it.productActive,
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview
@Composable
fun ProductsScreenPreview() {
    MarketTheme {
        Surface {
            ProductsScreen()
        }
    }
}
