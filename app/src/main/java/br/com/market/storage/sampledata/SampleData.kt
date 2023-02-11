package br.com.market.storage.sampledata

import br.com.market.storage.models.Brand
import br.com.market.storage.models.Product

val sampleProducts = listOf(
    Product(
        id = 1,
        name = "Arroz",
        brands = listOf(
            Brand(id = 1, name = "Tio João"),
            Brand(id = 2, name = "Urbano"),
            Brand(id = 3, name = "Dalfovo"),
        ),
        imageUrl = "https://images.pexels.com/photos/4110251/pexels-photo-4110251.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
    ),
    Product(
        id = 1,
        name = "Feijão",
        brands = listOf(
            Brand(id = 4, name = "Camil"),
            Brand(id = 5, name = "Urbano"),
        ),
        imageUrl = "https://conteudo.imguol.com.br/c/entretenimento/51/2019/02/01/tipos-de-feijao-1549036437289_v2_450x337.jpg"
    )
)