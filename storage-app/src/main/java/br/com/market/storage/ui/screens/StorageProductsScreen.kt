package br.com.market.storage.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.storage.R
import br.com.market.storage.sampledata.sampleProducts
import br.com.market.storage.ui.components.*
import br.com.market.storage.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.storage.ui.screens.formproduct.CardProductItem
import br.com.market.storage.ui.states.StorageProductsUiState
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.theme.colorPrimary
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel
import kotlinx.coroutines.launch

/**
 * Tela de listagem dos produtos em estoque stateless.
 *
 * @param viewModel ViewModel da tela.
 * @param onItemClick Listener executado ao clicar em um item da lista.
 * @param onLogoutClick Listener executado ao clicar no item de menu Logout.
 * @param onFABNewProductClick Listener executado ao clicar no FAB de novo produto.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun StorageProductsScreen(
    viewModel: StorageProductsViewModel,
    onItemClick: (Long) -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onFABNewProductClick: () -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    StorageProductsScreen(
        state = state,
        onItemClick = onItemClick,
        onLogoutClick = onLogoutClick,
        onFABNewProductClick = onFABNewProductClick,
        onSynchronizeClick = {
            coroutineScope.launch {
                state.onToggleLoading()

                val responseSyncProducts = viewModel.syncProducts()

                if (responseSyncProducts.success) {
                    val responseSyncBrands = viewModel.syncBrands()

                    if (responseSyncBrands.success) {
                        Toast.makeText(context, "Produtos Sincronizados com Sucesso.", Toast.LENGTH_LONG).show()
                    } else {
                        state.onToggleMessageDialog(responseSyncProducts.error ?: "")
                    }
                } else {
                    state.onToggleMessageDialog(responseSyncProducts.error ?: "")
                }

                state.onToggleLoading()
            }
        }
    )
}

/**
 * Tela de listagem dos produtos em estoque statefull.
 *
 * @param state Objeto de estado da tela.
 * @param onItemClick Listener executado ao clicar em um item da lista.
 * @param onLogoutClick Listener executado ao clicar no item de menu Logout.
 * @param onFABNewProductClick Listener executado ao clicar no FAB de novo produto.
 * @param onSynchronizeClick Listener executado ao clicar no item de menu Sincronizar.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageProductsScreen(
    state: StorageProductsUiState = StorageProductsUiState(),
    onItemClick: (Long) -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onFABNewProductClick: () -> Unit = { },
    onSynchronizeClick: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            SearchableStorageTopAppBar(
                openSearch = state.openSearch,
                searchText = state.searchText,
                title = stringResource(R.string.storage_products_screen_app_bar_title),
                onSearchChange = state.onSearchChange,
                onToggleSearch = state.onToggleSearch,
                onLogoutClick = onLogoutClick,
                menuItems = {
                    DropdownMenuItem(
                        text = {
                            BadgedBox(badge = { Badge(containerColor = colorPrimary) { Text(state.registersCountToSync.toString()) } }) {
                                Text(stringResource(R.string.label_sinc))
                            }
                        },
                        onClick = onSynchronizeClick,
                        enabled = state.registersCountToSync > 0
                    )
                }
            )
        },
        floatingActionButton = { FloatingActionButtonAdd(onClick = onFABNewProductClick) }
    ) { paddingValues ->

        DialogMessage(
            title = stringResource(R.string.error_dialog_title),
            show = state.showDialogMessage,
            onDismissRequest = { state.onToggleMessageDialog("") },
            message = state.serverMessage
        )

        LazyVerticalGridComponent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            items = state.products,
            isSearching = state.isSearching(),
            emptyStateText = stringResource(R.string.storage_products_screen_empty_state)
        ) {
            CardProductItem(
                product = it,
                onClick = onItemClick
            )
        }

        StorageAppCircularBlockUIProgressIndicator(
            state.showLoading,
            stringResource(R.string.label_sync)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun StorageProductsPreview() {
    StorageTheme {
        Surface {
            StorageProductsScreen(state = StorageProductsUiState(products = sampleProducts))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun StorageProductsEmptyPreview() {
    StorageTheme {
        Surface {
            StorageProductsScreen()
        }
    }
}