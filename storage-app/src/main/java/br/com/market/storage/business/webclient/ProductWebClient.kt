package br.com.market.storage.business.webclient

import android.content.Context
import br.com.market.storage.R
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.sdo.product.NewProductSDO
import br.com.market.storage.business.sdo.product.UpdateProductSDO
import br.com.market.storage.business.services.ProductService
import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import br.com.market.storage.preferences.dataStore
import br.com.market.storage.preferences.getToken
import br.com.market.storage.utils.TokenUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject


/**
 * Classe usada para realizar operações no serviço.
 *
 * @author Nikolas Luiz Schmitt
 *
 * @property productService Interface para acesso do serviço.
 *
 */
class ProductWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productService: ProductService
) {

    /**
     * Função para salvar um produto na base de dados remota.
     *
     * @author Nikolas Luiz Schmitt
     *
     * @param product Produto que deseja salvar na base remota.
     *
     * @exception SocketTimeoutException Pode ocorrer essa exceção quando o serviço demorar para responder,
     * nesse caso podemos considerar como sucesso pois nós salvamos o produto localmente e permitimos uma
     * sincronização dos dados.
     *
     * @exception ConnectException Pode ocorrer essa exceção quando o usuário não possuir conexão de internet
     * no dispositivo, nesse caso podemos considerar como sucesso pois nós salvamos o produto localmente e permitimos
     * uma sincronização dos dados.
     *
     */
    suspend fun saveProduct(product: Product): PersistenceResponse {
        return try {
            val token = TokenUtils.formatToken(context.dataStore.getToken()!!)
            val newProductSDO = NewProductSDO(idLocal = product.id, name = product.name, imageUrl = product.imageUrl)
            val response = productService.saveProduct(token, newProductSDO)

            val gson = Gson()
            val type = object : TypeToken<PersistenceResponse>() {}.type
            response.body() ?: gson.fromJson(response.errorBody()!!.charStream(), type)
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException, is ConnectException -> {
                    PersistenceResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = true
                    )
                }
                else -> throw e
            }
        }
    }

    /**
     * Função para alterar um produto na base de dados remota.
     *
     * @author Nikolas Luiz Schmitt
     *
     * @param product Produto que deseja alterar na base remota.
     *
     * @exception SocketTimeoutException Pode ocorrer essa exceção quando o serviço demorar para responder,
     * nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos uma
     * sincronização dos dados.
     *
     * @exception ConnectException Pode ocorrer essa exceção quando o usuário não possuir conexão de internet
     * no dispositivo, nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos
     * uma sincronização dos dados.
     *
     */
    suspend fun updateProduct(product: Product): PersistenceResponse {
        return try {
            val token = TokenUtils.formatToken(context.dataStore.getToken()!!)
            val updateProductSDO = UpdateProductSDO(idLocal = product.id, name = product.name, imageUrl = product.imageUrl)
            val response = productService.updateProduct(token, updateProductSDO)

            val gson = Gson()
            val type = object : TypeToken<PersistenceResponse>() {}.type
            response.body() ?: gson.fromJson(response.errorBody()!!.charStream(), type)
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException, is ConnectException -> {
                    PersistenceResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = true
                    )
                }
                else -> throw e
            }
        }
    }

    /**
     * Função para sincronizar a base de dados local com a remota. Os produtos criados e alterados serão
     * enviados para a base de dados remota.
     *
     * @author Nikolas Luiz Schmitt
     *
     * @param products Produtos que deseja sincronizar.
     *
     * @exception SocketTimeoutException Pode ocorrer essa exceção quando o serviço demorar para responder,
     * como estamos realizando um sincronismo é obrigatório o serviço estar respondendo.
     *
     * @exception ConnectException Pode ocorrer essa exceção quando o usuário não possuir conexão de internet
     * no dispositivo, nesse caso é obrigatório o acesso a internet para possibilitar o envio dos produtos
     * para o serviço.
     *
     */
    suspend fun syncProducts(products: List<Product>): MarketServiceResponse {
        return try {
            val token = TokenUtils.formatToken(context.dataStore.getToken()!!)
            val dtoList = products.map { NewProductSDO(idLocal = it.id, name = it.name, imageUrl = it.imageUrl) }
            val response = productService.syncProducts(token, dtoList)

            val gson = Gson()
            val type = object : TypeToken<PersistenceResponse>() {}.type
            response.body() ?: gson.fromJson(response.errorBody()!!.charStream(), type)
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    MarketServiceResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = false,
                        error = context.getString(R.string.sync_products_service_error_message)
                    )
                }
                is ConnectException -> {
                    MarketServiceResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = false,
                        error = context.getString(R.string.sync_products_conection_error_message)
                    )
                }
                else -> throw e
            }
        }
    }
}