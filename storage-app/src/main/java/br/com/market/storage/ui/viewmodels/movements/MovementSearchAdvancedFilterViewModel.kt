package br.com.market.storage.ui.viewmodels.movements

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.fromJsonNavParamToArgs
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MovementSearchAdvancedFilterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AdvancedFilterUIState> = MutableStateFlow(AdvancedFilterUIState())
    val uiState get() = _uiState.asStateFlow()

    private val filterJson: String? = savedStateHandle[argumentMovementSearchAdvancedFilterJson]

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        filterJson?.fromJsonNavParamToArgs(MovementSearchScreenFilters::class.java, gson)?.let { filter ->
            _uiState.update { currentState ->
                currentState.copy(
                    filters = listOf(
                        CommonAdvancedFilterItem(
                            labelResId = R.string.movements_search_screen_label_filter_product_name,
                            formatter = StringAdvancedFilterFormatter(),
                            identifier = EnumMovementsSearchScreenFilters.PRODUCT_NAME.name,
                            value = filter.productName
                        ),
                        CommonAdvancedFilterItem(
                            labelResId = R.string.movements_search_screen_label_filter_description,
                            formatter = StringAdvancedFilterFormatter(),
                            identifier = EnumMovementsSearchScreenFilters.DESCRIPTION.name,
                            value = filter.description
                        ),
                        CommonAdvancedFilterItem(
                            labelResId = R.string.movements_search_screen_label_filter_date_prevision,
                            formatter = DateTimeRangeAdvancedFilterFormatter(),
                            identifier = EnumMovementsSearchScreenFilters.DATE_PREVISION.name,
                            value = filter.datePrevision
                        ),
                        CommonAdvancedFilterItem(
                            labelResId = R.string.movements_search_screen_label_filter_date_realization,
                            formatter = DateTimeRangeAdvancedFilterFormatter(),
                            identifier = EnumMovementsSearchScreenFilters.DATE_REALIZATION.name,
                            value = filter.dateRealization
                        ),
                        CommonAdvancedFilterItem(
                            labelsReference = br.com.market.core.R.array.movement_screen_advanced_filter_operation_type_labels,
                            labelResId = R.string.movements_search_screen_label_filter_operation_type,
                            formatter = SelectOneFormatter(),
                            identifier = EnumMovementsSearchScreenFilters.OPERATION_TYPE.name,
                            value = filter.operationType
                        ),
                        CommonAdvancedFilterItem(
                            labelResId = R.string.movements_search_screen_label_filter_quantity,
                            formatter = NumberAdvancedFilterFormatter(integer = true),
                            identifier = EnumMovementsSearchScreenFilters.QUANTITY.name,
                            value = filter.quantity
                        ),
                        CommonAdvancedFilterItem(
                            labelResId = R.string.movements_search_screen_label_filter_user,
                            formatter = LovAdvancedFilterFormatter(),
                            identifier = EnumMovementsSearchScreenFilters.RESPONSIBLE.name,
                            value = filter.responsible
                        )
                    )
                )
            }
        }
    }

}
