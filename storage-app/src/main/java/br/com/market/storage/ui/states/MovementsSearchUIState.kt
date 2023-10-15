package br.com.market.storage.ui.states

import androidx.paging.PagingData
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.StorageOperationHistoryReadDomain
import br.com.market.market.pdf.generator.common.ReportFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MovementsSearchUIState(
    var categoryId: String? = null,
    var brandId: String? = null,
    var productId: String? = null,
    var productName: String? = null,
    var brandName: String? = null,
    var productQuantity: Int = 0,
    val operations: Flow<PagingData<StorageOperationHistoryReadDomain>> = emptyFlow(),
    val generatedReports: List<ReportFile> = emptyList(),
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState {

}
