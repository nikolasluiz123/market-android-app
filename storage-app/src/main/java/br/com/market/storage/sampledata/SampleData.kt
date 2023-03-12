package br.com.market.storage.sampledata

import br.com.market.storage.ui.domains.ProductDomain

/**
 * Produtos de exemplo que são usados nos previews do compose.
 *
 * @author Nikolas Luiz Schmitt
 */
val sampleProducts = listOf(
    ProductDomain(
        id = 1,
        name = "Arroz",
        imageUrl = "https://images.pexels.com/photos/4110251/pexels-photo-4110251.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
    ),
    ProductDomain(
        id = 1,
        name = "Feijão",
        imageUrl = "https://conteudo.imguol.com.br/c/entretenimento/51/2019/02/01/tipos-de-feijao-1549036437289_v2_450x337.jpg"
    )
)