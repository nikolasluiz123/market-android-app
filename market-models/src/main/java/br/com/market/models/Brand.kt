package br.com.market.models

import androidx.room.*
import br.com.market.models.base.MarketModel
import java.util.*

/**
 * Uma classe que representa uma Marca na base local.
 *
 * @property name Nome da Marca.
 *
 * @author Nikolas Luiz Schmitt
 */
@Entity(
    tableName = "brands",
    foreignKeys = [
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        )
    ],
    indices = [Index(value = ["market_id"])]
)
data class Brand(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var name: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("market_id")
    override var marketId: Long? = null
) : MarketModel()
