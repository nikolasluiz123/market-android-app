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

    @Query("select * from addresses where id = :id")
    abstract suspend fun findById(id: String): Address
}