package br.com.market.storage.ui.states

import androidx.paging.PagingData
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.localdataaccess.tuples.StorageOperationHistoryTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MovementsSearchUIState(
    var categoryId: String? = null,
    var brandId: String? = null,
    var productId: String? = null,
    var productName: String? = null,
    var brandName: String? = null,
    var productQuantity: Int = 0,
    val operations: Flow<PagingData<StorageOperationHistoryTuple>> = emptyFlow(),
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState {

}
