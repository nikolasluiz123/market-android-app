package br.com.market.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

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
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var name: String = "",
    var imageUrl: String = "",
    override var synchronized: Boolean = false,
    override var active: Boolean = true
) : BaseModel()