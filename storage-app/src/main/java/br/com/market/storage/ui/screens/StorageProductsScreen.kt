package br.com.market.storage.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.storage.sampledata.sampleProducts
import br.com.market.storage.ui.components.AppBarTextField
import br.com.market.storage.ui.components.CardProductItem
import br.com.market.storage.ui.states.StorageProductsUiState
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.theme.secondary
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel

@Composable
fun StorageProductsScreen(
    viewModel: StorageProductsViewModel,
    onItemClick: () -> Unit = { },
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
    onItemClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onFABNewProductClick: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (state.openSearch) {
                        AppBarTextField(
                            value = state.searchText,
                            onValueChange = state.onSearchChange,
                            hint = "O que você procura?"
                        )
                    } else {
                        Text(text = "Produtos em Estoque", style = MaterialTheme.typography.titleMedium)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = secondary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    if (state.openSearch) {
                        IconButton(onClick = state.onToggleSearch) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                    }
                },
                actions = {
                    if (!state.openSearch) {
                        IconButton(onClick = state.onToggleSearch) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Pesquisar"
                            )
                        }

                        IconButton(onClick = onLogoutClick) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout"
                            )
                        }
                    } else {
                        IconButton(onClick = { state.onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpar Pesquisa"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(100),
                onClick = onFABNewProductClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }

        }) { paddingValues ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val (searchRef, lazyColumnRef, lazyGridRef) = createRefs()

            if (state.isSearching()) {
                LazyColumn(
                    modifier = Modifier
                        .constrainAs(lazyColumnRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(items = state.products) { product ->
                        CardProductItem(
                            product = product,
                            onClick = onItemClick
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .constrainAs(lazyGridRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(items = state.products) { product ->
                        CardProductItem(
                            product = product,
                            onClick = onItemClick
                        )
                    }
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun StorageProductsLightPreview() {
    StorageTheme {
        Surface {
            StorageProductsScreen(state = StorageProductsUiState(products = sampleProducts))
        }
    }
}