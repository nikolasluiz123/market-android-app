package br.com.market.storage.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.storage.R
import br.com.market.storage.sampledata.sampleProducts
import br.com.market.storage.ui.components.*
import br.com.market.storage.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.storage.ui.states.StorageProductsUiState
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel

@Composable
fun StorageProductsScreen(
    viewModel: StorageProductsViewModel,
    onItemClick: (Long) -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onFABNewProductClick: () -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()
    StorageProductsScreen(
        state = state,
        onItemClick = onItemClick,
        onLogoutClick = onLogoutClick,
        onFABNewProductClick = onFABNewProductClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageProductsScreen(
    state: StorageProductsUiState = StorageProductsUiState(),
    onItemClick: (Long) -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onFABNewProductClick: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            SearchableStorageTopAppBar(
                openSearch = state.openSearch,
                searchText = state.searchText,
                title = stringResource(R.string.storage_products_screen_app_bar_title),
                onSearchChange = state.onSearchChange,
                onToggleSearch = state.onToggleSearch,
                onLogoutClick = onLogoutClick
            )
        },
        floatingActionButton = { FloatingActionButtonAdd(onClick = onFABNewProductClick) }
    ) { paddingValues ->

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