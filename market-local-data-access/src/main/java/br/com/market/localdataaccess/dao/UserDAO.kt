package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import br.com.market.domain.UserDomain
import br.com.market.models.Brand
import br.com.market.models.User
import java.util.StringJoiner

@Dao
abstract class UserDAO : AbstractBaseDAO() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveUsers(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveUsers(users: List<User>)

    @Query("select * from users where email = :email")
    abstract suspend fun findUserByEmail(email: String): User?

    suspend fun findUsers(limit: Int, offset: Int, name: String? = null): List<UserDomain> {
        val params = mutableListOf<Any>()

        val select = StringJoiner("\r\n")
        select.add(" select u.* ")

        val from = StringJoiner("\r\n")
        from.add(" from users u ")

        val where = StringJoiner("\r\n")
        where.add(" where 1=1 ")

        if (!name.isNullOrEmpty()) {
            where.add(" and u.name like '%' || ? || '%'")
            params.add(name)
        }

        val orderBy = StringJoiner("\r\n")
        orderBy.add(" order by u.name ")
        orderBy.add(" limit ? offset ? ")

        params.add(limit)
        params.add(offset)

        val sql = StringJoiner("\r\n")
        sql.add(select.toString())
        sql.add(from.toString())
        sql.add(where.toString())
        sql.add(orderBy.toString())

        return executeQueryFindUser(SimpleSQLiteQuery(sql.toString(), params.toTypedArray()))
    }

    @RawQuery(observedEntities = [Brand::class])
    abstract suspend fun executeQueryFindUser(query: SupportSQLiteQuery): List<UserDomain>

    @Query("delete from users")
    abstract suspend fun clearAll()
}