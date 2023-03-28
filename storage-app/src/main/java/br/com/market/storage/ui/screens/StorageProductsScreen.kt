package br.com.market.storage.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.DialogMessage
import br.com.market.core.ui.components.LazyVerticalGridComponent
import br.com.market.core.ui.components.MarketCircularBlockUIProgressIndicator
import br.com.market.core.ui.components.SearchableMarketTopAppBar
import br.com.market.core.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.storage.R
import br.com.market.storage.sampledata.sampleProducts
import br.com.market.storage.ui.screens.formproduct.CardProductItem
import br.com.market.storage.ui.states.StorageProductsUiState
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel
import kotlinx.coroutines.launch
import java.util.*

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
    onItemClick: (UUID) -> Unit = { },
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

//                val responseSyncProducts = viewModel.syncProducts()
//
//                if (responseSyncProducts.success) {
//                    val responseSyncBrands = viewModel.syncBrands()
//
//                    if (responseSyncBrands.success) {
//                        Toast.makeText(context, "Produtos Sincronizados com Sucesso.", Toast.LENGTH_LONG).show()
//                    } else {
//                        state.onToggleMessageDialog(responseSyncProducts.error ?: "")
//                    }
//                } else {
//                    state.onToggleMessageDialog(responseSyncProducts.error ?: "")
//                }

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
@Composable
fun StorageProductsScreen(
    state: StorageProductsUiState = StorageProductsUiState(),
    onItemClick: (UUID) -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onFABNewProductClick: () -> Unit = { },
    onSynchronizeClick: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            SearchableMarketTopAppBar(
                openSearch = state.openSearch,
                searchText = state.searchText,
                title = stringResource(R.string.storage_products_screen_app_bar_title),
                onSearchChange = state.onSearchChange,
                onToggleSearch = state.onToggleSearch,
                onLogoutClick = onLogoutClick,
                menuItems = {
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.label_sinc))
                        },
                        onClick = onSynchronizeClick
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

        MarketCircularBlockUIProgressIndicator(
            state.showLoading,
            stringResource(br.com.market.core.R.string.label_sync)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun StorageProductsPreview() {
    MarketTheme {
        Surface {
            StorageProductsScreen(state = StorageProductsUiState(products = sampleProducts))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun StorageProductsEmptyPreview() {
    MarketTheme {
        Surface {
            StorageProductsScreen()
        }
    }
}