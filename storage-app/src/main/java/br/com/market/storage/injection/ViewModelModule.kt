package br.com.market.storage.injection

import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.UserDAO
import br.com.market.servicedataaccess.webclients.BrandWebClient
import br.com.market.servicedataaccess.webclients.CategoryWebClient
import br.com.market.servicedataaccess.webclients.UserWebClient
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.repository.UserRepository
import br.com.market.storage.repository.brand.BrandRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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
        brandDAO: BrandDAO,
        marketDAO: MarketDAO
    ): BrandRepository = BrandRepository(brandDAO, marketDAO, brandWebClient)

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
        userDAO: UserDAO,
        marketDAO: MarketDAO,
        userWebClient: UserWebClient
    ): UserRepository = UserRepository(userDAO, marketDAO, userWebClient)

    @Provides
    @ViewModelScoped
    fun provideCategoryRepository(
        categoryDAO: CategoryDAO,
        marketDAO: MarketDAO,
        categoryWebClient: CategoryWebClient
    ): CategoryRepository {
        return CategoryRepository(categoryDAO, marketDAO, categoryWebClient)
    }
}
