package br.com.market.storage.ui.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.domain.BrandDomain
import br.com.market.domain.ProductBrandDomain
import br.com.market.domain.ProductDomain
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.storage.R
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.ui.states.FormProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel da tela de manutenção do estoque.
 *
 * @property productRepository Classe responsável por realizar operações referentes ao Produto.
 * @property brandRepository
 *
 * @param context Contexto do APP
 * @param savedStateHandle Utilizado para recuperar atributos de navegação
 *
 * @author Nikolas Luiz Schmitt
 */
@HiltViewModel
class FormProductViewModel @Inject constructor(
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<FormProductUiState> = MutableStateFlow(FormProductUiState())
    val uiState get() = _uiState.asStateFlow()

    var productId = savedStateHandle.get<String>("productId")
        private set

    init {
        updateProductFormInfos()
        updateProductBrandsInfos()

        _uiState.update { currentState ->
            currentState.copy(
                onProductNameChange = { _uiState.value = _uiState.value.copy(productName = it, productNameErrorMessage = "") },
                onProductImageChange = { _uiState.value = _uiState.value.copy(productImage = it, productImageErrorMessage = "") },
                onBrandNameChange = { _uiState.value = _uiState.value.copy(brandName = it) },
                onBrandQtdChange = { _uiState.value = _uiState.value.copy(brandQtd = it) },
                onBrandsChange = { _uiState.value = _uiState.value.copy(brands = _uiState.value.brands + it) },
                onToggleSearch = { _uiState.value = _uiState.value.copy(openSearch = !_uiState.value.openSearch) },
                onSearchChange = ::updateProductBrandsInfos,
                onHideBrandDialog = { _uiState.value = _uiState.value.copy(openBrandDialog = false) },
                onShowBrandDialog = { productBrandDomain ->
                    _uiState.value = _uiState.value.copy(
                        brandId = productBrandDomain?.brandId,
                        brandName = productBrandDomain?.brandName ?: "",
                        brandQtd = productBrandDomain?.count?.toString() ?: "",
                        openBrandDialog = true
                    )
                },
                onToggleMessageDialog = { errorMessage ->
                    _uiState.value = _uiState.value.copy(
                        showDialogMessage = !_uiState.value.showDialogMessage,
                        serverMessage = errorMessage
                    )
                },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) },
                onValidateProduct = {
                    var isValid = true

                    if (_uiState.value.productName.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productNameErrorMessage = context.getString(R.string.form_product_screen_tab_product_required_product_name_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            productNameErrorMessage = ""
                        )
                    }

                    if (_uiState.value.productImage.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productImageErrorMessage = context.getString(R.string.form_product_screen_tab_product_required_product_image_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            productImageErrorMessage = ""
                        )
                    }

                    if (isValid) {
                        _uiState.value = _uiState.value.copy(
                            productNameErrorMessage = "",
                            productImageErrorMessage = "",
                        )
                    }

                    isValid
                },
                onValidateBrand = {
                    var isValid = true

                    if (_uiState.value.brandName.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            brandNameErrorMessage = context.getString(R.string.form_product_screen_tab_brand_required_brand_name_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            brandNameErrorMessage = ""
                        )
                    }

                    if (_uiState.value.brandQtd.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            qtdBrandErrorMessage = context.getString(R.string.form_product_screen_tab_brand_required_brand_qtd_message)
                        )
                    } else if (_uiState.value.brandQtd.toInt() <= 0) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            qtdBrandErrorMessage = context.getString(R.string.form_product_screen_tab_brand_invalid_brand_qtd_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            qtdBrandErrorMessage = ""
                        )
                    }

                    isValid
                }
            )
        }
    }

    suspend fun saveProduct(productDomain: ProductDomain): PersistenceResponse {
        val response = productRepository.save(productDomain)
        productId = response.idLocal.toString()

        updateProductBrandsInfos()

        return response
    }

    suspend fun deleteProduct(): MarketServiceResponse {
        return productRepository.deleteProduct(UUID.fromString(productId!!.navParamToString()))
    }

    suspend fun deleteBrand(brandId: UUID): MarketServiceResponse {
        return brandRepository.deleteBrand(brandId)
    }

    suspend fun saveBrand(brand: BrandDomain): PersistenceResponse {
        return brandRepository.save(UUID.fromString(productId!!.navParamToString()), brand)
    }

    fun permissionNavToBrand(): Boolean {
        return productId != null
    }

    private fun updateProductFormInfos() {
        if (productId != null) {
            val productDomainFlow = productRepository.findProductById(UUID.fromString(productId!!.navParamToString()))

            viewModelScope.launch {
                productDomainFlow.collect {
                    _uiState.value = _uiState.value.copy(
                        productName = it?.name ?: "",
                        productImage = it?.imageUrl ?: ""
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(
                productName = "",
                productImage = ""
            )
        }
    }

    private fun updateProductBrandsInfos(searchedText: String = "") {
        if (productId != null) {
            val productBrandDomainFlow = brandRepository.findAllActiveProductBrandsByProductId(UUID.fromString(productId!!.navParamToString()))

            viewModelScope.launch {
                productBrandDomainFlow.collect { productBrandDomainList ->
                    val brands = if (searchedText.isNotEmpty()) {
                        productBrandDomainList.filter { productBrandDomain ->
                            containsProductNameOrBrandName(productBrandDomain, searchedText)
                        }
                    } else {
                        productBrandDomainList
                    }

                    _uiState.value = _uiState.value.copy(
                        searchText = searchedText,
                        brands = brands
                    )
                }
            }
        }
    }

    private fun containsProductNameOrBrandName(product: ProductBrandDomain, searchedText: String): Boolean {
        return product.productName.contains(searchedText, ignoreCase = true) ||
                product.brandName.contains(searchedText, ignoreCase = true)
    }
}
