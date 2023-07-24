package br.com.market.models

import androidx.room.*
import br.com.market.models.base.BaseModel
import java.util.*

@Entity(tableName = "addresses")
data class Address(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var state: String? = null,
    var city: String? = null,
    @ColumnInfo("public_place")
    var publicPlace: String? = null,
    var number: String? = null,
    var complement: String? = null,
    var cep: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true
) : BaseModel()
