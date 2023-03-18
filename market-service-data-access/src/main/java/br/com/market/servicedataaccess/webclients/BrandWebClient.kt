package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Brand
import br.com.market.models.ProductBrand
import br.com.market.sdo.brand.DeleteBrandSDO
import br.com.market.sdo.brand.NewBrandSDO
import br.com.market.sdo.brand.UpdateBrandSDO
import br.com.market.servicedataaccess.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.extensions.getReadResponseBody
import br.com.market.servicedataaccess.extensions.getResponseBody
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.responses.ReadResponse
import br.com.market.servicedataaccess.services.BrandService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

/**
 * Classe usada para realizar operações dos end points da marca.
 *
 * @property context Contexto de uso diversificado
 * @property brandService Interface para acesso do serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
class BrandWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val brandService: BrandService
) : BaseWebClient(context) {

    /**
     * Função utilizada para salvar uma marca.
     *
     * @param productId Id do produto que está sendo editado.
     * @param brand Brand que já foi salva localmente.
     * @param productBrand ProductBrand que já foi salva localmente,
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun saveBrand(productId: UUID, brand: Brand, productBrand: ProductBrand): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val newBrandSDO = NewBrandSDO(
                    localProductBrandId = productBrand.id,
                    localProductId = productId,
                    localBrandId = brand.id,
                    name = brand.name,
                    count = productBrand.count
                )
                brandService.saveBrand(getToken(), newBrandSDO).getPersistenceResponseBody()
            }
        )
    }

    /**
     * Função para atualizar uma marca.
     *
     * @param brand Brand que deseja atualizar.
     * @param productBrand ProductBrand que deseja atualizar.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun updateBrand(brand: Brand, productBrand: ProductBrand): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val updateBrandSDO = UpdateBrandSDO(brand.id, brand.name, productBrand.count)
                brandService.updateBrand(getToken(), updateBrandSDO).getPersistenceResponseBody()
            }
        )
    }

    /**
     * Função para deletar uma marca.
     *
     * @param localBrandId Id da marca que deseja deletar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun deleteBrand(localBrandId: UUID): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val deleteBrandSDO = DeleteBrandSDO(localBrandId)
                brandService.deleteBrand(getToken(), deleteBrandSDO).getResponseBody()
            }
        )
    }

    /**
     * Função que sincroniza as informações das Marcas, enviando quem foi criado e quem foi alterado
     * para a base remota.
     *
     * @param activeBrandsToSync Brands que deseja sincronizar.
     * @param activeProductsBrandsToSync ProductBrands que deseja sincronizar.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun syncBrands(activeBrandsToSync: List<Brand>, activeProductsBrandsToSync: List<ProductBrand>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val brandsDTOs = mutableListOf<NewBrandSDO>()

                for (brand in activeBrandsToSync) {
                    val productBrand = activeProductsBrandsToSync.find { it.brandId == brand.id }!!
                    brandsDTOs.add(NewBrandSDO(productBrand.id, productBrand.productId, brand.id, brand.name, productBrand.count))
                }

                brandService.syncBrands(getToken(), brandsDTOs).getResponseBody()
            }
        )
    }

    /**
     * Função para deletar 'N' brands.
     *
     * @param inactiveAndNotSynchronizedBrands Brands que deseja deletar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun deleteBrands(inactiveAndNotSynchronizedBrands: List<Brand>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val brandsDTOs = inactiveAndNotSynchronizedBrands.map { DeleteBrandSDO(it.id) }
                brandService.deleteBrands(getToken(), brandsDTOs).getResponseBody()
            }
        )
    }

    suspend fun findAllBrands(): ReadResponse<Brand> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val readResponse = brandService.findAllBrands(getToken()).getReadResponseBody()
                val values = readResponse.values.map { Brand(id = it.localBrandId, name = it.name, synchronized = true) }
                ReadResponse(values, readResponse.code, readResponse.success, readResponse.error)
            }
        )
    }

    suspend fun findAllProductBrands(): ReadResponse<ProductBrand> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val readResponse = brandService.findAllProductBrands(getToken()).getReadResponseBody()
                val values = readResponse.values.map {
                    ProductBrand(
                        id = it.localId,
                        productId = it.localProductId,
                        brandId = it.localBrandId,
                        count = it.count,
                        synchronized = true
                    )
                }
                ReadResponse(values, readResponse.code, readResponse.success, readResponse.error)
            }
        )
    }

}