package br.com.market.storage.business.services

import br.com.market.storage.business.sdo.product.DeleteProductSDO
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
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param productSDO Produto com os atributos que o serviço precisa.
     *
     * @author Nikolas Luiz Schmitt
     *
     */
    @POST("product")
    suspend fun saveProduct(@Header("Authorization") token: String, @Body productSDO: NewProductSDO): Response<PersistenceResponse>

    /**
     * Função para atualizar um produto na base de dados remota.
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param productSDO Produto com os atributos que o serviço precisa.
     *
     * @author Nikolas Luiz Schmitt
     *
     */
    @PUT("product")
    suspend fun updateProduct(@Header("Authorization") token: String, @Body productSDO: UpdateProductSDO): Response<PersistenceResponse>

    /**
     * Função para remover um Produto e os dados com referência a ele.
     *
     * OBS: Está sendo usado o método HTTP Post pois adotei o padrão de passagem de um Body,
     * mas o DELETE não suporta. Isso foi feito devido a necessidade de deletar mais de uma entidade
     * no sincronismo dai não ficamos com dois padrões de DELETE.
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param productSDO Produto com os atributos que o serviço precisa.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("product/delete")
    suspend fun deleteProduct(@Header("Authorization") token: String, @Body productSDO: DeleteProductSDO): Response<MarketServiceResponse>

    /**
     * Função para remover 'N' Produtos e os dados com referência a ele.
     *
     * Utilizada no sincronismos das bases de dados.
     *
     * OBS: Está sendo usado o método HTTP Post pois adotei o padrão de passagem de um Body,
     * se isso não fosse feito teriam de ser passados os 'N' ids por GET
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param productSDOs Produtos com os atributos que o serviço precisa.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("product/synchronize/delete")
    suspend fun deleteProducts(@Header("Authorization") token: String, @Body productSDOs: List<DeleteProductSDO>): Response<MarketServiceResponse>

    /**
     * Função que sincronizar as informações locais e remotas referentes ao produto.
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param productSDOs Lista de produtos com os atributos que o serviço precisa.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("product/synchronize")
    suspend fun syncProducts(@Header("Authorization") token: String, @Body productSDOs: List<NewProductSDO>): Response<MarketServiceResponse>

}