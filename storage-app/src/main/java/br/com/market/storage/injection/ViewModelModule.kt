package br.com.market.storage.injection

import android.content.Context
import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.dao.ProductDAO
import br.com.market.storage.business.repository.BrandRepository
import br.com.market.storage.business.repository.ProductRepository
import br.com.market.storage.business.repository.UserRepository
import br.com.market.storage.business.webclient.BrandWebClient
import br.com.market.storage.business.webclient.ProductWebClient
import br.com.market.storage.business.webclient.UserWebClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideProductRepository(
        @ApplicationContext context: Context,
        productDAO: ProductDAO,
        productWebClient: ProductWebClient,
        brandDAO: BrandDAO
    ): ProductRepository = ProductRepository(context, productDAO, productWebClient, brandDAO)

    @Provides
    @ViewModelScoped
    fun provideBrandRepository(
        brandWebClient: BrandWebClient,
        brandDAO: BrandDAO
    ): BrandRepository = BrandRepository(brandWebClient, brandDAO)

    @Provides
    @ViewModelScoped
    fun provideUserRepository(
        @ApplicationContext context: Context,
        userWebClient: UserWebClient
    ): UserRepository = UserRepository(context, userWebClient)
}
