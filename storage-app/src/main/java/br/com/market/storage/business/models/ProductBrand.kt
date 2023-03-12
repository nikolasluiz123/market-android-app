package br.com.market.storage.business.models

import androidx.room.*

/**
 * Uma classe que representa uma tabela intermediária entre Produto e Marca.
 *
 * Ela é utilizada para armazenar dados que dependem de ambas as entidades.
 *
 * @property id Identificador da Entidade.
 * @property productId Id do Produto
 * @property brandId Id da Marca
 * @property count Quantidade que há no estoque de um Produto de uma Marca específica.
 *
 * @author Nikolas Luiz Schmitt
 */
@Entity(
    tableName = "products_brands",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = Brand::class,
            parentColumns = ["id"],
            childColumns = ["brand_id"]
        )
    ],
    indices = [Index(value = ["product_id"]), Index(value = ["brand_id"], unique = true)]
)
data class ProductBrand(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "product_id")
    var productId: Long? = null,
    @ColumnInfo(name = "brand_id")
    var brandId: Long? = null,
    var count: Int = 0,
    override var synchronized: Boolean = false,
    override var active: Boolean = true
): BaseModel()