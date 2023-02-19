package br.com.market.storage.inection

import android.content.Context
import androidx.room.Room
import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.dao.ProductDAO
import br.com.market.storage.business.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideProductDAO(appDatabase: AppDatabase): ProductDAO {
        return appDatabase.productDAO()
    }

    @Provides
    fun provideBrandDAO(appDatabase: AppDatabase): BrandDAO {
        return appDatabase.brandDAO()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "storage.db"
        ).fallbackToDestructiveMigration().build()
    }

}