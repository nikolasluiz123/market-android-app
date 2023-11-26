package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Category
import br.com.market.sdo.CategoryReadSDO
import br.com.market.sdo.CategorySDO
import br.com.market.servicedataaccess.responses.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.extensions.getSingleValueResponseBody
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
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
                    marketId = category.marketId
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
    suspend fun toggleActive(id: String): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                service.toggleActive(getToken(), id).getPersistenceResponseBody()
            }
        )
    }

    /**
     * Função que busca todos as categorias da base remota
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun getListCategoryReadSDO(simpleFilter: String?, marketId: Long, limit: Int, offset: Int): ReadResponse<CategoryReadSDO> {
        return service.getListCategoryReadSDO(getToken(), simpleFilter, marketId, limit, offset).getReadResponseBody()
    }

    suspend fun findCategoryByLocalId(id: String): SingleValueResponse<CategorySDO> {
        return service.findCategoryByLocalId(getToken(), id).getSingleValueResponseBody()
    }
}