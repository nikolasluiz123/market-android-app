package br.com.market.commerce.ui.state

import androidx.paging.PagingData
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.core.ui.states.IServerErrorHandlerUIState
import br.com.market.localdataaccess.tuples.ProductImageTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ProductsUIState(
    var products: Flow<PagingData<ProductImageTuple>> = emptyFlow(),
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = { },
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { }
) : ILoadingUIState, IServerErrorHandlerUIState