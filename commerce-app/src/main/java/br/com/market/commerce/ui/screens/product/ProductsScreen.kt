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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.commerce.R
import br.com.market.commerce.ui.state.ProductsUIState
import br.com.market.commerce.ui.viewmodel.products.ProductsViewModel
import br.com.market.core.theme.MarketTheme
import br.com.market.domain.ProductImageReadDomain
import br.com.market.market.compose.components.button.icons.IconButtonAdvancedFilters
import br.com.market.market.compose.components.filter.SimpleFilter
import br.com.market.market.compose.components.list.PagedVerticalListWithEmptyState
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel,
) {
    val state by viewModel.uiState.collectAsState()

    ProductsScreen(
        state = state,
        onSimpleFilterChange = viewModel::onSimpleFilterChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    state: ProductsUIState = ProductsUIState(),
    onSimpleFilterChange: (String) -> Unit = { }
) {
    val pagingItems = state.products.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Produtos"
            )
        }
    ) { padding ->
        ConstraintLayout(Modifier.padding(padding)) {
            val (listRef, searchBarRef, searchDividerRef) = createRefs()

            var searchActive by remember { mutableStateOf(false) }

            SimpleFilter(
                modifier = Modifier
                    .constrainAs(searchBarRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth(),
                onSimpleFilterChange = onSimpleFilterChange,
                active = searchActive,
                onActiveChange = { searchActive = it },
                placeholderResId = R.string.products_screen_label_shearch_for,
                trailingIcon = {
                    IconButtonAdvancedFilters()
                }
            ) {
                ProductList(pagingItems = pagingItems)
            }

            if (!searchActive) {
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(searchDividerRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            top.linkTo(searchBarRef.bottom)
                        }
                )

                ProductList(
                    pagingItems = pagingItems,
                    modifier = Modifier.constrainAs(listRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        linkTo(top = searchDividerRef.top, bottom = parent.bottom, bias = 0F)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductList(pagingItems: LazyPagingItems<ProductImageReadDomain>, modifier: Modifier = Modifier) {
    PagedVerticalListWithEmptyState(modifier = modifier, pagingItems = pagingItems) {
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

@Preview
@Composable
fun ProductsScreenPreview() {
    MarketTheme {
        Surface {
            ProductsScreen()
        }
    }
}
