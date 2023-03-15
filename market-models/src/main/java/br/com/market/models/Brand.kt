package br.com.market.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Uma classe que representa uma Marca na base local.
 *
 * @property id Identificador da Entidade.
 * @property name Nome da Marca.
 *
 * @author Nikolas Luiz Schmitt
 */
@Entity(tableName = "brands")
data class Brand(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var name: String = "",
    override var synchronized: Boolean = false,
    override var active: Boolean = true
): BaseModel()
