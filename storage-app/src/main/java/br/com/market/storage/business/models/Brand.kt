package br.com.market.storage.business.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "",
    override var synchronized: Boolean = false,
    override var active: Boolean = true
): BaseModel()
