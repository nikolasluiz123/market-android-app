package br.com.market.storage.ui.screens.movement

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import br.com.market.core.filter.AdvancedFilterArgs
import br.com.market.core.filter.CommonAdvancedFilterItem
import br.com.market.core.filter.DateAdvancedFilterArgs
import br.com.market.core.filter.NumberAdvancedFilterArgs
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.LazyVerticalListComponent
import br.com.market.core.ui.components.filter.AdvancedFilterItem
import br.com.market.core.ui.components.filter.SelectOneAdvancedFilter
import br.com.market.core.ui.states.filter.AdvancedFilterUIState
import br.com.market.storage.enums.filter.EnumMovementsSearchScreenFilters.*
import br.com.market.storage.ui.viewmodels.movements.MovementSearchAdvancedFilterViewModel
import java.time.LocalDateTime

@Composable
fun MovementSearchAdvancedFilterScreen(
    viewModel: MovementSearchAdvancedFilterViewModel,
    onNavigateToTextFilter: (AdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToDateRangeFilter: (DateAdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToNumberFilter: (NumberAdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToUserLovFilter: ((Any) -> Unit) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    MovementSearchAdvancedFilterScreen(
        state = state,
        onSimpleFilterChange = {
            // fazer a pesquisa entre os filtros da lista
        },
        onNavigateToTextFilter = onNavigateToTextFilter,
        onNavigateToDateRangeFilter = onNavigateToDateRangeFilter,
        onNavigateToNumberFilter = onNavigateToNumberFilter,
        onNavigateToUserLovFilter = onNavigateToUserLovFilter
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementSearchAdvancedFilterScreen(
    state: AdvancedFilterUIState = AdvancedFilterUIState(),
    onSimpleFilterChange: (String) -> Unit = { },
    onNavigateToTextFilter: (AdvancedFilterArgs, (Any) -> Unit) -> Unit = { _, _ -> },
    onNavigateToDateRangeFilter: (DateAdvancedFilterArgs, (Any) -> Unit) -> Unit = { _, _ -> },
    onNavigateToNumberFilter: (NumberAdvancedFilterArgs, (Any) -> Unit) -> Unit = { _, _ -> },
    onNavigateToUserLovFilter: ((Any) -> Unit) -> Unit = { }
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
                MovementSearchAdvancedFilterItem(
                    item = item,
                    onNavigateToTextFilter = onNavigateToTextFilter,
                    onNavigateToDateRangeFilter = onNavigateToDateRangeFilter,
                    onNavigateToNumberFilter = onNavigateToNumberFilter,
                    onNavigateToUserLovFilter = onNavigateToUserLovFilter,
                    onOperationTypeClick = { callback ->
                        callbackSelectOne = callback
                        openSelectOn = true
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
                    .padding(top = 8.dp)
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
                MovementSearchAdvancedFilterItem(
                    item = item,
                    onNavigateToTextFilter = onNavigateToTextFilter,
                    onNavigateToDateRangeFilter = onNavigateToDateRangeFilter,
                    onNavigateToNumberFilter = onNavigateToNumberFilter,
                    onNavigateToUserLovFilter = onNavigateToUserLovFilter,
                    onOperationTypeClick = { callback ->
                        callbackSelectOne = callback
                        openSelectOn = true
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

@Composable
fun MovementSearchAdvancedFilterItem(
    item: CommonAdvancedFilterItem,
    onNavigateToTextFilter: (AdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToDateRangeFilter: (DateAdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToNumberFilter: (NumberAdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToUserLovFilter: ((Any) -> Unit) -> Unit,
    onOperationTypeClick: ((Any) -> Unit) -> Unit
) {
    AdvancedFilterItem(
        item = item,
        onItemClick = { callback ->
            when (item.identifier) {
                PRODUCT_NAME.name, DESCRIPTION.name -> {
                    onNavigateToTextFilter(
                        AdvancedFilterArgs(
                            titleResId = item.labelResId,
                            value = item.formatter.formatToString(item.value)
                        ),
                        callback
                    )
                }

                DATE_PREVISION.name, DATE_REALIZATION.name -> {
                    onNavigateToDateRangeFilter(
                        DateAdvancedFilterArgs(
                            titleResId = item.labelResId,
                            value = item.value as Pair<LocalDateTime?, LocalDateTime?>?
                        ),
                        callback
                    )
                }

                QUANTITY.name -> {
                    onNavigateToNumberFilter(
                        NumberAdvancedFilterArgs(
                            integer = true,
                            titleResId = item.labelResId,
                            value = item.formatter.formatToString(item.value)
                        ), callback
                    )
                }

                OPERATION_TYPE.name -> {
                    onOperationTypeClick(callback)
                }

                USER.name -> {
                    onNavigateToUserLovFilter(callback)
                }
            }
        }
    )
}

@Preview
@Composable
fun AdvancedFilterScreenPreview() {
    Surface {
        MarketTheme {
            MovementSearchAdvancedFilterScreen()
        }
    }
}