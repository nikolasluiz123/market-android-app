package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Brand
import br.com.market.models.CategoryBrand
import br.com.market.sdo.brand.BrandBodySDO
import br.com.market.sdo.brand.BrandSDO
import br.com.market.sdo.brand.CategoryBrandSDO
import br.com.market.servicedataaccess.responses.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.extensions.getResponseBody
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
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
                    active = brand.active
                )

                val categoryBrandSDO = CategoryBrandSDO(
                    localId = categoryBrand.id,
                    localCategoryId = categoryBrand.categoryId!!,
                    localBrandId = categoryBrand.brandId!!,
                    active = categoryBrand.active,
                )

                service.save(getToken(), BrandBodySDO(brandSDO, categoryBrandSDO)).getPersistenceResponseBody()
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
                    localBrandId = categoryBrand.brandId!!
                )

                service.toggleActive(getToken(), categoryBrandSDO).getPersistenceResponseBody()
            }
        )
    }

    /**
     * Função para enviar o que está presenta apenas na base local do dispositivo
     * para a base remota.
     *
     * @param brands Marcas que deseja enviar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun sync(brands: List<Brand>, categoryBrands: List<CategoryBrand>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val brandSDOs = brands.map {
                    BrandSDO(
                        localId = it.id,
                        name = it.name,
                        active = it.active
                    )
                }

                val categoryBrandSDOs = categoryBrands.map {
                    CategoryBrandSDO(
                        localId = it.id,
                        localCategoryId = it.categoryId!!,
                        localBrandId = it.brandId!!,
                        active = it.active
                    )
                }

                val syncList = mutableListOf<BrandBodySDO>()

                for (brandSDO in brandSDOs) {
                    for (categoryBrandSDO in categoryBrandSDOs) {
                        if (categoryBrandSDO.localBrandId == brandSDO.localId) {
                            syncList.add(BrandBodySDO(brandSDO, categoryBrandSDO))
                            break
                        }
                    }
                }

                service.sync(getToken(), syncList).getResponseBody()
            }
        )
    }

    /**
     * Função que busca todos as marcas da base remota
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun findAllBrands(): ReadResponse<Brand> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findAllBrandDTOs(getToken()).getReadResponseBody()

                val brands = response.values.map {
                    Brand(id = it.localId, name = it.name, synchronized = true, active = it.active)
                }

                ReadResponse(values = brands, code = response.code, success = response.success, error = response.error)
            }
        )
    }

    /**
     * Função que busca todos as categoria marca da base remota
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun findAllCategoryBrands(): ReadResponse<CategoryBrand> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findAllCategoryBrandDTOs(getToken()).getReadResponseBody()

                val brands = response.values.map {
                    CategoryBrand(
                        id = it.localId,
                        categoryId = it.localCategoryId,
                        brandId = it.localBrandId,
                        synchronized = true,
                        active = it.active
                    )
                }

                ReadResponse(values = brands, code = response.code, success = response.success, error = response.error)
            }
        )
    }
}