package br.com.market.storage.extensions

import br.com.market.storage.business.mappers.ProductMapper
import br.com.market.storage.business.models.Product
import br.com.market.storage.ui.domains.ProductDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

fun Flow<List<Product>>.toDomain(): Flow<List<ProductDomain>> = transform {
    return@transform emit(it.map(ProductMapper::toProductTO))
}