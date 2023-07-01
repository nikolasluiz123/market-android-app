package br.com.market.servicedataaccess.injection

import br.com.market.servicedataaccess.responses.adapters.ByteArrayToBase64TypeAdapter
import br.com.market.servicedataaccess.responses.adapters.LocalDateTimeTypeAdapter
import br.com.market.servicedataaccess.responses.adapters.LocalDateTypeAdapter
import br.com.market.servicedataaccess.services.IBrandService
import br.com.market.servicedataaccess.services.ICategoryService
import br.com.market.servicedataaccess.services.ICompanyService
import br.com.market.servicedataaccess.services.IDeviceService
import br.com.market.servicedataaccess.services.IProductService
import br.com.market.servicedataaccess.services.IStorageOperationsHistoryService
import br.com.market.servicedataaccess.services.IUserService
import com.google.gson.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
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
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(ByteArray::class.java, ByteArrayToBase64TypeAdapter())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
            .create()

        return Retrofit
            .Builder()
            .baseUrl("http://192.168.0.49:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
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

    @Provides
    @Singleton
    fun provideProductService(retrofit: Retrofit): IProductService {
        return retrofit.create(IProductService::class.java)
    }

    @Provides
    @Singleton
    fun provideStorageOperationsHistoryService(retrofit: Retrofit): IStorageOperationsHistoryService {
        return retrofit.create(IStorageOperationsHistoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideCompanyService(retrofit: Retrofit): IDeviceService {
        return retrofit.create(IDeviceService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeviceService(retrofit: Retrofit): ICompanyService {
        return retrofit.create(ICompanyService::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

}