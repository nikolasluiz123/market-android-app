package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.Address

@Dao
abstract class AddressDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(address: Address)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveAll(address: List<Address>)

    @Query("select * from addresses where id = :id")
    abstract suspend fun findById(id: String): Address

    @Query("delete from addresses")
    abstract suspend fun clearAll()
}