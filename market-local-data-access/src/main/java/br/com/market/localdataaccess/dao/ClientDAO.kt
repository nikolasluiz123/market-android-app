package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.Client

@Dao
abstract class ClientDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(client: Client)

    @Query("select * from client where id = :id")
    abstract suspend fun findById(id: String): Client
}