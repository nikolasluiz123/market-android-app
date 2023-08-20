package br.com.market.storage.ui.viewmodels.movements

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.filter.CommonAdvancedFilterItem
import br.com.market.core.filter.formatter.DateTimeRangeAdvancedFilterFormatter
import br.com.market.core.filter.formatter.LovAdvancedFilterFormatter
import br.com.market.core.filter.formatter.NumberAdvancedFilterFormatter
import br.com.market.core.filter.formatter.SelectOneFormatter
import br.com.market.core.filter.formatter.StringAdvancedFilterFormatter
import br.com.market.core.ui.states.filter.AdvancedFilterUIState
import br.com.market.storage.R
import br.com.market.storage.enums.filter.EnumMovementsSearchScreenFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovementSearchAdvancedFilterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AdvancedFilterUIState> = MutableStateFlow(AdvancedFilterUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                filters = listOf(
                    CommonAdvancedFilterItem(
                        labelResId = R.string.movements_search_screen_label_filter_product_name,
                        formatter = StringAdvancedFilterFormatter(),
                        identifier = EnumMovementsSearchScreenFilters.PRODUCT_NAME.name
                    ),
                    CommonAdvancedFilterItem(
                        labelResId = R.string.movements_search_screen_label_filter_description,
                        formatter = StringAdvancedFilterFormatter(),
                        identifier = EnumMovementsSearchScreenFilters.DESCRIPTION.name
                    ),
                    CommonAdvancedFilterItem(
                        labelResId = R.string.movements_search_screen_label_filter_date_prevision,
                        formatter = DateTimeRangeAdvancedFilterFormatter(),
                        identifier = EnumMovementsSearchScreenFilters.DATE_PREVISION.name
                    ),
                    CommonAdvancedFilterItem(
                        labelResId = R.string.movements_search_screen_label_filter_date_realization,
                        formatter = DateTimeRangeAdvancedFilterFormatter(),
                        identifier = EnumMovementsSearchScreenFilters.DATE_REALIZATION.name
                    ),
                    CommonAdvancedFilterItem(
                        labelsReference = br.com.market.core.R.array.movement_screen_advanced_filter_operation_type_labels,
                        labelResId = R.string.movements_search_screen_label_filter_operation_type,
                        formatter = SelectOneFormatter(),
                        identifier = EnumMovementsSearchScreenFilters.OPERATION_TYPE.name
                    ),
                    CommonAdvancedFilterItem(
                        labelResId = R.string.movements_search_screen_label_filter_quantity,
                        formatter = NumberAdvancedFilterFormatter(integer = true),
                        identifier = EnumMovementsSearchScreenFilters.QUANTITY.name
                    ),
                    CommonAdvancedFilterItem(
                        labelResId = R.string.movements_search_screen_label_filter_user,
                        formatter = LovAdvancedFilterFormatter(),
                        identifier = EnumMovementsSearchScreenFilters.USER.name
                    )
                )
            )
        }
    }

}
