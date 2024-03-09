package br.com.market.market.compose.components.lov

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
import br.com.market.domain.CategoryDomain
import br.com.market.market.compose.components.R
import br.com.market.market.compose.components.list.PagedVerticalListWithEmptyState
import br.com.market.market.compose.components.lov.items.CategoryLovListItem
import br.com.market.market.compose.components.lov.state.CategoryLovUIState
import br.com.market.market.compose.components.lov.viewmodel.CategoryLovViewModel
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import java.util.*

@Composable
fun CategoryLov(
    viewModel: CategoryLovViewModel,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()

    CategoryLov(
        state = state,
        onItemClick = onItemClick,
        onFilterChange = viewModel::onSimpleFilterChange,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryLov(
    state: CategoryLovUIState = CategoryLovUIState(),
    onItemClick: (String) -> Unit = { },
    onFilterChange: (String) -> Unit = { },
    onBackClick: () -> Unit = { }
) {
    val pagingData = state.brands.collectAsLazyPagingItems()
    Scaffold(
        topBar = {
            Column {
                SimpleMarketTopAppBar(
                    title = stringResource(R.string.category_lov_label_title),
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
                    placeholder = {
                        Text(
                            text = stringResource(R.string.category_lov_placeholder),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
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
                    BrandList(pagingData, onItemClick)
                }
                if (!active) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    ) { padding ->
        ConstraintLayout(modifier = Modifier.padding(padding)) {
            BrandList(pagingData, onItemClick)
        }
    }
}

@Composable
private fun BrandList(
    pagingData: LazyPagingItems<CategoryDomain>,
    onItemClick: (String) -> Unit
) {
    PagedVerticalListWithEmptyState(pagingItems = pagingData) { categoryDomain ->
        CategoryLovListItem(
            name = categoryDomain.name,
            onItemClick = {
                onItemClick(categoryDomain.id!!)
            }
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
fun CategoryLovLovPreview() {
    MarketTheme {
        Surface {
            CategoryLov()
        }
    }
}