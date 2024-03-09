package br.com.market.market.compose.components.lov

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.theme.MarketTheme
import br.com.market.domain.ProductImageReadDomain
import br.com.market.market.compose.components.R
import br.com.market.market.compose.components.filter.SimpleFilter
import br.com.market.market.compose.components.list.PagedVerticalListWithEmptyState
import br.com.market.market.compose.components.lov.items.ProductLovListItem
import br.com.market.market.compose.components.lov.state.ProductLovUIState
import br.com.market.market.compose.components.lov.viewmodel.ProductLovViewModel
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar

@Composable
fun ProductLov(
    viewModel: ProductLovViewModel,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()

    ProductLov(
        state = state,
        onItemClick = onItemClick,
        onFilterChange = viewModel::updateList,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductLov(
    state: ProductLovUIState = ProductLovUIState(),
    onItemClick: (String) -> Unit = { },
    onFilterChange: (String) -> Unit = { },
    onBackClick: () -> Unit = { }
) {
    val pagingData = state.products.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            Column {
                SimpleMarketTopAppBar(
                    title = stringResource(R.string.lov_products_title),
                    showMenuWithLogout = false,
                    onBackClick = onBackClick
                )
            }
        }
    ) { padding ->
        ConstraintLayout(modifier = Modifier.padding(padding)) {
            val (listRef, searchBarRef, searchDividerRef) = createRefs()
            var searchActive by rememberSaveable { mutableStateOf(false) }

            SimpleFilter(
                modifier = Modifier
                    .constrainAs(searchBarRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth(),
                onSimpleFilterChange = onFilterChange,
                active = searchActive,
                onActiveChange = { searchActive = it },
                placeholderResId = R.string.product_lov_placeholder_filter
            ) {
                ProductList(
                    pagingData = pagingData,
                    onItemClick = onItemClick
                )
            }

            if (!searchActive) {
                HorizontalDivider(Modifier
                    .fillMaxWidth()
                    .constrainAs(searchDividerRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        top.linkTo(searchBarRef.bottom)
                    }
                    .padding(top = 8.dp)
                )

                ProductList(
                    modifier = Modifier
                        .constrainAs(listRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            linkTo(top = searchDividerRef.bottom, bottom = parent.bottom, bias = 0F)
                        }
                        .padding(bottom = 74.dp),
                    pagingData = pagingData,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
private fun ProductList(
    pagingData: LazyPagingItems<ProductImageReadDomain>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PagedVerticalListWithEmptyState(modifier = modifier, pagingItems = pagingData) { productTuple ->
        ProductLovListItem(
            name = productTuple.productName,
            price = productTuple.productPrice,
            quantity = productTuple.productQuantity,
            quantityUnit = productTuple.productQuantityUnit,
            image = productTuple.imageBytes!!,
            active = productTuple.active,
            onItemClick = {
                onItemClick(productTuple.id!!)
            }
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
fun ProductLovPreview() {
    MarketTheme {
        Surface {
            ProductLov()
        }
    }
}