package br.com.market.storage.repository.base

import br.com.market.localdataaccess.dao.CompanyDAO
import kotlinx.coroutines.flow.first
import javax.inject.Inject

open class BaseRepository {

    @Inject
    protected lateinit var companyDAO: CompanyDAO

    suspend fun getCompanyId(): Long = companyDAO.findFirstCompany().first()?.id!!
}