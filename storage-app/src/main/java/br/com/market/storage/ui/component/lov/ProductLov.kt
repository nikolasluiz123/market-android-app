package br.com.market.storage.ui.component.lov

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.localdataaccess.tuples.ProductImageTuple
import br.com.market.storage.R
import br.com.market.storage.ui.screens.brand.ProductListItem
import br.com.market.storage.ui.states.product.ProductLovUIState
import br.com.market.storage.ui.viewmodels.product.ProductLovViewModel
import java.util.*

@Composable
fun ProductLov(
    viewModel: ProductLovViewModel,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()

    ProductLov(
        state = state,
        onItemClick = onItemClick,
        onFilterChange = {

        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductLov(
    state: ProductLovUIState = ProductLovUIState(),
    onItemClick: (String) -> Unit = { },
    onFilterChange: (String) -> Unit = { },
    onBackClick: () -> Unit = { }
) {
    val pagingData = state.products.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            Column {
                SimpleMarketTopAppBar(
                    title = stringResource(R.string.lov_products_title),
                    showMenuWithLogout = false,
                    onBackClick = onBackClick
                )

                var text by rememberSaveable { mutableStateOf("") }
                var active by rememberSaveable { mutableStateOf(false) }

                SearchBar(
                    query = text,
                    onQueryChange = {
                        text = it
                        onFilterChange(text)
                    },
                    onSearch = {
                        active = false
                    },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text(text = "Buscar por Nome") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SearchBarDefaults.colors(
                        containerColor = Color.Transparent,
                        inputFieldColors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        ),
                        dividerColor = DividerDefaults.color
                    ),
                    shape = SearchBarDefaults.fullScreenShape
                ) {
                    ProductList(pagingData, onItemClick)
                }
                if (!active) {
                    Divider(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                }
            }
        }
    ) { padding ->
        ConstraintLayout(modifier = Modifier.padding(padding)) {
            ProductList(pagingData, onItemClick)
        }
    }
}

@Composable
private fun ProductList(
    pagingData: LazyPagingItems<ProductImageTuple>,
    onItemClick: (String) -> Unit
) {
    PagedVerticalListComponent(pagingItems = pagingData) { productTuple ->
        ProductListItem(
            name = productTuple.productName,
            price = productTuple.productPrice,
            quantity = productTuple.productQuantity,
            quantityUnit = productTuple.productQuantityUnit,
            image = productTuple.imageBytes!!,
            active = productTuple.productActive,
            onItemClick = {
                onItemClick(productTuple.productId)
            }
        )
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
fun ProductLovPreview() {
    MarketTheme {
        Surface {
            ProductLov()
        }
    }
}