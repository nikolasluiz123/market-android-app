package br.com.market.core.ui.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.enums.EnumFileExtension
import br.com.market.core.extensions.navParamToString
import br.com.market.core.ui.navigation.pdfViewerUriArgument
import br.com.market.core.ui.states.PDFViewerUIState
import br.com.market.core.utils.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PDFViewerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<PDFViewerUIState> = MutableStateFlow(PDFViewerUIState())
    val uiState get() = _uiState.asStateFlow()

    private val uriString: String? = savedStateHandle[pdfViewerUriArgument]

    init {
        uriString?.navParamToString()?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    uri = FileUtils.getUriFromPath(context, it, EnumFileExtension.PDF_FILE),
                    path = it
                )
            }
        }
    }
}