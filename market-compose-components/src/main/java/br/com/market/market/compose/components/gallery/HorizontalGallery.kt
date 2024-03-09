package br.com.market.market.compose.components.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.CoilImageViewer
import coil.compose.SubcomposeAsyncImage

/**
 * Componente de galeria horizontal
 *
 * @param images Lista de imagens que vão ser exibidas. O tipo deve ser compatível com os que são aceitos pelo componente [SubcomposeAsyncImage]
 * @param onLoadClick Função executada ao clicar no botão 'Carregar', pode ser utilizado para exibir as opções de carregamento da imagem
 * @param modifier Modifier para definições de layout específicos
 * @param maxImages Número máximo de imagens aceito pelo componente
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun HorizontalGallery(
    images: MutableList<Any>,
    onLoadClick: () -> Unit,
    modifier: Modifier = Modifier,
    maxImages: Int = 3,
    onImageClick: (Any) -> Unit = { },
    onDeleteButtonClick: (Any) -> Unit = { },
    contentScale: ContentScale = ContentScale.Crop
) {
    ConstraintLayout(modifier) {
        val (buttonLoadRef, imageRef) = createRefs()
        val imageList = images.ifEmpty { listOf(R.drawable.placeholder, R.drawable.placeholder, R.drawable.placeholder) }.toMutableList()

        if (imageList.size < maxImages) {
            for (i in 1..maxImages) {
                if (i > imageList.size) {
                    imageList.add(R.drawable.placeholder)
                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .constrainAs(imageRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .fillMaxWidth()
        ) {
            items(imageList) {
                CoilImageViewer(
                    imageModifier = Modifier
                        .fillParentMaxWidth()
                        .height(250.dp)
                        .clickable { onImageClick(it) },
                    showDeleteButton = it !is Int,
                    onDeleteButtonClick = { onDeleteButtonClick(it) },
                    data = it,
                    contentScale = contentScale
                )
            }
        }

        Button(
            onClick = { onLoadClick() },
            modifier = Modifier.constrainAs(buttonLoadRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(imageRef.bottom)
                bottom.linkTo(imageRef.bottom)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_load_24dp),
                contentDescription = "Carregar Imagem"
            )
            Spacer(Modifier.size(8.dp))
            Text("Carregar", style = MaterialTheme.typography.bodyMedium)
        }
    }

}

@Preview
@Composable
fun HorizontalGalleryPreview() {
    MarketTheme {
        Surface {
            HorizontalGallery(
                images = mutableListOf(
                    R.drawable.imagem_grande_teste,
                    R.drawable.imagem_grande_teste,
                    R.drawable.imagem_grande_teste
                ),
                onLoadClick = { },
            )
        }
    }
}