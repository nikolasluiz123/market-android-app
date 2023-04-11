package br.com.market.storage.sampledata

import br.com.market.domain.CategoryDomain
import br.com.market.domain.ProductDomain
import java.util.*


/**
 * Produtos de exemplo que são usados nos previews do compose.
 *
 * @author Nikolas Luiz Schmitt
 */
val sampleProducts = listOf(
    ProductDomain(
        id = UUID.randomUUID(),
        name = "Arroz",
        imageUrl = "https://images.pexels.com/photos/4110251/pexels-photo-4110251.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
    ),
    ProductDomain(
        id = UUID.randomUUID(),
        name = "Feijão",
        imageUrl = "https://conteudo.imguol.com.br/c/entretenimento/51/2019/02/01/tipos-de-feijao-1549036437289_v2_450x337.jpg"
    )
)

val sampleCategories = listOf(
    CategoryDomain(
        id = UUID.randomUUID(),
        name = "Biscoitos"
    ),
    CategoryDomain(
        id = UUID.randomUUID(),
        name = "Frutas"
    ),
    CategoryDomain(
        id = UUID.randomUUID(),
        name = "Massas"
    ),
    CategoryDomain(
        id = UUID.randomUUID(),
        name = "Grãos"
    ),
    CategoryDomain(
        id = UUID.randomUUID(),
        name = "Sucos"
    ),
    CategoryDomain(
        id = UUID.randomUUID(),
        name = "Refrigerantes"
    ),
    CategoryDomain(
        id = UUID.randomUUID(),
        name = "Carnes"
    ),
    CategoryDomain(
        id = UUID.randomUUID(),
        name = "Frios"
    )
)