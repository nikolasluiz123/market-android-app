package br.com.market.storage.ui.screens.brand

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
import br.com.market.storage.ui.states.brand.BrandUIState
import br.com.market.storage.ui.viewmodels.brand.BrandViewModel
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun BrandScreen(
    viewModel: BrandViewModel,
    onBackClick: () -> Unit,
    onNavToBrandLov: (UUID, (UUID) -> Unit) -> Unit,
    onFabAddProductClick: (UUID) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    BrandScreen(
        state = state,
        onBackClick = onBackClick,
        onToggleActive = {
            viewModel.toggleActive()
        },
        onSaveBrandClick = {
            viewModel.saveBrand()
        },
        onNavToBrandLov = { categoryId ->
            onNavToBrandLov(categoryId) { brandId ->
                viewModel.findBrandById(brandId)
            }
        },
        onFabAddProductClick = onFabAddProductClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BrandScreen(
    state: BrandUIState = BrandUIState(),
    onBackClick: () -> Unit = { },
    onToggleActive: () -> Unit = { },
    onSaveBrandClick: (Boolean) -> Unit = { },
    onNavToBrandLov: (UUID) -> Unit = { },
    onFabAddProductClick: (UUID) -> Unit = { }
) {
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
            val (tabRowRef, horizontalPagerRef) = createRefs()

            val tabTitles = listOf(
                stringResource(R.string.brand_screen_label_tab_brand),
                stringResource(R.string.brand_screen_label_tab_product)
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
                        BrandScreenTaBrand(
                            state = state,
                            onUpdateEditMode = { isEditMode = it },
                            onToggleActive = onToggleActive,
                            onSaveBrandClick = onSaveBrandClick,
                            isEdit = isEditMode,
                            onNavToBrandLov = onNavToBrandLov
                        )
                    }
                    1 -> {
                        BrandScreenTabProduct(
                            onFabAddClick = {
                                onFabAddProductClick(state.brandDomain?.id!!)
                            }
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