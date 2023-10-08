package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.Market
import kotlinx.coroutines.flow.Flow

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
}