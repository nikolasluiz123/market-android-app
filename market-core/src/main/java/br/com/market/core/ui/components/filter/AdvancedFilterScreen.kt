package br.com.market.core.ui.components.filter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.filter.CommonAdvancedFilterItem
import br.com.market.core.filter.EnumAdvancedFilterType
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.LazyVerticalListComponent
import br.com.market.core.ui.states.filter.AdvancedFilterUIState
import br.com.market.core.ui.viewmodel.filter.AdvancedFilterViewModel

@Composable
fun AdvancedFilterScreen(
    viewModel: AdvancedFilterViewModel,
    onItemClick: (CommonAdvancedFilterItem, (Any) -> Unit) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    AdvancedFilterScreen(
        state = state,
        onSimpleFilterChange = {
            // fazer a pesquisa entre os filtros da lista
        },
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedFilterScreen(
    state: AdvancedFilterUIState = AdvancedFilterUIState(),
    onSimpleFilterChange: (String) -> Unit = { },
    onItemClick: (CommonAdvancedFilterItem, (Any) -> Unit) -> Unit = { _, _ -> }
) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val (listRef, searchBarRef, searchDividerRef) = createRefs()
        var text by rememberSaveable { mutableStateOf("") }
        var searchActive by remember { mutableStateOf(false) }
        var openSelectOn by remember { mutableStateOf(false) }
        var callbackSelectOne: ((Any) -> Unit)? = null

        SearchBar(
            modifier = Modifier
                .constrainAs(searchBarRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0F)
                    top.linkTo(parent.top)
                }
                .fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
                onSimpleFilterChange(text)
            },
            onSearch = {
                onSimpleFilterChange(text)
            },
            active = searchActive,
            onActiveChange = { searchActive = it },
            placeholder = {
                Text(
                    text = stringResource(R.string.advanced_filter_screen_search_for),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
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
            LazyVerticalListComponent(
                items = state.filters,
                verticalArrangementSpace = 0.dp,
                contentPadding = 0.dp
            ) { item ->
                AdvancedFilterItem(
                    item = item,
                    onItemClick = { callback ->
                        when (item.filterType) {
                            EnumAdvancedFilterType.SELECT_ONE -> {
                                openSelectOn = true
                                callbackSelectOne = callback
                            }
                            else -> onItemClick(item, callback)
                        }

                    }
                )
                Divider(Modifier.fillMaxWidth())

                if (openSelectOn) {
                    OpenSelectOneOption(
                        item,
                        onDismiss = { openSelectOn = false },
                        onItemClick = {
                            callbackSelectOne!!.invoke(it)
                            openSelectOn = false
                        }
                    )
                }
            }
        }

        if (!searchActive) {
            Divider(
                Modifier
                    .fillMaxWidth()
                    .constrainAs(searchDividerRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        top.linkTo(searchBarRef.bottom)
                    }
            )

            LazyVerticalListComponent(
                modifier = Modifier.constrainAs(listRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0F)
                    top.linkTo(searchBarRef.bottom)
                },
                items = state.filters,
                verticalArrangementSpace = 0.dp,
                contentPadding = 0.dp
            ) { item ->
                AdvancedFilterItem(
                    item = item,
                    onItemClick = { callback ->
                        when (item.filterType) {
                            EnumAdvancedFilterType.SELECT_ONE -> {
                                openSelectOn = true
                                callbackSelectOne = callback
                            }
                            else -> onItemClick(item, callback)
                        }
                    }
                )
                Divider(Modifier.fillMaxWidth())

                if (openSelectOn) {
                    OpenSelectOneOption(
                        item = item,
                        onDismiss = { openSelectOn = false },
                        onItemClick = {
                            callbackSelectOne!!.invoke(it)
                            openSelectOn = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OpenSelectOneOption(
    item: CommonAdvancedFilterItem,
    onDismiss: () -> Unit,
    onItemClick: (Pair<String, Int>) -> Unit
) {
    item.labelsReference?.let { labelsId ->
        val options = mutableListOf<Pair<String, Int>>()
        stringArrayResource(id = labelsId).forEachIndexed { index, label ->
            options.add(label to index)
        }

        SelectOneAdvancedFilter(
            items = options,
            onDismiss = onDismiss,
            onItemClick = onItemClick
        )
    }
}

@Preview
@Composable
fun AdvancedFilterScreenPreview() {
    Surface {
        MarketTheme {
            AdvancedFilterScreen()
        }
    }
}