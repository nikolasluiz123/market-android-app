package br.com.market.storage.ui.viewmodels.movements

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.fromJsonNavParamToArgs
import br.com.market.core.extensions.searchWordsInText
import br.com.market.core.filter.CommonAdvancedFilterItem
import br.com.market.core.filter.formatter.DateTimeRangeAdvancedFilterFormatter
import br.com.market.core.filter.formatter.LovAdvancedFilterFormatter
import br.com.market.core.filter.formatter.NumberAdvancedFilterFormatter
import br.com.market.core.filter.formatter.SelectOneFormatter
import br.com.market.core.filter.formatter.StringAdvancedFilterFormatter
import br.com.market.core.gson.LocalDateTimeAdapter
import br.com.market.core.ui.states.filter.AdvancedFilterUIState
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import br.com.market.storage.R
import br.com.market.storage.filter.enums.EnumMovementsSearchScreenFilters
import br.com.market.storage.ui.navigation.movement.argumentMovementSearchAdvancedFilterJson
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MovementSearchAdvancedFilterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState: MutableStateFlow<AdvancedFilterUIState> = MutableStateFlow(AdvancedFilterUIState())

    val uiState get() = _uiState.asStateFlow()
    private val filterJson: String? = savedStateHandle[argumentMovementSearchAdvancedFilterJson]
    private val filters = mutableListOf<CommonAdvancedFilterItem>()

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        filterJson?.fromJsonNavParamToArgs(MovementSearchScreenFilters::class.java, gson)?.let { filter ->
            filters.add(
                CommonAdvancedFilterItem(
                    labelResId = R.string.movements_search_screen_label_filter_product_name,
                    formatter = StringAdvancedFilterFormatter(),
                    identifier = EnumMovementsSearchScreenFilters.PRODUCT_NAME.name,
                    value = filter.productName.value,
                    checked = filter.productName.checked
                )
            )
            filters.add(
                CommonAdvancedFilterItem(
                    labelResId = R.string.movements_search_screen_label_filter_description,
                    formatter = StringAdvancedFilterFormatter(),
                    identifier = EnumMovementsSearchScreenFilters.DESCRIPTION.name,
                    value = filter.description.value,
                    checked = filter.description.checked
                )
            )
            filters.add(
                CommonAdvancedFilterItem(
                    labelResId = R.string.movements_search_screen_label_filter_date_prevision,
                    formatter = DateTimeRangeAdvancedFilterFormatter(),
                    identifier = EnumMovementsSearchScreenFilters.DATE_PREVISION.name,
                    value = filter.datePrevision.value,
                    checked = filter.datePrevision.checked
                )
            )
            filters.add(
                CommonAdvancedFilterItem(
                    labelResId = R.string.movements_search_screen_label_filter_date_realization,
                    formatter = DateTimeRangeAdvancedFilterFormatter(),
                    identifier = EnumMovementsSearchScreenFilters.DATE_REALIZATION.name,
                    value = filter.dateRealization.value,
                    checked = filter.dateRealization.checked
                )
            )
            filters.add(
                CommonAdvancedFilterItem(
                    labelsReference = br.com.market.core.R.array.movement_screen_advanced_filter_operation_type_labels,
                    labelResId = R.string.movements_search_screen_label_filter_operation_type,
                    formatter = SelectOneFormatter(),
                    identifier = EnumMovementsSearchScreenFilters.OPERATION_TYPE.name,
                    value = filter.operationType.value,
                    checked = filter.operationType.checked
                )
            )
            filters.add(
                CommonAdvancedFilterItem(
                    labelResId = R.string.movements_search_screen_label_filter_quantity,
                    formatter = NumberAdvancedFilterFormatter(integer = true),
                    identifier = EnumMovementsSearchScreenFilters.QUANTITY.name,
                    value = filter.quantity.value,
                    checked = filter.quantity.checked
                )
            )
            filters.add(
                CommonAdvancedFilterItem(
                    labelResId = R.string.movements_search_screen_label_filter_user,
                    formatter = LovAdvancedFilterFormatter(),
                    identifier = EnumMovementsSearchScreenFilters.RESPONSIBLE.name,
                    value = filter.responsible.value,
                    checked = filter.responsible.checked
                )
            )

            _uiState.update { currentState ->
                currentState.copy(filters = filters)
            }
        }
    }

    fun onSearch(filter: String?) {
        _uiState.update {
            it.copy(filters = getFilteredFilters(filter))
        }
    }

    private fun getFilteredFilters(filterText: String?): List<CommonAdvancedFilterItem> {
        if (filterText.isNullOrEmpty()) {
            return filters
        }

        return filters.filter {
            context.resources.getString(it.labelResId).searchWordsInText(filterText)
        }
    }
}
