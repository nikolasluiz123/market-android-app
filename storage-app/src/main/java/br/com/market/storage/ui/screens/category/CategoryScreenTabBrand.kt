package br.com.market.storage.ui.screens.category

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.core.ui.components.buttons.fab.FloatingActionButtonAdd
import br.com.market.core.ui.components.filter.SimpleFilter
import br.com.market.storage.R
import br.com.market.storage.ui.screens.brand.BrandListItem
import br.com.market.storage.ui.states.category.CategoryUIState

@Composable
fun CategoryScreenTabBrand(
    state: CategoryUIState = CategoryUIState(),
    onInactivateBrandsClick: () -> Unit = { },
    onFabAddClick: () -> Unit = { },
    onItemClick: (String, String) -> Unit = { _: String, _: String -> },
    onSimpleFilterChange: (String) -> Unit = { }
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
                placeholderResId = R.string.category_screen_tab_brand_buscar_por
            ) {
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

            if (!searchActive) {
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(searchDividerRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            top.linkTo(searchBarRef.bottom)
                        }
                        .padding(top = 8.dp)
                )

                PagedVerticalListComponent(
                    pagingItems = pagingData,
                    modifier = Modifier
                        .constrainAs(listRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            linkTo(top = searchDividerRef.bottom, bottom = parent.bottom, bias = 0F)
                        }
                        .padding(bottom = 74.dp)
                ) {
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