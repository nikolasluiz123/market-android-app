package br.com.market.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.theme.BLUE_500
import br.com.market.core.theme.MarketTheme

@Composable
fun HorizontalGallery(
    images: List<Any>,
    onLoadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier) {
        val (buttonLoadRef, imageRef) = createRefs()

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
            items(images) {
                CoilImageViewer(
                    Modifier
                        .fillParentMaxWidth()
                        .height(250.dp),
                    data = it
                )
            }
        }

        Button(
            onClick = { onLoadClick() },
            colors = ButtonDefaults.buttonColors(containerColor = BLUE_500, contentColor = Color.White),
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
                images = listOf(
                    R.drawable.imagem_grande_teste,
                    R.drawable.imagem_grande_teste,
                    R.drawable.imagem_grande_teste
                ),
                onLoadClick = { }
            )
        }
    }
}