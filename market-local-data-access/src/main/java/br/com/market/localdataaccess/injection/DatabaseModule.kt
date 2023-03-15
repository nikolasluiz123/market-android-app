package br.com.market.localdataaccess.injection

import android.content.Context
import androidx.room.Room
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.ProductDAO
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
     * função para instanciar um ProductDAO
     *
     * @param appDatabase DataBase do Room
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    fun provideProductDAO(appDatabase: AppDatabase): ProductDAO {
        return appDatabase.productDAO()
    }

    /**
     * Função para instanciar um BrandDAO
     *
     * @param appDatabase DataBase do Room
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    fun provideBrandDAO(appDatabase: AppDatabase): BrandDAO {
        return appDatabase.brandDAO()
    }

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
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "storage.db"
        ).fallbackToDestructiveMigration().build()
    }

}