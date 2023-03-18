package br.com.market.servicedataaccess.services

import br.com.market.sdo.brand.*
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.responses.ReadResponse
import retrofit2.Response
import retrofit2.http.*


/**
 * Interface utilizada para acessar os end points da marca no serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
interface BrandService {

    /**
     * Função utilizada para salvar uma marca no serviço
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param brandSDO Marca que será incluída.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("brand")
    suspend fun saveBrand(@Header("Authorization") token: String, @Body brandSDO: NewBrandSDO): Response<PersistenceResponse>

    /**
     * Função utilizada para alterar uma marca no serviço.
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param brandSDO Marca que será alterada.
     *
     * @author Nikolas Luiz Schmitt
     */
    @PUT("brand")
    suspend fun updateBrand(@Header("Authorization") token: String, @Body brandSDO: UpdateBrandSDO): Response<PersistenceResponse>

    /**
     * Função utilizada para deletar uma marca no serviço.
     *
     * OBS: Está sendo usado o método HTTP Post pois adotei o padrão de passagem de um Body,
     * mas o DELETE não suporta. Isso foi feito devido a necessidade de deletar mais de uma entidade
     * no sincronismo dai não ficamos com dois padrões de DELETE.
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param brandSDO Marca que será excluída.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("brand/delete")
    suspend fun deleteBrand(@Header("Authorization") token: String, @Body brandSDO: DeleteBrandSDO): Response<MarketServiceResponse>

    /**
     * Função utilizada para sincronizar as informações locais e remotas referentes a marca.
     *
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param brandsDTOList Lista das marcas que serão enviadas para sincronizar os dados.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("brand/synchronize")
    suspend fun syncBrands(@Header("Authorization") token: String, @Body brandsDTOList: List<NewBrandSDO>): Response<MarketServiceResponse>

    /**
     * Função utilizada para deletar várias marcas, usado no sincronismo.
     *
     * OBS: Está sendo usado o método HTTP Post pois adotei o padrão de passagem de um Body,
     * se isso não fosse feito teriam de ser passados os 'N' ids por GET.
     *
     * @param token Token recebido ao logar que é usado em todas as requisições ao serviço.
     * @param brandsDTOList Lista de marcas que serão excluídas.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("brand/synchronize/delete")
    suspend fun deleteBrands(@Header("Authorization") token: String, @Body brandsDTOList: List<DeleteBrandSDO>): Response<MarketServiceResponse>

    @GET("brand")
    suspend fun findAllBrands(@Header("Authorization") token: String): Response<ReadResponse<SyncBrandSDO>>

    @GET("brand/productBrands")
    suspend fun findAllProductBrands(@Header("Authorization") token: String): Response<ReadResponse<SyncProductBrandSDO>>

}