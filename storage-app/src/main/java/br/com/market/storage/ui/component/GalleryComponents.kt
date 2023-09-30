package br.com.market.storage.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.market.domain.ProductImageDomain
import br.com.market.market.compose.components.gallery.HorizontalGallery

@Composable
fun ProductImageHorizontalGallery(
    modifier: Modifier = Modifier,
    images: MutableList<ProductImageDomain>,
    onLoadClick: () -> Unit,
    maxImages: Int = 3,
    onImageClick: (String) -> Unit,
    onDeleteButtonClick: (ByteArray, String?) -> Unit
) {
    HorizontalGallery(
        images = images.map { it.byteArray!! }.toMutableList(),
        onLoadClick = onLoadClick,
        maxImages = maxImages,
        modifier = modifier,
        onImageClick = { imageData ->
            images.find { it.byteArray == imageData }?.id?.let { id ->
                onImageClick(id)
            }
        },
        onDeleteButtonClick = { imageData ->
            val imageDomain = images.find { it.byteArray == imageData }

            onDeleteButtonClick(
                imageDomain?.byteArray!!,
                imageDomain.id
            )
        }
    )
}

