package br.com.market.localdataaccess.dao.remotekeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.keys.BrandRemoteKeys

@Dao
interface BrandRemoteKeysDAO{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<BrandRemoteKeys>)

    @Query("select * from brand_remote_keys where id = :id")
    suspend fun getRemoteKeyByID(id: String): BrandRemoteKeys?

    @Query("delete from brand_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("select created_at from brand_remote_keys order by created_at desc limit 1")
    suspend fun getCreationTime(): Long?
}