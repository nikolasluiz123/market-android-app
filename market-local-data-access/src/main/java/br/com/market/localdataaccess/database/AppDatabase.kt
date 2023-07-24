package br.com.market.localdataaccess.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import br.com.market.localdataaccess.converters.RoomTypeConverters
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.DeviceDAO
import br.com.market.localdataaccess.dao.MarketDAO
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
    version = 23,
    entities = [
        Address::class, Brand::class, Card::class, CartItem::class, Category::class, CategoryBrand::class, Client::class, Company::class,
        DeliveryMan::class, DeliveryManQueue::class, Product::class, ProductImage::class, ProductRating::class, PurchaseCart::class,
        StorageOperationHistory::class, ThemeDefinitions::class, User::class, Vehicle::class, VehicleCapacity::class, Device::class,
        Market::class
    ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 21, to = 22, spec = AutoMigrationSpec21To22::class),
        AutoMigration(from = 22, to = 23, spec = AutoMigrationSpec22To23::class)
    ]
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

    abstract fun marketDAO(): MarketDAO

    abstract fun addressDAO(): AddressDAO
}

@DeleteColumn(tableName = "theme_definitions", "synchronized")
@DeleteColumn(tableName = "theme_definitions", "active")
class AutoMigrationSpec21To22: AutoMigrationSpec

@DeleteColumn(tableName = "devices", "synchronized")
@DeleteColumn(tableName = "devices", "active")
@DeleteColumn(tableName = "markets", "active")
@DeleteColumn(tableName = "users", "synchronized")
@DeleteColumn(tableName = "users", "active")
class AutoMigrationSpec22To23: AutoMigrationSpec
