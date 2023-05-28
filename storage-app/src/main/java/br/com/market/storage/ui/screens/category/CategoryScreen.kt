package br.com.market.storage.ui.screens.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.storage.R
import br.com.market.storage.ui.states.category.CategoryUIState
import br.com.market.storage.ui.viewmodels.category.CategoryViewModel
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onBackClick: () -> Unit,
    onFabAddBrandClick: (String) -> Unit,
    onBrandItemClick: (String, String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    CategoryScreen(
        state = state,
        onToggleActive = {
            viewModel.toggleActive()
        },
        onSaveCategoryClick = {
            viewModel.saveCategory()
        },
        onBackClick = onBackClick,
        onFabAddBrandClick = onFabAddBrandClick,
        onBrandItemClick = onBrandItemClick
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    state: CategoryUIState,
    onToggleActive: () -> Unit = { },
    onSaveCategoryClick: (Boolean) -> Unit = { },
    onBackClick: () -> Unit = { },
    onFabAddBrandClick: (String) -> Unit = { },
    onBrandItemClick: (String, String) -> Unit = { _: String, _: String -> }
) {
    var isEditMode by remember(state.categoryDomain) {
        mutableStateOf(state.categoryDomain != null)
    }

    Scaffold(
        topBar = {
            val title = if (isEditMode) "Categoria ${state.categoryDomain?.name}" else "Nova Categoria"

            SimpleMarketTopAppBar(
                title = title,
                showMenuWithLogout = false,
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val (tabRowRef, horizontalPagerRef) = createRefs()
            val tabTitles = listOf(
                stringResource(R.string.category_screen_label_tab_category),
                stringResource(R.string.category_screen_label_tab_brand)
            )
            val pagerState = rememberPagerState()
            val coroutineScope = rememberCoroutineScope()
            var tabIndex by remember { mutableStateOf(0) }

            TabRow(
                modifier = Modifier.constrainAs(tabRowRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                selectedTabIndex = tabIndex
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = {
                            coroutineScope.launch {
                                tabIndex = index
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title) },
                        enabled = when (index) {
                            0 -> true
                            1 -> isEditMode
                            else -> false
                        }
                    )
                }
            }

            HorizontalPager(
                pageCount = tabTitles.size,
                state = pagerState,
                modifier = Modifier
                    .constrainAs(horizontalPagerRef) {
                        start.linkTo(parent.start)
                        top.linkTo(tabRowRef.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        height = Dimension.fillToConstraints
                    },
                userScrollEnabled = when (tabIndex) {
                    0 -> true
                    1 -> isEditMode
                    else -> false
                }
            ) { index ->
                tabIndex = index

                when (tabIndex) {
                    0 -> {
                        CategoryScreenTabCategory(
                            state = state,
                            onUpdateEditMode = { isEditMode = it },
                            onToggleActive = onToggleActive,
                            onSaveCategoryClick = onSaveCategoryClick,
                            isEdit = isEditMode
                        )
                    }
                    1 -> {
                        CategoryScreenTabBrand(
                            state = state,
                            onFabAddClick = {
                                onFabAddBrandClick(state.categoryDomain?.id!!)
                            },
                            onItemClick = onBrandItemClick
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoryScreenEmptyListPreview() {
    MarketTheme {
        Surface {
            CategoryScreen(state = CategoryUIState())
        }
    }
}