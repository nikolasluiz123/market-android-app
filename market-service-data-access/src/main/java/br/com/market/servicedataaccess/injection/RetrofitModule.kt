package br.com.market.servicedataaccess.injection

import br.com.market.servicedataaccess.services.BrandService
import br.com.market.servicedataaccess.services.CategoryService
import br.com.market.servicedataaccess.services.ProductService
import br.com.market.servicedataaccess.services.UserService
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
     * Função para instanciar um ProductService.
     *
     * @param retrofit Instância do Retrofit
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    @Singleton
    fun provideProductService(retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
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
    fun provideBrandService(retrofit: Retrofit): BrandService {
        return retrofit.create(BrandService::class.java)
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
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryService(retrofit: Retrofit): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }

}