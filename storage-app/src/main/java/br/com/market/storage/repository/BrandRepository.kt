package br.com.market.storage.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.BrandDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.models.Brand
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.webclients.BrandWebClient
import br.com.market.storage.pagination.BrandPagingSource
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

/**
 * Classe Repository responsável pela manipulação de dados
 * referentes a Marca.
 *
 * @property webClient WebClient para acesso dos End Points referentes a Marca.
 * @property dao DAO responsável pelo acesso ao dados locais referentes a Marca.
 *
 * @author Nikolas Luiz Schmitt
 */
class BrandRepository @Inject constructor(
    private val dao: BrandDAO,
    private val webClient: BrandWebClient
) {

    /**
     * Função para obter um fluxo de dados paginados que possa ser
     * fornecido a tela de listagem de marcas
     *
     * @author Nikolas Luiz Schmitt
     */
    fun findBrands(): Flow<PagingData<BrandDomain>> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = { BrandPagingSource(dao) }
        ).flow
    }

    /**
     * Função que persiste uma marca nas bases local e remota.
     *
     * Primeiro tenta persistir na base remota, sendo sucesso,
     * define a marca como sincronizada e persiste também na base
     * local.
     *
     * Se ocorrer algum erro na persistência remota, a marca será
     * salva localmente e marcada como não sincronizada para futura
     * sincronização.
     *
     * @param domain Classe de domínio com as informações que deseja salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun save(domain: BrandDomain): PersistenceResponse {
        val brand = if (domain.id != null) {
            dao.findById(domain.id!!).copy(name = domain.name)
        } else {
            Brand(name = domain.name)
        }

        domain.id = brand.id

        val response = webClient.save(brand = brand)

        brand.synchronized = response.getObjectSynchronized()
        dao.save(brand = brand)

        return  response
    }

    /**
     * Função para buscar uma marca com um ID específico.
     *
     * @param brandId Id da marca que deseja buscar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun findById(brandId: UUID): BrandDomain {
        val brand = dao.findById(brandId)
        return BrandDomain(id = brand.id, name = brand.name!!, active = brand.active)
    }

    /**
     * Função para alterar a flag [Brand.active] nas bases local e remota.
     *
     * Primeiro tenta alterar na base remota, sendo sucesso,
     * define a marca como sincronizada e altera também na base
     * local.
     *
     * Se ocorrer algum erro na alteração remota, a marca será
     * alerada localmente e marcada como não sincronizada para futura
     * sincronização.
     *
     * @param brandId Id da marca que deseja reativar ou inativar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun toggleActive(brandId: UUID): PersistenceResponse {
        val category = dao.findById(brandId)

        val response = webClient.toggleActive(category)

        category.synchronized = response.getObjectSynchronized()

        dao.toggleActive(category)

        return response
    }

    /**
     * Função para sincronizar os dados locais e remotos.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun sync(): MarketServiceResponse {
        val response = sendBrandsToRemoteDB()
        return if (response.success) updateBrandsOfLocalDB() else response
    }

    /**
     * Função para enviar as marcas para o banco remoto
     *
     * @author Nikolas Luiz Schmitt
     */
    private suspend fun sendBrandsToRemoteDB(): MarketServiceResponse {
        val brandsNotSynchronized = dao.findBrandsNotSynchronized()
        val response = webClient.sync(brandsNotSynchronized)

        if (response.success) {
            val brandsSynchronized = brandsNotSynchronized.map { it.copy(synchronized = true) }
            dao.save(brandsSynchronized)
        }

        return response
    }

    /**
     * Função para buscar as marcas da base remota e cadastrar ou alterar
     * elas na base local
     *
     * @author Nikolas Luiz Schmitt
     */
    private suspend fun updateBrandsOfLocalDB(): MarketServiceResponse {
        val response = webClient.findAll()

        if (response.success) {
            dao.save(response.values)
        }

        return response.toBaseResponse()
    }

}