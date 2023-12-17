package br.com.market.storage.ui.screens.movement

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.inputs.CommonAdvancedFilterItem
import br.com.market.core.inputs.arguments.DateTimeRangeInputArgs
import br.com.market.core.inputs.arguments.InputArgs
import br.com.market.core.inputs.arguments.InputNumberArgs
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.states.filter.AdvancedFilterUIState
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import br.com.market.market.compose.components.SelectOneOption
import br.com.market.market.compose.components.filter.AdvancedFilterItem
import br.com.market.market.compose.components.filter.SimpleFilter
import br.com.market.market.compose.components.list.LazyVerticalListWithEmptyState
import br.com.market.storage.filter.enums.EnumMovementsSearchScreenFilters.DATE_PREVISION
import br.com.market.storage.filter.enums.EnumMovementsSearchScreenFilters.DATE_REALIZATION
import br.com.market.storage.filter.enums.EnumMovementsSearchScreenFilters.DESCRIPTION
import br.com.market.storage.filter.enums.EnumMovementsSearchScreenFilters.OPERATION_TYPE
import br.com.market.storage.filter.enums.EnumMovementsSearchScreenFilters.PRODUCT_NAME
import br.com.market.storage.filter.enums.EnumMovementsSearchScreenFilters.QUANTITY
import br.com.market.storage.filter.enums.EnumMovementsSearchScreenFilters.RESPONSIBLE
import br.com.market.storage.ui.viewmodels.movements.MovementSearchAdvancedFilterViewModel
import java.time.LocalDateTime

