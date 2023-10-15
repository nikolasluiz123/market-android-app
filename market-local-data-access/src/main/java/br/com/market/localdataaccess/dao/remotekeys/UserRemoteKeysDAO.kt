package br.com.market.localdataaccess.dao.remotekeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.keys.UserRemoteKeys

@Dao
interface UserRemoteKeysDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<UserRemoteKeys>)

    @Query("select * from user_remote_keys where id = :id")
    suspend fun getRemoteKeyByID(id: String): UserRemoteKeys?

    @Query("delete from user_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("select created_at from user_remote_keys order by created_at desc limit 1")
    suspend fun getCreationTime(): Long?
}