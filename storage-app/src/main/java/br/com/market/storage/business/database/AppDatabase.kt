package br.com.market.storage.business.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.dao.ProductDAO
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.models.ProductBrand

@Database(
    version = 4,
    entities = [Product::class, Brand::class, ProductBrand::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDAO(): ProductDAO

    abstract fun brandDAO(): BrandDAO

}