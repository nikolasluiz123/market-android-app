package br.com.market.storage.ui.states

import br.com.market.domain.ProductImageDomain

data class ImageViewerUIState(
    val productName: String? = null,
    val productImageDomain: ProductImageDomain? = null
)
