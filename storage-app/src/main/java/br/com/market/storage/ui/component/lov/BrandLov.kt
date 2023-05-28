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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.theme.GREY_600
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.storage.ui.screens.brand.BrandListCard
import br.com.market.storage.ui.screens.brand.BrandListCardSearch
import br.com.market.storage.ui.states.brand.BrandLovUIState
import br.com.market.storage.ui.viewmodels.brand.BrandLovViewModel
import java.util.*

@Composable
fun BrandLov(
    viewModel: BrandLovViewModel,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()

    BrandLov(
        state = state,
        onItemClick = onItemClick,
        onFilterChange = {
            viewModel.findBrands(it)
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandLov(
    state: BrandLovUIState = BrandLovUIState(),
    onItemClick: (String) -> Unit = { },
    onFilterChange: (String) -> Unit = { },
    onBackClick: () -> Unit = { }
) {
    val pagingData = state.brands.collectAsLazyPagingItems()
    Scaffold(
        topBar = {
            Column {
                SimpleMarketTopAppBar(
                    title = "Marcas",
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
                        inputFieldColors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                        dividerColor = GREY_600
                    ),
                    shape = SearchBarDefaults.fullScreenShape
                ) {
                    if (text.isNotEmpty()) {
                        PagedVerticalListComponent(pagingItems = pagingData) { brandDomain ->
                            BrandListCardSearch(
                                brandName = brandDomain.name,
                                active = brandDomain.active,
                                onItemClick = {
                                    onItemClick(brandDomain.id!!)
                                }
                            )
                        }
                    }
                }
                if (!active) {
                    Divider(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), color = GREY_600)
                }
            }
        }
    ) { padding ->
        ConstraintLayout(modifier = Modifier.padding(padding)) {
            PagedVerticalListComponent(pagingItems = pagingData) { brandDomain ->
                BrandListCard(
                    brandName = brandDomain.name,
                    active = brandDomain.active,
                    onItemClick = {
                        onItemClick(brandDomain.id!!)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun BrandLovPreview() {
    MarketTheme {
        Surface {
            BrandLov()
        }
    }
}