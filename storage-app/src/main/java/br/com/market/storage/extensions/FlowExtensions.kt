package br.com.market.storage.extensions

import br.com.market.storage.business.models.Product
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.utils.TransformClassHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

fun Flow<List<Product>>.toProductDomainList(): Flow<List<ProductDomain>> = transform {
    return@transform emit(it.map { product ->
        val domain = ProductDomain()
        TransformClassHelper.modelToDomain(product, domain)
        domain
    })
}

fun Flow<Product?>.toProductDomain(): Flow<ProductDomain?> = transform {
    return@transform it?.let {
        val domain = ProductDomain()
        TransformClassHelper.modelToDomain(it, domain)
        emit(domain)
    } ?: emit(null)
}