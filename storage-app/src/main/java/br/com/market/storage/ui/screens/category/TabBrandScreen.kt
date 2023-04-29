package br.com.market.storage.ui.screens.category

import androidx.compose.foundation.layout.padding
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
import br.com.market.core.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.storage.ui.screens.brand.BrandListCard
import br.com.market.storage.ui.states.category.CategoryUIState

@Composable
fun TabBrandScreen(
    state: CategoryUIState = CategoryUIState(),
    onInactivateBrandsClick: () -> Unit = { },
    onFabAddClick: () -> Unit = { }
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
                BrandListCard(brandName = it.name)
            }
        }
    }
}

@Preview
@Composable
fun TabBrandScreenPreview() {
    MarketTheme {
        Surface {
            TabBrandScreen()
        }
    }
}