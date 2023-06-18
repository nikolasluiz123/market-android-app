package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.User

@Dao
abstract class UserDAO : AbstractBaseDAO() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(users: List<User>)

    @Query("select * from users where email = :email")
    abstract suspend fun findUserByEmail(email: String): User?

    @Query("select * from users where synchronized = 0")
    abstract suspend fun findUsersNotSynchronized(): List<User>
}