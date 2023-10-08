package br.com.market.storage.ui.screens.movement

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.R
import br.com.market.core.R.drawable.*
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.buttons.fab.SubActionFabItem
import br.com.market.enums.EnumOperationType
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import br.com.market.localdataaccess.tuples.StorageOperationHistoryTuple
import br.com.market.market.compose.components.MarketBottomAppBar
import br.com.market.market.compose.components.button.fab.MarketFloatingActionButtonMultiActions
import br.com.market.market.compose.components.button.fab.SmallFabActions
import br.com.market.market.compose.components.button.fab.rememberFabMultiActionsState
import br.com.market.market.compose.components.button.icons.IconButtonAdvancedFilters
import br.com.market.market.compose.components.button.icons.IconButtonAdvancedFiltersApply
import br.com.market.market.compose.components.button.icons.IconButtonReport
import br.com.market.market.compose.components.button.icons.IconButtonReports
import br.com.market.market.compose.components.filter.SimpleFilter
import br.com.market.market.compose.components.list.PagedVerticalListWithEmptyState
import br.com.market.market.compose.components.loading.MarketCircularBlockUIProgressIndicator
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import br.com.market.market.pdf.generator.enums.EnumReportDirectory
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
    onNavigateToReportList: (directory: String) -> Unit
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
        hasFilterApplied = viewModel.hasAdvancedFilterApplied(),
        onReportGenerateClick = viewModel::generateReport,
        onNavigateToReportList = onNavigateToReportList
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
    hasFilterApplied: Boolean = false,
    onReportGenerateClick: (onStart: () -> Unit, onFinish: () -> Unit) -> Unit = { _, _ -> },
    onNavigateToReportList: (directory: String) -> Unit = { }
) {
    val bottomBarState = rememberFabMultiActionsState()
    val pagingData = state.operations.collectAsLazyPagingItems()
    var showLoadingBlockUI by remember { mutableStateOf(false) }

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
            MarketBottomAppBar(
                actions = {
                    IconButtonReport {
                        onReportGenerateClick(
                            { showLoadingBlockUI = true },
                            {
                                showLoadingBlockUI = false
                                onNavigateToReportList(EnumReportDirectory.REPORT_DIRECTORY_STORAGE_OPERATIONS.path)
                            }
                        )
                    }

                    IconButtonReports {
                        onNavigateToReportList(EnumReportDirectory.REPORT_DIRECTORY_STORAGE_OPERATIONS.path)
                    }
                }
            ) {
                MarketFloatingActionButtonMultiActions(state = bottomBarState) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.label_adicionar)
                    )
                }
            }
        }
    ) { padding ->
        ConstraintLayout(
            Modifier.padding(padding)
        ) {
            val (fabActionRef, listRef, searchBarRef,
                searchDividerRef, headerRef, headerDivider) = createRefs()

            var searchActive by remember { mutableStateOf(false) }

            MarketCircularBlockUIProgressIndicator(
                show = showLoadingBlockUI,
                label = stringResource(string.label_generating_report)
            )

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
                PagedVerticalListWithEmptyState(pagingItems = pagingData) {
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

                if (!state.productName.isNullOrBlank()) {
                    Header(
                        modifier = Modifier
                            .constrainAs(headerRef) {
                                top.linkTo(searchDividerRef.bottom)
                                linkTo(start = parent.start, end = parent.end, bias = 0f)
                            },
                        state = state
                    )

                    Divider(
                        Modifier
                            .fillMaxWidth()
                            .constrainAs(headerDivider) {
                                linkTo(start = parent.start, end = parent.end, bias = 0F)
                                top.linkTo(headerRef.bottom)
                            }
                    )
                }

                PagedVerticalListWithEmptyState(
                    pagingItems = pagingData,
                    modifier = Modifier
                        .constrainAs(listRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)

                            val constraintTop = if (state.productName.isNullOrBlank()) searchDividerRef.top else headerRef.bottom
                            linkTo(top = constraintTop, bottom = parent.bottom, bias = 0F)
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
                        icon = painterResource(id = ic_calendar_24dp),
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
                        icon = painterResource(id = ic_input_storage_24dp),
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
                        icon = painterResource(id = ic_warning_24dp),
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

@Composable
private fun Header(
    modifier: Modifier,
    state: MovementsSearchUIState
) {
    ConstraintLayout(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val (productNameRef, quantity) = createRefs()

        Text(
            modifier = Modifier.constrainAs(productNameRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            },
            text = stringResource(string.movements_search_screen_label_quantity_header),
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            modifier = Modifier.constrainAs(quantity) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            },
            text = state.productQuantity.toString(),
            style = MaterialTheme.typography.titleSmall
        )
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