@Composable
fun MovementSearchAdvancedFilterScreen(
    viewModel: MovementSearchAdvancedFilterViewModel,
    onNavigateToTextFilter: (InputArgs, (Any) -> Unit) -> Unit,
    onNavigateToDateRangeFilter: (DateTimeRangeInputArgs, (Any) -> Unit) -> Unit,
    onNavigateToNumberFilter: (InputNumberArgs, (Number?) -> Unit) -> Unit,
    onNavigateToUserLovFilter: ((Any) -> Unit) -> Unit,
    onApplyFilters: (MovementSearchScreenFilters) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    MovementSearchAdvancedFilterScreen(
        state = state,
        onSimpleFilterChange = viewModel::onSearch,
        onNavigateToTextFilter = onNavigateToTextFilter,
        onNavigateToDateRangeFilter = onNavigateToDateRangeFilter,
        onNavigateToNumberFilter = onNavigateToNumberFilter,
        onNavigateToUserLovFilter = onNavigateToUserLovFilter,
        onApplyFilters = onApplyFilters
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
fun MovementSearchAdvancedFilterScreen(
    state: AdvancedFilterUIState = AdvancedFilterUIState(),
    onSimpleFilterChange: (String) -> Unit = { },
    onNavigateToTextFilter: (InputArgs, (Any) -> Unit) -> Unit = { _, _ -> },
    onNavigateToDateRangeFilter: (DateTimeRangeInputArgs, (Any) -> Unit) -> Unit = { _, _ -> },
    onNavigateToNumberFilter: (InputNumberArgs, (Number?) -> Unit) -> Unit = { _, _ -> },
    onNavigateToUserLovFilter: ((Any) -> Unit) -> Unit = { },
    onApplyFilters: (MovementSearchScreenFilters) -> Unit = { },
) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val (listRef, searchBarRef, searchDividerRef, buttonApplyRef, buttonClearRef) = createRefs()
        var searchActive by remember { mutableStateOf(false) }
        var openSelectOneOptionFilter by remember { mutableStateOf(false) }
        var callbackSelectOne: ((Any) -> Unit)? = null

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
            placeholderResId = R.string.advanced_filter_screen_search_for,
        ) {
            LazyVerticalListWithEmptyState(items = state.filters) { item ->
                MovementSearchAdvancedFilterItem(
                    item = item as CommonAdvancedFilterItem<Any?>,
                    onNavigateToTextFilter = onNavigateToTextFilter,
                    onNavigateToDateRangeFilter = onNavigateToDateRangeFilter,
                    onNavigateToNumberFilter = onNavigateToNumberFilter,
                    onNavigateToUserLovFilter = onNavigateToUserLovFilter,
                    onOperationTypeClick = { callback ->
                        callbackSelectOne = callback
                        openSelectOneOptionFilter = true
                    }
                )
                Divider(Modifier.fillMaxWidth())

                if (openSelectOneOptionFilter) {
                    OpenSelectOneOptionFilter(
                        item = item as CommonAdvancedFilterItem<Pair<String, Int>?>,
                        onDismiss = { openSelectOneOptionFilter = false },
                        onItemClick = {
                            callbackSelectOne!!.invoke(it)
                            openSelectOneOptionFilter = false
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

            LazyVerticalListWithEmptyState(
                modifier = Modifier.constrainAs(listRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0F)
                    linkTo(top = searchDividerRef.bottom, bottom = buttonApplyRef.top, bottomMargin = 8.dp, bias = 0f)
                },
                items = state.filters
            ) { item ->
                MovementSearchAdvancedFilterItem(
                    item = item as CommonAdvancedFilterItem<Any?>,
                    onNavigateToTextFilter = onNavigateToTextFilter,
                    onNavigateToDateRangeFilter = onNavigateToDateRangeFilter,
                    onNavigateToNumberFilter = onNavigateToNumberFilter,
                    onNavigateToUserLovFilter = onNavigateToUserLovFilter,
                    onOperationTypeClick = { callback ->
                        callbackSelectOne = callback
                        openSelectOneOptionFilter = true
                    }
                )
                Divider(Modifier.fillMaxWidth())

                if (openSelectOneOptionFilter) {
                    OpenSelectOneOptionFilter(
                        item = item as CommonAdvancedFilterItem<Pair<String, Int>?>,
                        onDismiss = { openSelectOneOptionFilter = false },
                        onItemClick = {
                            callbackSelectOne!!.invoke(it)
                            openSelectOneOptionFilter = false
                        }
                    )
                }
            }

            OutlinedButton(
                modifier = Modifier.constrainAs(buttonApplyRef) {
                    end.linkTo(parent.end, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                },
                onClick = {
                    val filter = MovementSearchScreenFilters()

                    state.filters.map { item ->
                        when (item.identifier) {
                            PRODUCT_NAME.name -> {
                                item as CommonAdvancedFilterItem<String?>
                                filter.productName = item.toFilterValue()
                            }
                            DESCRIPTION.name -> {
                                item as CommonAdvancedFilterItem<String?>
                                filter.description = item.toFilterValue()
                            }
                            DATE_PREVISION.name -> {
                                item as CommonAdvancedFilterItem<Pair<LocalDateTime?, LocalDateTime?>?>
                                filter.datePrevision = item.toFilterValue()
                            }
                            DATE_REALIZATION.name -> {
                                item as CommonAdvancedFilterItem<Pair<LocalDateTime?, LocalDateTime?>?>
                                filter.dateRealization = item.toFilterValue()
                            }
                            OPERATION_TYPE.name -> {
                                item as CommonAdvancedFilterItem<Pair<String, Int>?>
                                filter.operationType = item.toFilterValue()
                            }
                            QUANTITY.name -> {
                                item as CommonAdvancedFilterItem<Long?>
                                filter.quantity = item.toFilterValue()
                            }
                            RESPONSIBLE.name -> {
                                item as CommonAdvancedFilterItem<Pair<String, String?>?>
                                filter.responsible = item.toFilterValue()
                            }
                        }
                    }

                    onApplyFilters(filter)
                },
                border = null
            ) {
                Text(stringResource(R.string.label_apply_advanced_filter), color = Color.White)
            }

            OutlinedButton(
                modifier = Modifier.constrainAs(buttonClearRef) {
                    end.linkTo(buttonApplyRef.start, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                },
                onClick = {
                    onApplyFilters(MovementSearchScreenFilters())
                },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
            ) {
                Text(stringResource(R.string.label_clear))
            }
        }
    }
}

@Composable
private fun OpenSelectOneOptionFilter(
    item: CommonAdvancedFilterItem<Pair<String, Int>?>,
    onDismiss: () -> Unit,
    onItemClick: (Pair<String, Int>) -> Unit
) {
    item.labelsReference?.let { labelsId ->
        val options = mutableListOf<Pair<String, Int>>()
        stringArrayResource(id = labelsId).forEachIndexed { index, label ->
            options.add(label to index)
        }

        SelectOneOption(
            items = options,
            onDismiss = onDismiss,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun <T> MovementSearchAdvancedFilterItem(
    item: CommonAdvancedFilterItem<T?>,
    onNavigateToTextFilter: (InputArgs, (T) -> Unit) -> Unit,
    onNavigateToDateRangeFilter: (DateTimeRangeInputArgs, (T) -> Unit) -> Unit,
    onNavigateToNumberFilter: (InputNumberArgs, (T?) -> Unit) -> Unit,
    onNavigateToUserLovFilter: ((T) -> Unit) -> Unit,
    onOperationTypeClick: ((T) -> Unit) -> Unit
) {
    AdvancedFilterItem<T?>(
        item = item,
        onItemClick = { callback ->
            when (item.identifier) {
                PRODUCT_NAME.name, DESCRIPTION.name -> {
                    onNavigateToTextFilter(
                        InputArgs(
                            titleResId = item.labelResId,
                            value = item.formatter.formatToString(item.value)
                        ),
                        callback
                    )
                }

                DATE_PREVISION.name, DATE_REALIZATION.name -> {
                    @Suppress("UNCHECKED_CAST")
                    onNavigateToDateRangeFilter(
                        DateTimeRangeInputArgs(
                            titleResId = item.labelResId,
                            value = item.value as Pair<LocalDateTime?, LocalDateTime?>?
                        ),
                        callback
                    )
                }

                QUANTITY.name -> {
                    onNavigateToNumberFilter(
                        InputNumberArgs(
                            integer = true,
                            titleResId = item.labelResId,
                            value = item.formatter.formatToString(item.value) ?: ""
                        ), callback
                    )
                }

                OPERATION_TYPE.name -> {
                    onOperationTypeClick(callback)
                }

                RESPONSIBLE.name -> {
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