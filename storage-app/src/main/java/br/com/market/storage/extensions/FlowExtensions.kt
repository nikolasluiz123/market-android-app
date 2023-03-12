package br.com.market.storage.extensions

import br.com.market.storage.business.models.Product
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.utils.TransformClassHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

/**
 * Função para converter um Flow de Lista de Modelo para um Flow de Lista de Domínio.
 *
 * Foi implmentada específicamente para Produto.
 *
 * @author Nikolas Luiz Schmitt
 */
fun Flow<List<Product>>.toProductDomainList(): Flow<List<ProductDomain>> = transform {
    return@transform emit(it.map { product ->
        val domain = ProductDomain()
        TransformClassHelper.modelToDomain(product, domain)
        domain
    })
}

/**
 * Função para converter um Flow de Modelo para Domínio.
 *
 * Foi implmentada específicamente para Produto.
 *
 * @author Nikolas Luiz Schmitt
 */
fun Flow<Product?>.toProductDomain(): Flow<ProductDomain?> = transform {
    return@transform it?.let {
        val domain = ProductDomain()
        TransformClassHelper.modelToDomain(it, domain)
        emit(domain)
    } ?: emit(null)
}