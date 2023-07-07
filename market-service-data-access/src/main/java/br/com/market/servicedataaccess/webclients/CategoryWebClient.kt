package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Category
import br.com.market.sdo.CategorySDO
import br.com.market.sdo.filters.CategoryFiltersSDO
import br.com.market.servicedataaccess.responses.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.extensions.getResponseBody
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.ICategoryService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Classe usada para realizar operações dos end points da categoria,
 * realizando as traduções necessárias do contexto local para o remoto.
 *
 * @property context Contexto de uso diversificado
 * @property service Interface para acesso do serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
class CategoryWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: ICategoryService
) : BaseWebClient(context) {

    /**
     * Função para salvar uma categoria na base remota
     *
     * @param category Categoria que deseja salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun save(category: Category): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val categorySDO = CategorySDO(
                    name = category.name!!,
                    localId = category.id,
                    active = category.active,
                    companyId = category.companyId
                )

                service.save(getToken(), categorySDO).getPersistenceResponseBody()
            }
        )
    }

    /**
     * Função para alterar a flag [Category.active] na base remota.
     *
     * @param category Categoria que deseja alterar a flag
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun toggleActive(category: Category): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val categorySDO = CategorySDO(
                    localId = category.id,
                    active = category.active
                )

                service.toggleActive(getToken(), categorySDO).getPersistenceResponseBody()
            }
        )
    }

    /**
     * Função para enviar o que está presenta apenas na base local do dispositivo
     * para a base remota.
     *
     * @param categories Categorias que deseja enviar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun sync(categories: List<Category>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val categorySDOs = categories.map {
                    CategorySDO(
                        localId = it.id,
                        name = it.name,
                        active = it.active
                    )
                }

                service.sync(getToken(), categorySDOs).getResponseBody()
            }
        )
    }

    /**
     * Função que busca todos as categorias da base remota
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun findAll(companyId: Long): ReadResponse<Category> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findAll(getToken(), CategoryFiltersSDO(companyId = companyId)).getReadResponseBody()

                val categories = response.values.map {
                    Category(
                        id = it.localId,
                        name = it.name,
                        synchronized = true,
                        active = it.active,
                        companyId = it.companyId
                    )
                }

                ReadResponse(values = categories, code = response.code, success = response.success, error = response.error)
            }
        )
    }
}