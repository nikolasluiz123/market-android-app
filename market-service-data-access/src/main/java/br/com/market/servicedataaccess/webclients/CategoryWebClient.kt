package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Category
import br.com.market.sdo.category.CategorySDO
import br.com.market.servicedataaccess.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.extensions.getReadResponseBody
import br.com.market.servicedataaccess.extensions.getResponseBody
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.responses.ReadResponse
import br.com.market.servicedataaccess.services.CategoryService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CategoryWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: CategoryService
) : BaseWebClient(context) {

    suspend fun save(category: Category): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val categorySDO = CategorySDO(
                    name = category.name!!,
                    localCategoryId = category.id,
                    active = category.active
                )

                service.save(getToken(), categorySDO).getPersistenceResponseBody()
            }
        )
    }

    suspend fun toggleActive(category: Category): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val categorySDO = CategorySDO(
                    localCategoryId = category.id,
                    active = category.active
                )

                service.toggleActive(getToken(), categorySDO).getPersistenceResponseBody()
            }
        )
    }

    suspend fun sync(categories: List<Category>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val categorySDOs = categories.map {
                    CategorySDO(
                        localCategoryId = it.id,
                        name = it.name,
                        active = it.active
                    )
                }

                service.sync(getToken(), categorySDOs).getResponseBody()
            }
        )
    }

    suspend fun findAll(): ReadResponse<Category> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findAll(getToken()).getReadResponseBody()
                val categories = response.values.map {
                    Category(id = it.localCategoryId, name = it.name, synchronized = true, active = it.active)
                }

                ReadResponse(values = categories, code = response.code, success = response.success, error = response.error)
            }
        )
    }
}