package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.StorageOperationHistory

@Dao
abstract class StorageOperationsHistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(storageOperationHistory: StorageOperationHistory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(storageOperationsHistory: List<StorageOperationHistory>)

    @Query("select * from storage_operations_history where id = :id")
    abstract suspend fun findById(id: String): StorageOperationHistory

    @Query("select * from storage_operations_history where synchronized = 0")
    abstract suspend fun findStorageOperationsHistoryNotSynchronized(): List<StorageOperationHistory>
}