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
import br.com.market.localdataaccess.dao.ClientDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.DeviceDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.StorageOperationsHistoryDAO
import br.com.market.localdataaccess.dao.UserDAO
import br.com.market.localdataaccess.dao.remotekeys.BrandRemoteKeysDAO
import br.com.market.localdataaccess.dao.remotekeys.CategoryRemoteKeysDAO
import br.com.market.localdataaccess.dao.remotekeys.MarketRemoteKeysDAO
import br.com.market.localdataaccess.dao.remotekeys.ProductRemoteKeysDAO
import br.com.market.localdataaccess.dao.remotekeys.UserRemoteKeysDAO
import br.com.market.localdataaccess.dao.report.StorageOperationsReportDAO
import br.com.market.models.Address
import br.com.market.models.Brand
import br.com.market.models.Card
import br.com.market.models.CartItem
import br.com.market.models.Category
import br.com.market.models.CategoryBrand
import br.com.market.models.Client
import br.com.market.models.Company
import br.com.market.models.DeliveryMan
import br.com.market.models.DeliveryManQueue
import br.com.market.models.Device
import br.com.market.models.Market
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.models.ProductRating
import br.com.market.models.PurchaseCart
import br.com.market.models.StorageOperationHistory
import br.com.market.models.ThemeDefinitions
import br.com.market.models.User
import br.com.market.models.Vehicle
import br.com.market.models.VehicleCapacity
import br.com.market.models.keys.BrandRemoteKeys
import br.com.market.models.keys.CategoryRemoteKeys
import br.com.market.models.keys.MarketRemoteKeys
import br.com.market.models.keys.ProductRemoteKeys
import br.com.market.models.keys.UserRemoteKeys

/**
 * Classe que representa a base de dados local do dispositivo.
 *
 * Nela são definidas as entidades, versão da base e podemos
 * recuperar os DAOs criados para acessar cada tabela.
 *
 * @author Nikolas Luiz Schmitt
 */
@Database(
    version = 25,
    entities = [
        Address::class, Brand::class, Card::class, CartItem::class, Category::class, CategoryBrand::class, Client::class, Company::class,
        DeliveryMan::class, DeliveryManQueue::class, Product::class, ProductImage::class, ProductRating::class, PurchaseCart::class,
        StorageOperationHistory::class, ThemeDefinitions::class, User::class, Vehicle::class, VehicleCapacity::class, Device::class,
        Market::class, CategoryRemoteKeys::class, BrandRemoteKeys::class, ProductRemoteKeys::class, MarketRemoteKeys::class,
        UserRemoteKeys::class
    ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 21, to = 22, spec = AutoMigrationSpec21To22::class),
        AutoMigration(from = 22, to = 23, spec = AutoMigrationSpec22To23::class),
        AutoMigration(from = 23, to = 24),
        AutoMigration(from = 24, to = 25)
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

    abstract fun storageOperationsHistoryDAO(): StorageOperationsHistoryDAO

    abstract fun userDAO(): UserDAO

    abstract fun deviceDAO(): DeviceDAO

    abstract fun companyDAO(): CompanyDAO

    abstract fun marketDAO(): MarketDAO

    abstract fun addressDAO(): AddressDAO

    abstract fun clientDAO(): ClientDAO

    abstract fun storageReportDAO(): StorageOperationsReportDAO

    abstract fun productRemoteKeysDAO(): ProductRemoteKeysDAO

    abstract fun categoryRemoteKeysDAO(): CategoryRemoteKeysDAO

    abstract fun brandRemoteKeysDAO(): BrandRemoteKeysDAO

    abstract fun marketRemoteKeysDAO(): MarketRemoteKeysDAO

    abstract fun userRemoteKeysDAO(): UserRemoteKeysDAO

}

@DeleteColumn(tableName = "theme_definitions", "synchronized")
@DeleteColumn(tableName = "theme_definitions", "active")
class AutoMigrationSpec21To22: AutoMigrationSpec

@DeleteColumn(tableName = "devices", "synchronized")
@DeleteColumn(tableName = "devices", "active")
@DeleteColumn(tableName = "markets", "active")
@DeleteColumn(tableName = "users", "synchronized")
@DeleteColumn(tableName = "users", "active")
@DeleteColumn(tableName = "client", columnName = "market_id")
class AutoMigrationSpec22To23: AutoMigrationSpec
