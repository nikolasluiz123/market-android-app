package br.com.market.storage.ui.screens.brand

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.core.ui.components.buttons.IconButtonStorage
import br.com.market.core.ui.components.buttons.fab.FloatingActionButtonAdd
import br.com.market.storage.ui.states.brand.BrandUIState

@Composable
fun BrandScreenTabProduct(
    state: BrandUIState,
    onFabAddClick: () -> Unit = { },
    onProductClick: (String) -> Unit = { },
    onStorageButtonClick: (String, String) -> Unit = { _,_ -> },
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
            PagedVerticalListComponent(pagingItems = pagingData) {
                ProductListItem(
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
                Divider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview
@Composable
fun BrandScreenTabProductPreview() {

}