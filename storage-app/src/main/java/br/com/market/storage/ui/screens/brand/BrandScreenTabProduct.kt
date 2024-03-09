package br.com.market.storage.ui.screens.brand

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.market.compose.components.MarketBottomAppBar
import br.com.market.market.compose.components.button.fab.FloatingActionButtonAdd
import br.com.market.market.compose.components.button.icons.IconButtonStorage
import br.com.market.market.compose.components.filter.SimpleFilter
import br.com.market.market.compose.components.list.PagedVerticalListWithEmptyState
import br.com.market.storage.R
import br.com.market.storage.ui.states.brand.BrandUIState

@Composable
fun BrandScreenTabProduct(
    state: BrandUIState,
    onFabAddClick: () -> Unit = { },
    onProductClick: (String) -> Unit = { },
    onStorageButtonClick: (String, String) -> Unit = { _, _ -> },
    onSimpleFilterChange: (String) -> Unit = { }
) {
    val pagingData = state.products.collectAsLazyPagingItems()

    Scaffold(
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    IconButtonStorage(
                        onClick = {
                            onStorageButtonClick(state.categoryDomain?.id!!, state.brandDomain?.id!!)
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonAdd(onClick = onFabAddClick)
                }
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
                placeholderResId = R.string.brand_screen_tab_product_buscar_por
            ) {
                PagedVerticalListWithEmptyState(pagingItems = pagingData) {
                    ProductListItem(
                        name = it.productName,
                        price = it.productPrice,
                        quantity = it.productQuantity,
                        quantityUnit = it.productQuantityUnit,
                        image = it.imageBytes ?: it.imageUrl!!,
                        active = it.active,
                        onItemClick = {
                            onProductClick(it.id!!)
                        }
                    )
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }
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

                PagedVerticalListWithEmptyState(
                    pagingItems = pagingData,
                    modifier = Modifier
                        .constrainAs(listRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            linkTo(top = searchDividerRef.bottom, bottom = parent.bottom, bias = 0F)
                        }
                        .padding(bottom = 74.dp)
                ) {
                    ProductListItem(
                        name = it.productName,
                        price = it.productPrice,
                        quantity = it.productQuantity,
                        quantityUnit = it.productQuantityUnit,
                        image = it.imageBytes ?: it.imageUrl!!,
                        active = it.active,
                        onItemClick = {
                            onProductClick(it.id!!)
                        }
                    )
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}