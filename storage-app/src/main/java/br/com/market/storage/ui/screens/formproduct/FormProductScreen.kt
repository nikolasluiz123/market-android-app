package br.com.market.storage.ui.screens.formproduct

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
import br.com.market.storage.extensions.navParamToLong
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

/**
 * Tela de manutenção do produto e das marcas. Esse é o composable stateless,
 * mantem tudo que é estado da tela dentro o viewModel.
 *
 * @param viewModel ViewModel da tela.
 * @param onBackClick Listener executado ao clicar no botão de voltar.
 * @param onLogoutClick Listener executado ao clicar no item de menu de logout.
 * @param onSuccessDeleteProduct Listener executado ao deletar um produto com sucesso.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun FormProductScreen(
    viewModel: FormProductViewModel,
    onBackClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onSuccessDeleteProduct: () -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    FormProductScreen(
        state = state,
        isEditing = !viewModel.productId.isNullOrEmpty(),
        onBackClick = onBackClick,
        onLogoutClick = onLogoutClick,
        onFABSaveProductClick = {
            coroutineScope.launch {
                if (state.onValidateProduct()) {

                    state.onToggleLoading()

                    val productDomain = ProductDomain(
                        id = viewModel.productId.navParamToLong(),
                        name = state.productName,
                        imageUrl = state.productImage
                    )

                    val response = viewModel.saveProduct(productDomain)

                    if (response.success) {
                        Toast.makeText(context, context.getString(R.string.product_saved_message), Toast.LENGTH_LONG).show()
                    } else {
                        state.onToggleMessageDialog(response.error ?: "")
                    }

                    state.onToggleLoading()
                }
            }
        },
        onDeleteProduct = {
            coroutineScope.launch {
                state.onToggleLoading()

                val response = viewModel.deleteProduct()

                if (response.success) {
                    onSuccessDeleteProduct()
                } else {
                    state.onToggleMessageDialog(response.error ?: "")
                }

                state.onToggleLoading()
            }
        },
        onDialogConfirmClick = { brand ->
            coroutineScope.launch {
                state.onToggleLoading()

                val response = viewModel.saveBrand(brand)

                if (response.success) {
                    Toast.makeText(context, context.getString(R.string.save_brand_success_message), Toast.LENGTH_LONG).show()
                } else {
                    state.onToggleMessageDialog(response.error ?: "")
                }

                state.onToggleLoading()
            }
        },
        onMenuItemDeleteBrandClick = {
            coroutineScope.launch {
                state.onToggleLoading()

                val response = viewModel.deleteBrand(it)

                if (!response.success) {
                    state.onToggleMessageDialog(response.error ?: "")
                }

                state.onToggleLoading()
            }
        },
        permissionNavToBrand = viewModel::permissionNavToBrand,
        onSuccessDeleteProduct = onSuccessDeleteProduct
    )
}

/**
 * Tela de manutenção do produto e das marcas. Esse é o composable statefull,
 * ele trabalha diretamente com a classe de estado.
 *
 * @param state Objeto de estado da tela.
 * @param isEditing Flag que indica se está editando.
 * @param onBackClick Listener executado ao clicar no botão de voltar.
 * @param onLogoutClick Listener executado ao clicar no item de menu de logout.
 * @param onFABSaveProductClick Listener executado ao clicar no FAB de salvar o produto.
 * @param onDeleteProduct  Listener executado ao deletar um produto.
 * @param onDialogConfirmClick Listener executado ao clicar no botão confirmar na dialog de marca.
 * @param onMenuItemDeleteBrandClick Listener executado ao clicar no menu Delete no item da lista de marcas.
 * @param permissionNavToBrand Flag que define se tem permissão para navegar para a tab de brands.
 * @param onSuccessDeleteProduct Listener executado ao deletar um produto com sucesso.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FormProductScreen(
    state: FormProductUiState = FormProductUiState(),
    isEditing: Boolean = false,
    onBackClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onFABSaveProductClick: () -> Unit = { },
    onDeleteProduct: () -> Unit = { },
    onDialogConfirmClick: (BrandDomain) -> Unit = { },
    onMenuItemDeleteBrandClick: (Long) -> Unit = { },
    permissionNavToBrand: () -> Boolean = { false },
    onSuccessDeleteProduct: () -> Unit = { },

    ) {
    var tabIndex by remember { mutableStateOf(0) }
    var isDelete = false

    Scaffold(topBar = {
        SearchableStorageTopAppBar(
            openSearch = state.openSearch,
            searchText = state.searchText,
            title = if (isEditing) stringResource(R.string.form_product_screen_top_app_bar_title_update)
            else stringResource(R.string.form_product_screen_top_app_bar_title_register),
            onSearchChange = state.onSearchChange,
            onToggleSearch = state.onToggleSearch,
            onLogoutClick = onLogoutClick,
            onNavigationIconClick = onBackClick,
            showNavigationIcon = true,
            showOnlyCustomActions = true,
            customActions = {
                if (!state.openSearch) {
                    if (tabIndex == 0 && isEditing) {
                        IconButtonDelete {
                            onDeleteProduct()
                            isDelete = true
                        }
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
                message = state.serverMessage,
                onDialogOkClick = {
                    if (isDelete) {
                        onSuccessDeleteProduct()
                    }
                }
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
                        TabProduct(
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