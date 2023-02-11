package br.com.market.storage.models

import kotlin.random.Random

data class Product(
    val id: Int = 0,
    val name: String = "",
    val brands: List<Brand> = emptyList(),
    val imageUrl: String = "",
    val count: Int = Random.nextInt(0, 50)
)