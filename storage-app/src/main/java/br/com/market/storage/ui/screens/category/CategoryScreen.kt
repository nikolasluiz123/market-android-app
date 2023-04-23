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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.FailureScreen
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.MarketLinearProgressIndicator
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.FloatingActionButtonSave
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.core.ui.components.buttons.IconButtonReactivate
import br.com.market.domain.CategoryDomain
import br.com.market.storage.R
import br.com.market.storage.ui.states.category.CategoryUIState
import br.com.market.storage.ui.viewmodels.category.CategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onBackClick: () -> Unit
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
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryScreen(
    state: CategoryUIState,
    onToggleActive: () -> Unit = { },
    onSaveCategoryClick: () -> Unit = { },
    onButtonBackClickFailureScreen: () -> Unit = { },
    onButtonRetryClick: () -> Unit = { },
    onBackClick: () -> Unit = { }
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isEditMode by remember((state as CategoryUIState.Success).categoryDomain) {
        mutableStateOf(state.categoryDomain != null)
    }

    var isActive by remember((state as CategoryUIState.Success).categoryDomain?.active) {
        mutableStateOf(state.categoryDomain?.active ?: true)
    }

    Scaffold(
        topBar = {
            val title = if (isEditMode) {
                state as CategoryUIState.Success
                "Categoria ${state.categoryDomain?.name}"
            } else {
                "Nova Categoria"
            }

            SimpleMarketTopAppBar(
                title = title,
                showMenuWithLogout = false,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    if (isActive) {
                        IconButtonInactivate(
                            enabled = isEditMode,
                            onClick = {
                                onToggleActive()
                                isActive = false

                                scope.launch {
                                    snackbarHostState.showSnackbar("Categoria Inativada com Sucesso")
                                }
                            }
                        )
                    } else {
                        IconButtonReactivate(
                            enabled = isEditMode,
                            onClick = {
                                onToggleActive()
                                isActive = true

                                scope.launch {
                                    snackbarHostState.showSnackbar("Categoria Reativada com Sucesso")
                                }
                            }
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButtonSave(
                        onClick = {
                            state as CategoryUIState.Success
                            isEditMode = saveCategory(state, isActive, isEditMode, onSaveCategoryClick, scope, snackbarHostState)
                        }
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(modifier = Modifier.padding(8.dp)) {
                    Text(text = it.visuals.message)
                }
            }
        }
    ) {
        when (state) {
            CategoryUIState.Failure -> {
                FailureScreen(
                    modifier = Modifier.padding(it),
                    onButtonBackClick = onButtonBackClickFailureScreen,
                    onButtonRetryClick = onButtonRetryClick
                )
            }
            CategoryUIState.Loading -> {
                ConstraintLayout(modifier = Modifier.padding(it)) {
                    val loadingRef = createRef()

                    MarketLinearProgressIndicator(
                        modifier = Modifier.constrainAs(loadingRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        },
                        show = true
                    )
                }
            }
            is CategoryUIState.Success -> {
                ConstraintLayout(
                    modifier = Modifier
                        .padding(it)
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
                                TabCategoryScreen(
                                    state = state,
                                    isActive = isActive,
                                    onSendClick = {
                                        isEditMode = saveCategory(state, isActive, isEditMode, onSaveCategoryClick, scope, snackbarHostState)
                                    }
                                )
                            }
                            1 -> {

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
fun CategoryScreenFailurePreview() {
    MarketTheme {
        Surface {
            CategoryScreen(state = CategoryUIState.Failure)
        }
    }
}

@Preview
@Composable
fun CategoryScreenLoadingPreview() {
    MarketTheme {
        Surface {
            CategoryScreen(state = CategoryUIState.Loading)
        }
    }
}

@Preview
@Composable
fun CategoryScreenEmptyListPreview() {
    MarketTheme {
        Surface {
            CategoryScreen(state = CategoryUIState.Success())
        }
    }
}


private fun saveCategory(
    state: CategoryUIState.Success,
    isActive: Boolean,
    isEditMode: Boolean,
    onSaveCategoryClick: () -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
): Boolean {
    if (state.onValidate() && isActive) {

        state.categoryDomain = if (isEditMode) {
            state.categoryDomain?.copy(name = state.categoryName)
        } else {
            CategoryDomain(name = state.categoryName)
        }

        onSaveCategoryClick()

        scope.launch {
            snackbarHostState.showSnackbar("Categoria Salva com Sucesso")
        }
    }

    return state.categoryDomain != null
}