package br.com.market.localdataaccess.dao.remotekeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.keys.ProductRemoteKeys

@Dao
interface ProductRemoteKeysDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ProductRemoteKeys>)

    @Query("select * from product_remote_keys where id = :id")
    suspend fun getRemoteKeyByID(id: String): ProductRemoteKeys?

    @Query("delete from product_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("select created_at from product_remote_keys order by created_at desc limit 1")
    suspend fun getCreationTime(): Long?
}