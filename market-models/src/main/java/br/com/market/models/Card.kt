package br.com.market.models

import androidx.room.*
import br.com.market.models.base.MarketModel
import java.time.LocalDate
import java.util.*

@Entity(
    tableName = "card",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["client_id"]
        ),
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        )
    ],
    indices = [Index(value = ["client_id"]), Index(value = ["market_id"])]
)
data class Card(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var accaount: String? = null,
    var agency: String? = null,
    var bank: String? = null,
    var validity: LocalDate? = null,
    @ColumnInfo("client_id")
    var clientId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("market_id")
    override var marketId: Long? = null
) : MarketModel()