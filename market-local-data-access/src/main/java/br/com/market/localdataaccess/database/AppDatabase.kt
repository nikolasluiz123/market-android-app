package br.com.market.localdataaccess.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import br.com.market.localdataaccess.converters.RoomTypeConverters
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.DeviceDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.ProductImageDAO
import br.com.market.localdataaccess.dao.StorageOperationsHistoryDAO
import br.com.market.localdataaccess.dao.UserDAO
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
    version = 20,
    entities = [
        Address::class, Brand::class, Card::class, CartItem::class, Category::class, CategoryBrand::class, Client::class, Company::class,
        DeliveryMan::class, DeliveryManQueue::class, Product::class, ProductImage::class, ProductRating::class, PurchaseCart::class,
        StorageOperationHistory::class, ThemeDefinitions::class, User::class, Vehicle::class, VehicleCapacity::class, Device::class
    ],
    autoMigrations = [
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13),
        AutoMigration(from = 13, to = 14),
        AutoMigration(from = 15, to = 16),
        AutoMigration(from = 16, to = 17, spec = AutoMigrationSpec16TO17::class),
        AutoMigration(from = 17, to = 18),
        AutoMigration(from = 18, to = 19),
        AutoMigration(from = 19, to = 20)
    ],
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Função responsável por retornar um [BrandDAO]
     *
     * @author Nikolas Luiz Schmitt
     */
    abstract fun brandDAO(): BrandDAO

    /**
     * Função responsável por retornar um [CategoryDAO]
     *
     * @author Nikolas Luiz Schmitt
     */
    abstract fun categoryDAO(): CategoryDAO

    abstract fun productDAO(): ProductDAO

    abstract fun productImageDAO(): ProductImageDAO

    abstract fun storageOperationsHistoryDAO(): StorageOperationsHistoryDAO

    abstract fun userDAO(): UserDAO

    abstract fun deviceDAO(): DeviceDAO

    abstract fun companyDAO(): CompanyDAO
}
@DeleteColumn(tableName = "users", columnName = "logged")
class AutoMigrationSpec16TO17 : AutoMigrationSpec