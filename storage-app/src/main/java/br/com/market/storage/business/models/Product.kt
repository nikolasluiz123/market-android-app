package br.com.market.storage.business.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Uma classe que representa o Produto na base local.
 *
 * @property id Identificador da Entidade.
 * @property name Nome do Produto.
 * @property imageUrl Link da Imagem que será feito download para exibição.
 *
 * @author Nikolas Luiz Schmitt
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "",
    var imageUrl: String = "",
    override var synchronized: Boolean = false,
    override var active: Boolean = true
) : BaseModel()