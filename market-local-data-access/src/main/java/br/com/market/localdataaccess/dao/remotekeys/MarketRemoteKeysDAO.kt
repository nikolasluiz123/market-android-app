package br.com.market.localdataaccess.dao.remotekeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.keys.MarketRemoteKeys

@Dao
interface MarketRemoteKeysDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<MarketRemoteKeys>)

    @Query("select * from market_remote_keys where id = :id")
    suspend fun getRemoteKeyByID(id: String): MarketRemoteKeys?

    @Query("delete from market_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("select created_at from market_remote_keys order by created_at desc limit 1")
    suspend fun getCreationTime(): Long?
}