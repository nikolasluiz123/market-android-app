package br.com.market.storage.ui.screens.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
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
            val (categoriesListRef) = createRefs()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(categoriesListRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        linkTo(top = parent.top, bottom = parent.bottom, bias = 0F)
                    },
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {

                when (pagingData.loadState.refresh) {
                    is LoadState.Error -> {
                        item {
                            Box(modifier = Modifier.fillParentMaxSize()) {
                                Text(text = "Ocorreu um Erro", Modifier.clickable {
                                    pagingData.refresh()
                                })
                            }
                        }
                    }
                    is LoadState.Loading -> {
                        item {
                            Box(modifier = Modifier.fillParentMaxSize()) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                        }
                    }
                    is LoadState.NotLoading -> {
                        items(items = pagingData) { category ->
                            category?.let {
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

                when (pagingData.loadState.append) {
                    is LoadState.Error -> {}
                    is LoadState.Loading -> {
                        item {
                            Box(Modifier.fillMaxWidth()) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                        }
                    }
                    is LoadState.NotLoading -> {}
                }

                when (pagingData.loadState.prepend) {
                    is LoadState.Error -> {}
                    is LoadState.NotLoading -> {}
                    is LoadState.Loading -> {
                        item {
                            Box(Modifier.fillMaxWidth()) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
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