package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Brand
import br.com.market.models.CategoryBrand
import br.com.market.sdo.BrandAndReferencesSDO
import br.com.market.sdo.BrandSDO
import br.com.market.sdo.CategoryBrandSDO
import br.com.market.servicedataaccess.responses.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.IBrandService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Classe usada para realizar operações dos end points da marca,
 * realizando as traduções necessárias do contexto local para o remoto.
 *
 * @property context Contexto de uso diversificado
 * @property service Interface para acesso do serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
class BrandWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: IBrandService
) : BaseWebClient(context) {

    /**
     * Função para salvar uma marca na base remota
     *
     * @param brand Marca que deseja salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun save(brand: Brand, categoryBrand: CategoryBrand): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val brandSDO = BrandSDO(
                    name = brand.name!!,
                    localId = brand.id,
                    active = brand.active,
                    marketId = brand.marketId
                )

                val categoryBrandSDO = CategoryBrandSDO(
                    localId = categoryBrand.id,
                    localCategoryId = categoryBrand.categoryId!!,
                    localBrandId = categoryBrand.brandId!!,
                    active = categoryBrand.active,
                    marketId = categoryBrand.marketId
                )

                service.save(getToken(), BrandAndReferencesSDO(brandSDO, categoryBrandSDO)).getPersistenceResponseBody()
            }
        )
    }

    /**
     * Função para alterar a flag [Brand.active] na base remota.
     *
     * @param categoryBrand Marca que deseja alterar a flag
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun toggleActive(categoryBrand: CategoryBrand): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val categoryBrandSDO = CategoryBrandSDO(
                    localId = categoryBrand.id,
                    active = categoryBrand.active,
                    localCategoryId = categoryBrand.categoryId!!,
                    localBrandId = categoryBrand.brandId!!,
                    marketId = categoryBrand.marketId
                )

                service.toggleActive(getToken(), categoryBrandSDO).getPersistenceResponseBody()
            }
        )
    }

    suspend fun getListBrand(simpleFilter: String?, marketId: Long, categoryLocalId: String?, limit: Int, offset: Int): ReadResponse<BrandAndReferencesSDO> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                service.getListBrand(
                    token = getToken(),
                    simpleFilter = simpleFilter,
                    marketId = marketId,
                    categoryLocalId = categoryLocalId,
                    limit = limit,
                    offset = offset
                ).getReadResponseBody()
            }
        )
    }
}