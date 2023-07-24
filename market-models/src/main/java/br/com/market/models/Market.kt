package br.com.market.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "markets",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        ),
        ForeignKey(
            entity = Address::class,
            parentColumns = ["id"],
            childColumns = ["address_id"]
        )
    ],
    indices = [Index(value = ["company_id"]), Index(value = ["address_id"])]
)
data class Market(
    @PrimaryKey
    var id: Long? = null,
    @ColumnInfo("company_id")
    var companyId: Long? = null,
    @ColumnInfo("address_id")
    var addressId: String? = null,
    var name: String? = null
)