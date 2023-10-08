package br.com.market.localdataaccess.dao.remotekeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.ProductRemoteKeys

@Dao
abstract class ProductRemoteKeysDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(remoteKey: List<ProductRemoteKeys>)

    @Query("select * from product_remote_keys where id = :id")
    abstract suspend fun getRemoteKeyByID(id: String): ProductRemoteKeys?

    @Query("Delete From product_remote_keys")
    abstract suspend fun clearRemoteKeys()

    @Query("Select created_at From product_remote_keys order by created_at DESC LIMIT 1")
    abstract suspend fun getCreationTime(): Long?
}