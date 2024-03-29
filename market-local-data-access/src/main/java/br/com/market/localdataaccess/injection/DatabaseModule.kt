package br.com.market.localdataaccess.injection

import android.content.Context
import androidx.room.Room
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
import br.com.market.localdataaccess.dao.remotekeys.StorageOperationsHistoryRemoteKeysDAO
import br.com.market.localdataaccess.dao.remotekeys.UserRemoteKeysDAO
import br.com.market.localdataaccess.dao.report.StorageOperationsReportDAO
import br.com.market.localdataaccess.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Modulo de injeção de dependências do Room
 *
 * @author Nikolas Luiz Schmitt
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    /**
     * Função para instanciar um [BrandDAO]
     *
     * @param appDatabase DataBase do Room
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    fun provideBrandDAO(appDatabase: AppDatabase): BrandDAO = appDatabase.brandDAO()

    /**
     * Função para instanciar um [CategoryDAO]
     *
     * @param appDatabase DataBase do Room
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    fun provideCategoryDAO(appDatabase: AppDatabase): CategoryDAO = appDatabase.categoryDAO()

    @Provides
    fun provideProductDAO(appDatabase: AppDatabase): ProductDAO = appDatabase.productDAO()

    @Provides
    fun provideStorageOperationsHistoryDAO(appDatabase: AppDatabase): StorageOperationsHistoryDAO = appDatabase.storageOperationsHistoryDAO()

    @Provides
    fun provideUserDAO(appDatabase: AppDatabase): UserDAO = appDatabase.userDAO()

    @Provides
    @Singleton
    fun provideDeviceDAO(appDatabase: AppDatabase): DeviceDAO = appDatabase.deviceDAO()

    @Provides
    @Singleton
    fun provideCompanyDAO(appDatabase: AppDatabase): CompanyDAO = appDatabase.companyDAO()

    @Provides
    @Singleton
    fun provideMarketDAO(appDatabase: AppDatabase): MarketDAO = appDatabase.marketDAO()

    @Provides
    @Singleton
    fun provideAddressDAO(appDatabase: AppDatabase): AddressDAO = appDatabase.addressDAO()

    @Provides
    @Singleton
    fun provideClientDAO(appDatabase: AppDatabase): ClientDAO = appDatabase.clientDAO()

    @Provides
    @Singleton
    fun provideStorageReportDAO(appDatabase: AppDatabase): StorageOperationsReportDAO = appDatabase.storageReportDAO()

    @Provides
    @Singleton
    fun provideProductRemoteKeysDAO(appDatabase: AppDatabase): ProductRemoteKeysDAO = appDatabase.productRemoteKeysDAO()

    @Provides
    @Singleton
    fun provideCategoryRemoteKeysDAO(appDatabase: AppDatabase): CategoryRemoteKeysDAO = appDatabase.categoryRemoteKeysDAO()

    @Provides
    @Singleton
    fun provideBrandRemoteKeysDAO(appDatabase: AppDatabase): BrandRemoteKeysDAO = appDatabase.brandRemoteKeysDAO()

    @Provides
    @Singleton
    fun provideMarketRemoteKeysDAO(appDatabase: AppDatabase): MarketRemoteKeysDAO = appDatabase.marketRemoteKeysDAO()

    @Provides
    @Singleton
    fun provideUserRemoteKeysDAO(appDatabase: AppDatabase): UserRemoteKeysDAO = appDatabase.userRemoteKeysDAO()

    @Provides
    @Singleton
    fun provideStorageOperationsHistoryRemoteKeysDAO(appDatabase: AppDatabase): StorageOperationsHistoryRemoteKeysDAO = appDatabase.storageOperationsHistoryRemoteKeysDAO()

    /**
     * função para criar o DataBase uma única vez.
     *
     * @param context Context do APP
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "market.db").build()
    }
}