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
import br.com.market.storage.ui.states.StorageProductsUiState
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel
import kotlinx.coroutines.launch

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

                val response = viewModel.syncProducts()

                state.onToggleLoading()

                if (response.success) {
                    Toast.makeText(context, "Produtos Sincronizados com Sucesso.", Toast.LENGTH_LONG).show()
                } else {
                    state.onToggleMessageDialog(response.error ?: "")
                }
            }
        }
    )
}

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
                registersCountToSync = state.registersCountToSync,
                onSynchronizeClick = onSynchronizeClick
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