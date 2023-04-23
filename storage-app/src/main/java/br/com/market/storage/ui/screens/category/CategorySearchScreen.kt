package br.com.market.storage.ui.screens.category

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.core.ui.components.buttons.IconButtonLogout
import br.com.market.core.ui.components.buttons.IconButtonSync
import br.com.market.storage.ui.states.category.CategorySearchUIState
import br.com.market.storage.ui.viewmodels.category.CategorySearchViewModel
import java.util.*

@Composable
fun CategorySearchScreen(
    viewModel: CategorySearchViewModel,
    onAddCategoryClick: () -> Unit,
    onCategoryClick: (UUID) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    CategorySearchScreen(
        state = state,
        onAddCategoryClick = onAddCategoryClick,
        onCategoryClick = onCategoryClick,
        onSyncClick = {
            viewModel.sync()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySearchScreen(
    state: CategorySearchUIState,
    onDeleteCategoryClick: () -> Unit = { },
    onAddCategoryClick: () -> Unit = { },
    onCategoryClick: (UUID) -> Unit = { },
    onSyncClick: () -> Unit = { }
) {
    val pagingData = state.categories.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Categorias",
                showNavigationIcon = false,
                showMenuWithLogout = false,
                actions = {
                    IconButtonSync(onSyncClick)
                    IconButtonLogout()
                }
            )
        },
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    IconButtonInactivate(
                        onClick = onDeleteCategoryClick,
                        enabled = pagingData.itemCount > 0
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonAdd(onClick = onAddCategoryClick)
                }
            )
        }
    ) { padding ->
        ConstraintLayout(modifier = Modifier.padding(padding)) {

            PagedVerticalListComponent(pagingItems = pagingData) {
                CategoryListCard(
                    categoryName = it.name,
                    active = it.active,
                    onItemClick = {
                        onCategoryClick(it.id!!)
                    }
                )
            }

        }
    }
}

@Preview
@Composable
fun CategorySearchScreenPreview() {
    MarketTheme {
        Surface {
            CategorySearchScreen(state = CategorySearchUIState())
        }
    }
}