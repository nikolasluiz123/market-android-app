package br.com.market.storage.ui.viewmodels.report

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.navParamToString
import br.com.market.core.extensions.searchWordsInText
import br.com.market.market.pdf.generator.common.ReportFile
import br.com.market.market.pdf.generator.utils.FileUtils
import br.com.market.storage.ui.navigation.report.argumentDirectory
import br.com.market.storage.ui.states.report.ReportSearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ReportSearchScreenViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState: MutableStateFlow<ReportSearchUIState> = MutableStateFlow(ReportSearchUIState())

    val uiState get() = _uiState.asStateFlow()
    private val directory: String? = savedStateHandle[argumentDirectory]

    init {
        directory?.navParamToString()?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    reports = FileUtils.getReportsFromFolder(context, it)
                )
            }
        }
    }

    fun simpleFilter(filter: String) {
        _uiState.value = _uiState.value.copy(
            reports = _uiState.value.reports.filter {
                it.name.searchWordsInText(filter) ||
                        it.date.searchWordsInText(filter)
            }
        )
    }

    fun saveReportClicked(reportFile: ReportFile) {
        _uiState.value = _uiState.value.copy(reportClicked = reportFile)
    }
}
