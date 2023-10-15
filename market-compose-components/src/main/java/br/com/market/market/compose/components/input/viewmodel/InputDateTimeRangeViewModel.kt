package br.com.market.market.compose.components.input.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.extensions.fromJsonNavParamToArgs
import br.com.market.core.inputs.arguments.DateTimeRangeInputArgs
import br.com.market.core.gson.adapter.LocalDateTimeAdapter
import br.com.market.core.ui.states.filter.DateRangeAdvancedFilterUIState
import br.com.market.market.compose.components.input.navigation.dateRangeAdvancedFilterArguments
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltViewModel
class InputDateTimeRangeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<DateRangeAdvancedFilterUIState> = MutableStateFlow(DateRangeAdvancedFilterUIState())
    val uiState get() = _uiState.asStateFlow()

    private val jsonArgs: String? = savedStateHandle[dateRangeAdvancedFilterArguments]

    init {
        val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter()).create()

        jsonArgs?.fromJsonNavParamToArgs(DateTimeRangeInputArgs::class.java, gson)?.let { args ->
            _uiState.update { currentState ->
                currentState.copy(
                    titleResId = args.titleResId,
                    dateFrom = getDateFrom(args),
                    timeFrom = getTimeFrom(args),
                    dateTo = getDateTo(args),
                    timeTo = getTimeTo(args),
                    onDateFromChange = { _uiState.value = _uiState.value.copy(dateFrom = it) },
                    onTimeFromChange = { _uiState.value = _uiState.value.copy(timeFrom = it) },
                    onDateToChange = { _uiState.value = _uiState.value.copy(dateTo = it) },
                    onTimeToChange = { _uiState.value = _uiState.value.copy(timeTo = it) }
                )
            }
        }
    }

    private fun getDateFrom(args: DateTimeRangeInputArgs): String? {
        return args.value?.first?.format(DateTimeFormatter.ofPattern(EnumDateTimePatterns.DATE.pattern))
    }

    private fun getTimeFrom(args: DateTimeRangeInputArgs): String? {
        return args.value?.first?.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    }

    private fun getDateTo(args: DateTimeRangeInputArgs): String? {
        return args.value?.second?.format(DateTimeFormatter.ofPattern(EnumDateTimePatterns.DATE.pattern))
    }

    private fun getTimeTo(args: DateTimeRangeInputArgs): String? {
        return args.value?.second?.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    }
}
