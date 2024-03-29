package br.com.market.storage.ui.screens.brand

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.callbacks.IServiceOperationCallback
import br.com.market.core.callbacks.ITextInputNavigationCallback
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.dialog.MarketDialog
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import br.com.market.storage.R
import br.com.market.storage.ui.states.brand.BrandUIState
import br.com.market.storage.ui.viewmodels.brand.BrandViewModel
import kotlinx.coroutines.launch

@Composable
fun BrandScreen(
    viewModel: BrandViewModel,
    onBackClick: () -> Unit,
    onNavToBrandLov: (String, (String) -> Unit) -> Unit,
    onFabAddProductClick: (String, String) -> Unit,
    onProductClick: (String, String, String) -> Unit,
    onStorageButtonClick: (String, String) -> Unit,
    textInputCallback: ITextInputNavigationCallback
) {
    val state by viewModel.uiState.collectAsState()

    BrandScreen(
        state = state,
        onBackClick = onBackClick,
        onToggleActive = viewModel::toggleActive,
        saveBrandCallback = viewModel::saveBrand,
        onNavToBrandLov = { categoryId ->
            onNavToBrandLov(categoryId) { brandId ->
                viewModel.findBrandById(brandId)
            }
        },
        onFabAddProductClick = onFabAddProductClick,
        onProductClick = onProductClick,
        onStorageButtonClick = onStorageButtonClick,
        onSimpleFilterChange = viewModel::onSimpleFilterChange,
        textInputCallback = textInputCallback
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BrandScreen(
    state: BrandUIState = BrandUIState(),
    onBackClick: () -> Unit = { },
    onToggleActive: IServiceOperationCallback? = null,
    saveBrandCallback: IServiceOperationCallback? = null,
    onNavToBrandLov: (categoryId: String) -> Unit = { },
    onFabAddProductClick: (String, String) -> Unit = { _, _ -> },
    onProductClick: (String, String, String) -> Unit = { _, _, _ -> },
    onStorageButtonClick: (String, String) -> Unit = { _, _ -> },
    onSimpleFilterChange: (String) -> Unit = { },
    textInputCallback: ITextInputNavigationCallback? = null
) {
    LaunchedEffect(state.internalErrorMessage) {
        if (state.internalErrorMessage.isNotEmpty()) {
            state.onShowDialog?.onShow(type = EnumDialogType.ERROR, message = state.internalErrorMessage, onConfirm = {}, onCancel = {})
            state.internalErrorMessage = ""
        }
    }

    var isEditMode by remember(state.brandDomain) {
        mutableStateOf(state.brandDomain != null)
    }

    Scaffold(
        topBar = {
            val title = "Categoria ${state.categoryDomain?.name}"
            val subtitle = if (isEditMode) state.brandDomain?.name else "Nova Marca"

            SimpleMarketTopAppBar(
                title = title,
                subtitle = subtitle,
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
            val (tabRowRef, horizontalPagerRef, tabDividerRef) = createRefs()

            val tabTitles = listOf(
                stringResource(R.string.brand_screen_label_tab_brand),
                stringResource(R.string.brand_screen_label_tab_product)
            )

            val pagerState = rememberPagerState { 2 }
            val coroutineScope = rememberCoroutineScope()
            var tabIndex by remember { mutableIntStateOf(0) }

            MarketDialog(
                type = state.dialogType,
                show = state.showDialog,
                onDismissRequest = { state.onHideDialog() },
                message = state.dialogMessage,
                onConfirm = state.onConfirm,
                onCancel = state.onCancel
            )

            HorizontalDivider(
                modifier = Modifier.constrainAs(tabDividerRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0f)
                    top.linkTo(parent.top)
                }.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSecondary
            )

            TabRow(
                modifier = Modifier.constrainAs(tabRowRef) {
                    start.linkTo(parent.start)
                    top.linkTo(tabDividerRef.bottom)
                    end.linkTo(parent.end)
                },
                selectedTabIndex = tabIndex,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                divider = {
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSecondary)
                }
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
                modifier = Modifier
                    .constrainAs(horizontalPagerRef) {
                        start.linkTo(parent.start)
                        top.linkTo(tabRowRef.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        height = Dimension.fillToConstraints
                    },
                state = pagerState,
                pageSpacing = 0.dp,
                userScrollEnabled = when (tabIndex) {
                    0 -> true
                    1 -> isEditMode
                    else -> false
                },
                reverseLayout = false,
                contentPadding = PaddingValues(0.dp),
                beyondBoundsPageCount = 0,
                pageSize = PageSize.Fill,
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                key = null,
                pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                    state = pagerState,
                    orientation = Orientation.Horizontal
                )
            ) { index ->
                tabIndex = index

                when (tabIndex) {
                    0 -> {
                        BrandScreenTaBrand(
                            state = state,
                            onUpdateEditMode = { isEditMode = it },
                            toggleActive = onToggleActive,
                            saveBrandCallback = saveBrandCallback,
                            isEdit = isEditMode,
                            onNavToBrandLov = onNavToBrandLov,
                            textInputCallback = textInputCallback
                        )
                    }

                    1 -> {
                        BrandScreenTabProduct(
                            state = state,
                            onFabAddClick = {
                                onFabAddProductClick(
                                    state.categoryDomain?.id!!,
                                    state.brandDomain?.id!!
                                )
                            },
                            onProductClick = { productId ->
                                onProductClick(
                                    state.categoryDomain?.id!!,
                                    state.brandDomain?.id!!,
                                    productId
                                )
                            },
                            onStorageButtonClick = onStorageButtonClick,
                            onSimpleFilterChange = onSimpleFilterChange
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BrandScreenPreview() {
    MarketTheme {
        Surface {
            BrandScreen()
        }
    }
}