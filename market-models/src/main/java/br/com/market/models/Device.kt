package br.com.market.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "devices",
    foreignKeys = [
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        )
    ],
    indices = [Index(value = ["market_id"])]
)
data class Device(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "market_id")
    var marketId: Long? = null,
    var name: String? = null
)
