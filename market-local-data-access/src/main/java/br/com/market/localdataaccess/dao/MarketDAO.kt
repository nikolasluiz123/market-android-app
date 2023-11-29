package br.com.market.localdataaccess.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import br.com.market.core.filter.base.BaseSearchFilter
import br.com.market.domain.MarketLovDomain
import br.com.market.models.Brand
import br.com.market.models.Market
import kotlinx.coroutines.flow.Flow
import java.util.StringJoiner

@Dao
abstract class MarketDAO: AbstractBaseDAO() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(market: Market)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveAll(markets: List<Market>)

    @Query("select * from markets limit 1")
    abstract fun findFirst(): Flow<Market?>

    @Query("delete from markets")
    abstract suspend fun clearAll()

    fun findMarketsLov(filter: BaseSearchFilter): PagingSource<Int, MarketLovDomain> {
        val params = mutableListOf<Any>()

        val select = StringJoiner("\r\n")
        with(select) {
            add(" select m.name as name, ")
            add("        a.public_place || ' ' || a.number as completeAddress ")
        }

        val from = StringJoiner("\r\n")
        with(from) {
            add(" from markets m ")
            add(" inner join addresses a on a.id = m.address_id ")
        }

        val where = StringJoiner("\r\n")
        where.add(" where m.active ")

        if (!filter.simpleFilter.isNullOrBlank()) {
            where.add(" and m.name like ? ")
            params.add("%${filter.simpleFilter}%")
        }

        val orderBy = StringJoiner("\r\n")
        orderBy.add(" order by m.name ")

        val sql = StringJoiner("\r\n")
        sql.add(select.toString())
        sql.add(from.toString())
        sql.add(where.toString())
        sql.add(orderBy.toString())

        return executeQueryFindMarketsLov(SimpleSQLiteQuery(sql.toString(), params.toTypedArray()))
    }

    @RawQuery(observedEntities = [Brand::class])
    abstract fun executeQueryFindMarketsLov(query: SupportSQLiteQuery): PagingSource<Int, MarketLovDomain>
}