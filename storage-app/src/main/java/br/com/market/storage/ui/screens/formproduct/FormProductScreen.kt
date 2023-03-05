package br.com.market.storage.ui.screens.formproduct

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.storage.R
import br.com.market.storage.ui.components.DialogMessage
import br.com.market.storage.ui.components.SearchableStorageTopAppBar
import br.com.market.storage.ui.components.StorageAppLinearProgressIndicator
import br.com.market.storage.ui.components.buttons.IconButtonClose
import br.com.market.storage.ui.components.buttons.IconButtonDelete
import br.com.market.storage.ui.components.buttons.IconButtonSearch
import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.ui.states.FormProductUiState
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.viewmodels.FormProductViewModel
import kotlinx.coroutines.launch

@Composable
fun FormProductScreen(
    viewModel: FormProductViewModel,
    onBackClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onAfterDeleteProduct: () -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    FormProductScreen(
        state = state,
        onBackClick = onBackClick,
        onLogoutClick = onLogoutClick,
        onFABSaveProductClick = {
            coroutineScope.launch {
                state.onToggleLoading()
                val response = viewModel.saveProduct(it)
                if (response.success) {
                    Toast.makeText(
                        context,
                        "Produto Salvo com Sucesso.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    state.onToggleMessageDialog(response.error ?: "")
                }
                Log.i("teste", "showLoading: ${state.showLoading}")
                state.onToggleLoading()
                Log.i("teste", "showLoading: ${state.showLoading}")
            }
        },
        onDeletePoduct = {
            viewModel.deleteProduct()
            onAfterDeleteProduct()
        },
        onDialogConfirmClick = { brand ->
            viewModel.saveBrand(brand)
            Toast.makeText(
                context,
                "Marca Salva com Sucesso.",
                Toast.LENGTH_LONG
            ).show()
        },
        onMenuItemDeleteBrandClick = {
            viewModel.deleteBrand(it)
        },
        permissionNavToBrand = viewModel::permissionNavToBrand
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FormProductScreen(
    state: FormProductUiState = FormProductUiState(),
    onBackClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onFABSaveProductClick: (ProductDomain) -> Unit = { },
    onDeletePoduct: () -> Unit = { },
    onDialogConfirmClick: (BrandDomain) -> Unit = { },
    onMenuItemDeleteBrandClick: (Long) -> Unit = { },
    permissionNavToBrand: () -> Boolean = { false }
) {
    var tabIndex by remember { mutableStateOf(0) }

    Scaffold(topBar = {
        SearchableStorageTopAppBar(
            openSearch = state.openSearch,
            searchText = state.searchText,
            title = state.productId?.let {
                stringResource(R.string.form_product_screen_top_app_bar_title_update, state.productName)
            } ?: stringResource(R.string.form_product_screen_top_app_bar_title_register),
            onSearchChange = state.onSearchChange,
            onToggleSearch = state.onToggleSearch,
            onLogoutClick = onLogoutClick,
            onNavigationIconClick = onBackClick,
            showNavigationIcon = true,
            showOnlyCustomActions = true,
            customActions = {
                if (!state.openSearch) {
                    if (tabIndex == 0 && state.productId != null) {
                        IconButtonDelete(onDeletePoduct)
                    } else if (state.brands.isNotEmpty()) {
                        IconButtonSearch(state.onToggleSearch)
                    }
                } else {
                    IconButtonClose { state.onSearchChange("") }
                }
            }
        )
    }) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val (tabRowRef, horizontalPagerRef, loadingRef) = createRefs()
            val tabTitles = listOf("Produto", "Marca")
            val pagerState = rememberPagerState()
            val coroutineScope = rememberCoroutineScope()

            StorageAppLinearProgressIndicator(
                state.showLoading,
                Modifier
                    .constrainAs(loadingRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )

            DialogMessage(
                title = stringResource(R.string.warning_dialog_title),
                show = state.showDialogMessage,
                onDismissRequest = { state.onToggleMessageDialog("") },
                message = state.serverMessage
            )

            TabRow(
                modifier = Modifier.constrainAs(tabRowRef) {
                    start.linkTo(parent.start)
                    top.linkTo(loadingRef.bottom)
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
                            1 -> permissionNavToBrand()
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
                    1 -> permissionNavToBrand()
                    else -> false
                }
            ) { index ->
                tabIndex = index

                when (tabIndex) {
                    0 -> {
                        FormProduct(
                            state = state,
                            onFABSaveProductClick = onFABSaveProductClick
                        )
                    }
                    1 -> {
                        TabBrand(
                            state = state,
                            onDialogConfirmClick = onDialogConfirmClick,
                            onMenuItemDeleteBrandClick = onMenuItemDeleteBrandClick
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FormProductScreenPreview() {
    StorageTheme {
        Surface {
            FormProductScreen()
        }
    }
}