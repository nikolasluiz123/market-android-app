package br.com.market.storage.ui.screens.category

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.buttons.fab.FloatingActionButtonAdd
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.storage.ui.screens.brand.BrandListItem
import br.com.market.storage.ui.states.category.CategoryUIState

@Composable
fun CategoryScreenTabBrand(
    state: CategoryUIState = CategoryUIState(),
    onInactivateBrandsClick: () -> Unit = { },
    onFabAddClick: () -> Unit = { },
    onItemClick: (String, String) -> Unit = { _: String, _: String -> }
) {
    val pagingData = state.brands.collectAsLazyPagingItems()
    Scaffold(
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    IconButtonInactivate(
                        onClick = onInactivateBrandsClick,
                        enabled = pagingData.itemCount > 0
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonAdd(onClick = onFabAddClick)
                }
            )
        }
    ) { padding ->
        ConstraintLayout(modifier = Modifier.padding(padding)) {
            PagedVerticalListComponent(pagingItems = pagingData) {
                BrandListItem(
                    brandName = it.name,
                    active = it.active,
                    onItemClick = {
                        onItemClick(state.categoryDomain?.id!!, it.id!!)
                    }
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview
@Composable
fun TabBrandScreenPreview() {
    MarketTheme {
        Surface {
            CategoryScreenTabBrand()
        }
    }
}