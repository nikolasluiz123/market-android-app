package br.com.market.servicedataaccess.injection

import br.com.market.servicedataaccess.services.IBrandService
import br.com.market.servicedataaccess.services.ICategoryService
import br.com.market.servicedataaccess.services.IUserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Módulo de injeção de dependências do Retrofit
 *
 * @author Nikolas Luiz Schmitt
 */
@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    /**
     * Função para fornecer uma instância única do retrofit.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("http://192.168.0.49:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Função para instanciar um BrandService.
     *
     * @param retrofit Instância do Retrofit
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    @Singleton
    fun provideBrandService(retrofit: Retrofit): IBrandService {
        return retrofit.create(IBrandService::class.java)
    }

    /**
     * Função para instanciar um UserService.
     *
     * @param retrofit Instância do Retrofit
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): IUserService {
        return retrofit.create(IUserService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryService(retrofit: Retrofit): ICategoryService {
        return retrofit.create(ICategoryService::class.java)
    }

}