package br.com.market.models

import androidx.room.*
import br.com.market.models.base.MarketModel
import java.util.*

@Entity(
    tableName = "client",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"]
        ),
        ForeignKey(
            entity = Address::class,
            parentColumns = ["id"],
            childColumns = ["address_id"]
        ),
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        )
    ],
    indices = [Index(value = ["user_id"]), Index(value = ["address_id"]), Index(value = ["market_id"])]
)
data class Client(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    val cpf: String? = null,
    @ColumnInfo("user_id")
    var userId: String? = null,
    @ColumnInfo("address_id")
    var addressId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("market_id")
    override var marketId: Long? = null
): MarketModel()