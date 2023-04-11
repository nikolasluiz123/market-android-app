package br.com.market.storage.injection

import android.content.Context
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.servicedataaccess.webclients.BrandWebClient
import br.com.market.servicedataaccess.webclients.ProductWebClient
import br.com.market.servicedataaccess.webclients.UserWebClient
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Módulo de injeção de dependências do ViewModel.
 *
 * @author Nikolas Luiz Schmitt
 */
@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    /**
     * Função para instanciar um ProductRepository.
     *
     * @param context Contexto do APP
     * @param productDAO Classe de acesso a base local para operações referentes ao produto.
     * @param productWebClient Classe de acesso ao serviço para operações referentes ao produto.
     * @param brandDAO Classe de acesso a base local para operações referentes a marca.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    @ViewModelScoped
    fun provideProductRepository(
        @ApplicationContext context: Context,
        productDAO: ProductDAO,
        productWebClient: ProductWebClient,
        brandDAO: BrandDAO
    ): ProductRepository = ProductRepository(context, productDAO, productWebClient, brandDAO)

    /**
     * Função para instanciar um BrandRepository
     *
     * @param brandWebClient Classe de acesso ao serviço para operações referentes a marca.
     * @param brandDAO Classe de acesso a base local para operações referentes a marca.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    @ViewModelScoped
    fun provideBrandRepository(
        brandWebClient: BrandWebClient,
        brandDAO: BrandDAO
    ): BrandRepository = BrandRepository(brandWebClient, brandDAO)

    /**
     * Função para instanciar um UserRepository.
     *
     * @param context Contexto do APP.
     * @param userWebClient Classe de acesso ao serviço para operações referentes ao produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Provides
    @ViewModelScoped
    fun provideUserRepository(
        @ApplicationContext context: Context,
        userWebClient: UserWebClient
    ): UserRepository = UserRepository(context, userWebClient)

    @Provides
    @ViewModelScoped
    fun provideCategoryRepository(
        categoryDAO: CategoryDAO
    ): CategoryRepository {
        return CategoryRepository(categoryDAO)
    }
}
