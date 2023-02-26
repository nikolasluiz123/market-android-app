package br.com.market.storage.injection

import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.dao.ProductDAO
import br.com.market.storage.business.repository.BrandRepository
import br.com.market.storage.business.repository.ProductRepository
import br.com.market.storage.business.webclient.BrandWebClient
import br.com.market.storage.business.webclient.ProductWebClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideProductRepository(
        productDAO: ProductDAO,
        brandDAO: BrandDAO
    ): ProductRepository = ProductRepository(productDAO, brandDAO)

    @Provides
    @ViewModelScoped
    fun provideBrandRepository(
        brandDAO: BrandDAO
    ): BrandRepository = BrandRepository(brandDAO)
}
