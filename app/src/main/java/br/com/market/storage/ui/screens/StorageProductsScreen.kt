package br.com.market.storage.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.market.storage.sampledata.sampleProducts
import br.com.market.storage.ui.components.CardProductItem
import br.com.market.storage.ui.components.SearchTextField
import br.com.market.storage.ui.states.StorageProductsUiState
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel

@Composable
fun StorageProductsScreen(viewModel: StorageProductsViewModel) {
    val state by viewModel.uiState.collectAsState()
    StorageProductsScreen(state)
}

@Composable
fun StorageProductsScreen(state: StorageProductsUiState = StorageProductsUiState()) {
    StorageTheme {
        Surface {
            Column {
                SearchTextField(
                    searchText = state.searchText,
                    onSearchChange = state.onSearchChange,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )

                if (state.isSearching()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(items = state.products) { product ->
                            CardProductItem(
                                product = product
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(items = state.products) { product ->
                            CardProductItem(
                                product = product
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun StorageProductsLightPreview() {
    StorageProductsScreen(state = StorageProductsUiState(products = sampleProducts))
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StorageProductsDarkPreview() {
    StorageProductsScreen(state = StorageProductsUiState(products = sampleProducts))
}

@Preview(showSystemUi = true)
@Composable
fun StorageProductsSearchingLightPreview() {
    StorageProductsScreen(state = StorageProductsUiState(products = sampleProducts, searchText = "Arroz"))
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StorageProductsSearchingDarkPreview() {
    StorageProductsScreen(state = StorageProductsUiState(products = sampleProducts, searchText = "Arroz"))
}