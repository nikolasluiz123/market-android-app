package br.com.market.storage.business.services

import br.com.market.storage.business.sdo.product.NewProductSDO
import br.com.market.storage.business.sdo.product.UpdateProductSDO
import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface utilizada para acessar as funções do ProductController do serviço.
 *
 * @author Nikolas Luiz Schmitt
 *
 */
interface ProductService {

    /**
     * Função para salvar um produto na base de dados remota.
     *
     * @author Nikolas Luiz Schmitt
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param productDTO Produto com os atributos que o serviço precisa.
     *
     */
    @POST("product")
    suspend fun saveProduct(@Header("Authorization") token: String, @Body productDTO: NewProductSDO): Response<PersistenceResponse>

    /**
     * Função para atualizar um produto na base de dados remota.
     *
     * @author
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param productDTO Produto com os atributos que o serviço precisa.
     *
     */
    @PUT("product")
    suspend fun updateProduct(@Header("Authorization") token: String, @Body productDTO: UpdateProductSDO): Response<PersistenceResponse>

    /**
     * Função para sincronizar os produtos que foram salvos ou alterados apenas localmente.
     *
     * @author Nikolas Luiz Schmitt
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param productDTOs Lista de produtos com os atributos que o serviço precisa.
     *
     */
    @POST("product/synchronize")
    suspend fun syncProducts(@Header("Authorization") token: String, @Body productDTOs: List<NewProductSDO>): Response<MarketServiceResponse>

}