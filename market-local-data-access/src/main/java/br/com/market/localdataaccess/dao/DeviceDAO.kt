package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.Device
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DeviceDAO : AbstractBaseDAO() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(device: Device)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveDevices(devices: List<Device>)

    @Query("select * from devices limit 1")
    abstract fun findFirst(): Flow<Device?>

    @Query("delete from devices")
    abstract suspend fun clearAll()

}