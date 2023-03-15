package br.com.market.storage.ui.states

import br.com.market.core.ui.states.BaseUiState
import br.com.market.domain.ProductDomain

/**
 * Classe de estado da tela de listagem dos produtos em estoque
 *
 * @property products Produtos exibidos na tela
 * @property searchText Texto pesquisado
 * @property openSearch Flag que indica se a pesquisa está aberta
 * @property onToggleSearch Listener executado para escoder e exibir a pesquisa
 * @property onSearchChange Listener executado quando o texto é alterado no campo de pesquisa
 * @property registersCountToSync Quantidade de registros que precisam ser sincronizados
 *
 * @author Nikolas Luiz Schmitt
 */
data class StorageProductsUiState(
    val products: List<ProductDomain> = emptyList(),
    val searchText: String = "",
    val openSearch: Boolean = false,
    val onToggleSearch: () -> Unit = { },
    val onSearchChange: (String) -> Unit = { },
    val registersCountToSync: Long = 0,
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = { },
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val onValidate: () -> Boolean = { false }
) : BaseUiState() {

    fun isSearching(): Boolean = searchText.isNotBlank()
}