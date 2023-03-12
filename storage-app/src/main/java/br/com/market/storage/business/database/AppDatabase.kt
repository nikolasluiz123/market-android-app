package br.com.market.storage.business.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.dao.ProductDAO
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.models.ProductBrand

/**
 * Classe que representa a base de dados local do dispositivo.
 *
 * Nela são definidas as entidades, versão da base e podemos
 * recuperar os DAOs criados para acessar cada tabela.
 *
 * @author Nikolas Luiz Schmitt
 */
@Database(
    version = 6,
    entities = [Product::class, Brand::class, ProductBrand::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Função responsável por retornar um ProductDAO
     *
     * @author Nikolas Luiz Schmitt
     */
    abstract fun productDAO(): ProductDAO

    /**
     * Função responsável por retornar um BrandDAO
     *
     * @author Nikolas Luiz Schmitt
     */
    abstract fun brandDAO(): BrandDAO

}