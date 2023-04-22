package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Category
import br.com.market.sdo.category.CategorySDO
import br.com.market.servicedataaccess.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.services.CategoryService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CategoryWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryService: CategoryService
) : BaseWebClient(context) {

    suspend fun save(category: Category): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val categorySDO = CategorySDO(
                    name = category.name!!,
                    localCategoryId = category.id,
                    active = category.active
                )

                categoryService.save(getToken(), categorySDO).getPersistenceResponseBody()
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

                categoryService.toggleActive(getToken(), categorySDO).getPersistenceResponseBody()
            }
        )
    }

}