package br.com.market.models

import androidx.room.*
import java.util.*

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        )
    ],
    indices = [Index(value = ["market_id"])]
)
data class User(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var token: String? = null,
    @ColumnInfo("market_id")
    var marketId: Long? = null
)