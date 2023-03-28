package br.com.market.storage.extensions

/**
 * Função para converter um Flow de Lista de Modelo para um Flow de Lista de Domínio.
 *
 * Foi implmentada específicamente para Produto.
 *
 * @author Nikolas Luiz Schmitt
 */
//fun Flow<List<Product2>>.toProductDomainList(): Flow<List<ProductDomain>> = transform {
//    return@transform emit(it.map { product ->
//        val domain = ProductDomain()
//        TransformClassHelper.modelToDomain(product, domain)
//        domain
//    })
//}

/**
 * Função para converter um Flow de Modelo para Domínio.
 *
 * Foi implmentada específicamente para Produto.
 *
 * @author Nikolas Luiz Schmitt
 */
//fun Flow<Product2?>.toProductDomain(): Flow<ProductDomain> = transform {
//    val domain = ProductDomain()
//    it?.let {
//        TransformClassHelper.modelToDomain(it, domain)
//        emit(domain)
//    }
//}