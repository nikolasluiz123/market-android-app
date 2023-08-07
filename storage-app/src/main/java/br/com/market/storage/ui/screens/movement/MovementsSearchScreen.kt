package br.com.market.storage.ui.screens.movement

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.R
import br.com.market.core.filter.CommonAdvancedFilterItem
import br.com.market.core.filter.EnumAdvancedFilterType
import br.com.market.core.filter.formatter.DateTimeRangeAdvancedFilterFormatter
import br.com.market.core.filter.formatter.StringAdvancedFilterFormatter
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.IconButtonAdvancedFilters
import br.com.market.core.ui.components.buttons.MarketFloatingActionButtonMultiActions
import br.com.market.core.ui.components.buttons.SmallFabActions
import br.com.market.core.ui.components.buttons.fab.SubActionFabItem
import br.com.market.core.ui.components.buttons.rememberFabMultiActionsState
import br.com.market.enums.EnumOperationType
import br.com.market.localdataaccess.tuples.StorageOperationHistoryTuple
import br.com.market.storage.R.*
import br.com.market.storage.ui.states.MovementsSearchUIState
import br.com.market.storage.ui.viewmodels.movements.MovementsSearchViewModel

@Composable
fun MovementsSearchScreen(
    viewModel: MovementsSearchViewModel,
    onAddMovementClick: (String, String, EnumOperationType, String?) -> Unit,
    onBackClick: () -> Unit,
    onMovementClick: (StorageOperationHistoryTuple) -> Unit,
    onAdvancedFiltersClick: (List<CommonAdvancedFilterItem>) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    MovementsSearchScreen(
        state = state,
        onAddMovementClick = onAddMovementClick,
        onBackClick = onBackClick,
        onMovementClick = onMovementClick,
        onSimpleFilterChange = viewModel::updateList,
        onAdvancedFiltersClick = onAdvancedFiltersClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsSearchScreen(
    state: MovementsSearchUIState = MovementsSearchUIState(),
    onAddMovementClick: (String, String, EnumOperationType, String?) -> Unit = { _, _, _, _ -> },
    onBackClick: () -> Unit = { },
    onMovementClick: (StorageOperationHistoryTuple) -> Unit = { },
    onSimpleFilterChange: (String) -> Unit = { },
    onAdvancedFiltersClick: (List<CommonAdvancedFilterItem>) -> Unit = { }
) {
    val bottomBarState = rememberFabMultiActionsState()
    val pagingData = state.operations.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(string.movements_search_screen_title),
                subtitle = state.productName ?: state.brandName,
                showMenuWithLogout = false,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            MarketBottomAppBar {
                MarketFloatingActionButtonMultiActions(state = bottomBarState) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.label_adicionar),
                        tint = Color.White
                    )
                }
            }
        }
    ) { padding ->
        ConstraintLayout(
            Modifier.padding(padding)
        ) {
            val (fabActionRef, listRef, searchBarRef) = createRefs()

            var text by rememberSaveable { mutableStateOf("") }
            var searchActive by remember { mutableStateOf(false) }

            SearchBar(
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
                        text = stringResource(string.movements_search_screen_buscar_por),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .constrainAs(searchBarRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth(),
                colors = SearchBarDefaults.colors(
                    containerColor = Color.Transparent,
                    inputFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    dividerColor = Color.Transparent
                ),
                shape = SearchBarDefaults.fullScreenShape,
                trailingIcon = {
                    IconButtonAdvancedFilters {
                        val filters = listOf(
                            CommonAdvancedFilterItem(
                                labelResId = string.movements_search_screen_label_filter_product_name,
                                filterType = EnumAdvancedFilterType.TEXT,
                                formatter = StringAdvancedFilterFormatter()
                            ),
                            CommonAdvancedFilterItem(
                                labelResId = string.movements_search_screen_label_filter_description,
                                filterType = EnumAdvancedFilterType.TEXT,
                                formatter = StringAdvancedFilterFormatter()
                            ),
                            CommonAdvancedFilterItem(
                                labelResId = string.movements_search_screen_label_filter_date_prevision,
                                filterType = EnumAdvancedFilterType.DATE,
                                formatter = DateTimeRangeAdvancedFilterFormatter()
                            ),
                            CommonAdvancedFilterItem(
                                labelResId = string.movements_search_screen_label_filter_date_realization,
                                filterType = EnumAdvancedFilterType.DATE,
                                formatter = DateTimeRangeAdvancedFilterFormatter()
                            ),
                            CommonAdvancedFilterItem(
                                labelResId = string.movements_search_screen_label_filter_operation_type,
                                filterType = EnumAdvancedFilterType.SELECT_ONE,
                                formatter = StringAdvancedFilterFormatter()
                            ),
                            CommonAdvancedFilterItem(
                                labelResId = string.movements_search_screen_label_filter_quantity,
                                filterType = EnumAdvancedFilterType.NUMBER,
                                formatter = StringAdvancedFilterFormatter()
                            ),
                            CommonAdvancedFilterItem(
                                labelResId = string.movements_search_screen_label_filter_user,
                                filterType = EnumAdvancedFilterType.LOV,
                                formatter = StringAdvancedFilterFormatter()
                            )
                        )

                        onAdvancedFiltersClick(filters)
                    }
                }
            ) {
                PagedVerticalListComponent(pagingItems = pagingData) {
                    StorageListCard(
                        productName = it.productName,
                        operationType = it.operationType,
                        datePrevision = it.datePrevision,
                        dateRealization = it.dateRealization,
                        quantity = it.quantity,
                        responsibleName = it.responsibleName,
                        description = it.description,
                        onItemClick = {
                            onMovementClick(it)
                        }
                    )
                }
            }

            if (!searchActive) {
                PagedVerticalListComponent(
                    pagingItems = pagingData,
                    modifier = Modifier.constrainAs(listRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        top.linkTo(searchBarRef.bottom)
                    }
                ) {
                    StorageListCard(
                        productName = it.productName,
                        operationType = it.operationType,
                        datePrevision = it.datePrevision,
                        dateRealization = it.dateRealization,
                        quantity = it.quantity,
                        responsibleName = it.responsibleName,
                        description = it.description,
                        onItemClick = {
                            onMovementClick(it)
                        }
                    )
                }
            }

            SmallFabActions(
                modifier = Modifier.constrainAs(fabActionRef) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                state = bottomBarState,
                subActionsFab = listOf(
                    SubActionFabItem(
                        icon = painterResource(id = R.drawable.ic_calendar_24dp),
                        label = "Agendar Entrada",
                        onFabItemClicked = {
                            onAddMovementClick(
                                state.categoryId!!,
                                state.brandId!!,
                                EnumOperationType.ScheduledInput,
                                state.productId
                            )
                        }
                    ),
                    SubActionFabItem(
                        icon = painterResource(id = R.drawable.ic_input_storage_24dp),
                        label = "Entrada",
                        onFabItemClicked = {
                            onAddMovementClick(
                                state.categoryId!!,
                                state.brandId!!,
                                EnumOperationType.Input,
                                state.productId
                            )
                        }
                    ),
                    SubActionFabItem(
                        icon = painterResource(id = R.drawable.ic_warning_24dp),
                        label = "Baixa",
                        onFabItemClicked = {
                            onAddMovementClick(
                                state.categoryId!!,
                                state.brandId!!,
                                EnumOperationType.Output,
                                state.productId
                            )
                        }
                    )
                )
            )
        }
    }
}

@Preview
@Composable
fun MovementsScreenSearchPreview() {
    MarketTheme {
        Surface {
            MovementsSearchScreen()
        }
    }
}