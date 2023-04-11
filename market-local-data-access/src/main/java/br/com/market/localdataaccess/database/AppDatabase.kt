package br.com.market.localdataaccess.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.market.localdataaccess.converters.RoomTypeConverters
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.models.*

/**
 * Classe que representa a base de dados local do dispositivo.
 *
 * Nela são definidas as entidades, versão da base e podemos
 * recuperar os DAOs criados para acessar cada tabela.
 *
 * @author Nikolas Luiz Schmitt
 */
@Database(
    version = 9,
    entities = [
        Address::class, Brand::class, Card::class, CartItem::class, Category::class, CategoryBrand::class, Client::class, Company::class,
        DeliveryMan::class, DeliveryManQueue::class, Product::class, ProductImage::class, ProductRating::class, PurchaseCart::class,
        StorageOperationsHistory::class, StorageProduct::class, ThemeDefinitions::class, User::class, Vehicle::class, VehicleCapacity::class
    ],
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
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

    abstract fun categoryDAO(): CategoryDAO

}