package br.com.market.storage.pagination

import br.com.market.core.pagination.BasePagingSource
import br.com.market.domain.UserDomain
import br.com.market.localdataaccess.dao.UserDAO

class UserPagingSource(
    private val dao: UserDAO,
    private val name: String?
) : BasePagingSource<UserDomain>() {

    override suspend fun getData(limit: Int, offset: Int): List<UserDomain> {
        return dao.findUsers(name = name, limit = limit, offset = offset)
    }
}