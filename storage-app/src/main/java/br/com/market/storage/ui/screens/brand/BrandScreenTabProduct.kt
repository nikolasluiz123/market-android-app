package br.com.market.storage.ui.screens.brand

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.storage.ui.states.brand.BrandUIState

@Composable
fun BrandScreenTabProduct(
    state: BrandUIState,
    onFabAddClick: () -> Unit = { },
    onProductClick: (String) -> Unit = { }
) {
    val pagingData = state.products.collectAsLazyPagingItems()

    Scaffold(
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    IconButtonInactivate(
                        onClick = {  },
                        enabled = false
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonAdd(onClick = onFabAddClick)
                }
            )
        }
    ) { padding ->
        ConstraintLayout(Modifier.padding(padding)) {
            PagedVerticalListComponent(pagingItems = pagingData) {
                ProductListCard(
                    name = it.productName,
                    price = it.productPrice,
                    quantity = it.productQuantity,
                    quantityUnit = it.productQuantityUnit,
                    image = it.imageBytes ?: it.imageUrl!!,
                    active = it.productActive,
                    onItemClick = {
                        onProductClick(it.productId)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun BrandScreenTabProductPreview() {

}