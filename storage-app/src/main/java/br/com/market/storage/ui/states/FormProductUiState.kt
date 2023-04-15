package br.com.market.storage.ui.states

import br.com.market.core.ui.states.BaseUiState2
import br.com.market.domain.ProductBrandDomain
import java.util.*

/**
 * Classe de estado da tela de manutenção do produto e das marcas.
 *
 * @property productName Nome do produto
 * @property productImage Link da imagem do produto
 * @property brandId Identificador da marca
 * @property brandName Nome da marca
 * @property brandQtd Quantidade de um produto de uma marca
 * @property openSearch Flag que indica se a pesquisa está ativada
 * @property brands Lista com as marcas
 * @property searchText Texto pesquisado
 * @property productImageErrorMessage Mensagem de erro do campo da imagem do produto
 * @property productNameErrorMessage Mensagem de erro do campo do noma do produto
 * @property brandNameErrorMessage Mensagem de erro do campo do nome da marca
 * @property qtdBrandErrorMessage Mensagem de erro do campo de quantidade do produto de uma marca específica
 * @property openBrandDialog Flag que indica se a dialog de manutenção da marca está aberta
 * @property onValidateBrand Listener executado para validar a marca
 * @property onHideBrandDialog Listener executado para esconder a dialog de manutenção da marca
 * @property onShowBrandDialog Listener executado para exibir a dialog de manutenção da marca
 * @property onValidateProduct Listener executado para validar o produto
 * @property onProductNameChange Listener executado quando o nome do produto é alterado no campo de texto
 * @property onProductImageChange Listener executado quando o link da imagem do produto é alterado no campo de texto
 * @property onBrandsChange Listener executado quando a lista de marcas é alterada
 * @property onBrandNameChange Listener executado quando o nome da marca é alterado no campo de texto
 * @property onBrandQtdChange Listener executado quando a quantidade de um produto de uma marca é alterado no campo de texto
 * @property onToggleSearch Listener executado para escoder ou exibir o campo de pesquisa
 * @property onSearchChange Listener executado quando o texto no campo de pesquisa é alterado
 *
 * @author Nikolas Luiz Schmitt
 */
data class FormProductUiState(
    val productName: String = "",
    val productImage: String = "",
    val brandId: UUID? = null,
    val brandName: String = "",
    val brandQtd: String = "",
    val openSearch: Boolean = false,
    val brands: List<ProductBrandDomain> = mutableListOf(),
    val searchText: String = "",
    val productImageErrorMessage: String = "",
    val productNameErrorMessage: String = "",
    val brandNameErrorMessage: String = "",
    val qtdBrandErrorMessage: String = "",
    val openBrandDialog: Boolean = false,
    val onValidateBrand: () -> Boolean = { true },
    val onHideBrandDialog: () -> Unit = { },
    val onShowBrandDialog: (ProductBrandDomain?) -> Unit = { },
    val onValidateProduct: () -> Boolean = { true },
    val onProductNameChange: (String) -> Unit = { },
    val onProductImageChange: (String) -> Unit = { },
    val onBrandsChange: (ProductBrandDomain) -> Unit = { },
    val onBrandNameChange: (String) -> Unit = { },
    val onBrandQtdChange: (String) -> Unit = { },
    val onToggleSearch: () -> Unit = { },
    val onSearchChange: (String) -> Unit = { },
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = { },
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val onValidate: () -> Boolean = { false },
): BaseUiState2()
