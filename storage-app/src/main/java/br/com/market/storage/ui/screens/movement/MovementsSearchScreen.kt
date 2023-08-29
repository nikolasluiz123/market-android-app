package br.com.market.storage.ui.screens.movement

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.IconButtonAdvancedFilters
import br.com.market.core.ui.components.buttons.IconButtonAdvancedFiltersApply
import br.com.market.core.ui.components.buttons.fab.MarketFloatingActionButtonMultiActions
import br.com.market.core.ui.components.buttons.fab.SmallFabActions
import br.com.market.core.ui.components.buttons.fab.SubActionFabItem
import br.com.market.core.ui.components.buttons.fab.rememberFabMultiActionsState
import br.com.market.core.ui.components.filter.SimpleFilter
import br.com.market.enums.EnumOperationType
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
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
    onAdvancedFiltersClick: (MovementSearchScreenFilters, (MovementSearchScreenFilters) -> Unit) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    MovementsSearchScreen(
        state = state,
        onAddMovementClick = onAddMovementClick,
        onBackClick = onBackClick,
        onMovementClick = onMovementClick,
        onSimpleFilterChange = {
            viewModel.updateList(simpleFilterText = it)
        },
        onAdvancedFiltersClick = {
            onAdvancedFiltersClick(viewModel.filter) {
                viewModel.updateList(advancedFilter = it)
            }
        },
        hasFilterApplied = viewModel.hasAdvancedFilterApplied()
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
    onAdvancedFiltersClick: () -> Unit = { },
    hasFilterApplied: Boolean = false
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
            val (fabActionRef, listRef, searchBarRef, searchDividerRef) = createRefs()

            var searchActive by remember { mutableStateOf(false) }

            SimpleFilter(
                modifier = Modifier
                    .constrainAs(searchBarRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth(),
                onSimpleFilterChange = onSimpleFilterChange,
                active = searchActive,
                onActiveChange = { searchActive = it },
                placeholderResId = string.movements_search_screen_buscar_por,
                trailingIcon = {
                    if (hasFilterApplied) {
                        IconButtonAdvancedFiltersApply(onAdvancedFiltersClick)
                    } else {
                        IconButtonAdvancedFilters(onAdvancedFiltersClick)
                    }
                }
            ) {
                PagedVerticalListComponent(pagingItems = pagingData) {
                    MovementListItem(
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
                    Divider(modifier = Modifier.fillMaxWidth())
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

                PagedVerticalListComponent(
                    pagingItems = pagingData,
                    modifier = Modifier
                        .constrainAs(listRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            linkTo(top = searchDividerRef.bottom, bottom = parent.bottom, bias = 0F)
                        }
                        .padding(bottom = 74.dp)
                ) {
                    MovementListItem(
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
                    Divider(modifier = Modifier.fillMaxWidth())
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