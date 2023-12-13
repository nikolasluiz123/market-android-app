package br.com.market.commerce.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.market.domain.ProductImageReadDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.remotekeys.ProductRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.models.Address
import br.com.market.models.Brand
import br.com.market.models.Category
import br.com.market.models.CategoryBrand
import br.com.market.models.Company
import br.com.market.models.Market
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.models.ThemeDefinitions
import br.com.market.models.keys.ProductRemoteKeys
import br.com.market.sdo.ProductClientSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.webclients.ProductWebClient
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class ProductsRemoteMediator(
    private val database: AppDatabase,
    private val webClient: ProductWebClient,
    private val categoryDAO: CategoryDAO,
    private val brandDAO: BrandDAO,
    private val marketDAO: MarketDAO,
    private val companyDAO: CompanyDAO,
    private val productDAO: ProductDAO,
    private val remoteKeysDAO: ProductRemoteKeysDAO,
    private val addressDAO: AddressDAO,
    private val simpleFilter: String?
) : RemoteMediator<Int, ProductImageReadDomain>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES)

        return if (System.currentTimeMillis() - (remoteKeysDAO.getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ProductImageReadDomain>): MediatorResult {
        return try {
            val page: Int = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 0
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val limit = state.config.pageSize
            val offset = page * limit
            val response = webClient.findProductsForSell(simpleFilter, limit, offset)
            val endOfPaginationReached = response.values.isEmpty()

            if (response.success) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        remoteKeysDAO.clearRemoteKeys()
                        productDAO.clearAll()
                        brandDAO.clearAll()
                        categoryDAO.clearAll()
                        marketDAO.clearAll()
                        addressDAO.clearAll()
                        companyDAO.clearAll()
                    }

                    val prevKey = if (page > 1) page - 1 else null
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val remoteKeys = response.values.map {
                        ProductRemoteKeys(id = it.localId, prevKey = prevKey, currentPage = page, nextKey = nextKey)
                    }

                    remoteKeysDAO.insertAll(remoteKeys)

                    val themes = getThemesFrom(response)
                    companyDAO.saveThemes(themes)

                    val companies = getCompaniesFrom(response)
                    companyDAO.saveCompanies(companies)

                    val addresses = getAddressesFrom(response)
                    addressDAO.saveAll(addresses)

                    val markets = getMarketsFrom(response)
                    marketDAO.saveAll(markets)

                    val categories = getCategoriesFrom(response)
                    categoryDAO.save(categories)

                    val brands = getBrandsFrom(response)
                    brandDAO.saveBrands(brands)

                    val categoryBrands = getCategoryBrandsFrom(response)
                    brandDAO.saveCategoryBrands(categoryBrands)

                    val products = getProductsFrom(response)
                    val images = getImagesFrom(response)
                    productDAO.saveProductsAndImages(products, images)
                }
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ProductImageReadDomain>): ProductRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDAO.getRemoteKeyByID(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ProductImageReadDomain>): ProductRemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { product ->
            remoteKeysDAO.getRemoteKeyByID(product.id!!)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ProductImageReadDomain>): ProductRemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { product ->
            remoteKeysDAO.getRemoteKeyByID(product.id!!)
        }
    }

    private fun getThemesFrom(response: ReadResponse<ProductClientSDO>): List<ThemeDefinitions> =
        response.values.map {
            ThemeDefinitions(
                id = it.company.themeDefinitions.id,
                colorPrimary = it.company.themeDefinitions.colorPrimary,
                colorSecondary = it.company.themeDefinitions.colorSecondary,
                colorTertiary = it.company.themeDefinitions.colorTertiary,
                imageLogo = it.company.themeDefinitions.imageLogo
            )
        }

    private fun getCompaniesFrom(response: ReadResponse<ProductClientSDO>): List<Company> =
        response.values.map {
            Company(
                id = it.company.id,
                name = it.company.name,
                themeDefinitionsId = it.company.themeDefinitions.id
            )
        }

    private fun getAddressesFrom(response: ReadResponse<ProductClientSDO>): List<Address> =
        response.values.map {
            Address(
                id = it.market.address?.localId!!,
                state = it.market.address?.state,
                city = it.market.address?.city,
                publicPlace = it.market.address?.publicPlace,
                number = it.market.address?.number,
                complement = it.market.address?.complement,
                cep = it.market.address?.cep,
                synchronized = true
            )
        }

    private fun getMarketsFrom(response: ReadResponse<ProductClientSDO>): List<Market> =
        response.values.map {
            Market(
                id = it.market.id,
                companyId = it.market.companyId,
                addressId = it.market.address?.localId,
                name = it.market.name
            )
        }

    private fun getCategoriesFrom(response: ReadResponse<ProductClientSDO>): List<Category> =
        response.values.map {
            Category(
                id = it.category.localId,
                name = it.category.name,
                synchronized = true,
                active = it.category.active,
                marketId = it.category.marketId
            )
        }

    private fun getBrandsFrom(response: ReadResponse<ProductClientSDO>): List<Brand> =
        response.values.map {
            Brand(
                id = it.brand.localId,
                name = it.brand.name,
                synchronized = true,
                active = it.brand.active,
                marketId = it.brand.marketId
            )
        }

    private fun getCategoryBrandsFrom(response: ReadResponse<ProductClientSDO>): List<CategoryBrand> =
        response.values.map {
            CategoryBrand(
                id = it.categoryBrand.localId,
                categoryId = it.categoryBrand.localCategoryId,
                brandId = it.categoryBrand.localBrandId,
                synchronized = true,
                marketId = it.categoryBrand.marketId
            )
        }

    private fun getProductsFrom(response: ReadResponse<ProductClientSDO>) =
        response.values.map {
            Product(
                id = it.localId,
                name = it.name,
                price = it.price,
                quantity = it.quantity,
                quantityUnit = it.quantityUnit,
                categoryBrandId = it.categoryBrand.localId,
                synchronized = true,
                active = it.active,
                marketId = it.marketId
            )
        }

    private fun getImagesFrom(response: ReadResponse<ProductClientSDO>): List<ProductImage> =
        response.values.map {
            ProductImage(
                id = it.image.localId,
                bytes = it.image.bytes,
                imageUrl = null,
                productId = it.image.productLocalId,
                principal = it.image.principal,
                synchronized = true,
                active = it.active,
                marketId = it.image.marketId
            )
        }
}