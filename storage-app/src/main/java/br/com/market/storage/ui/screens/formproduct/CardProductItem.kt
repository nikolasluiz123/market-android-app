package br.com.market.storage.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.storage.R
import br.com.market.storage.sampledata.sampleProducts
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.ui.theme.*
import coil.compose.AsyncImage

@Composable
fun CardProductItem(product: ProductDomain, onClick: (Long) -> Unit = { }) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(product.id!!)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = colorCard)
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {

            val (imageRef, textRef) = createRefs()

            CoilImageViewer(
                modifier = Modifier
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .height(100.dp),
                data = product.imageUrl
            )

            Text(
                modifier = Modifier.constrainAs(textRef) {
                    top.linkTo(imageRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 4.dp)
                },
                text = product.name,
                color = colorPrimary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CardProductItemLightPreview() {
    StorageTheme {
        Surface {
            CardProductItem(product = sampleProducts[0])
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardProductItemDarkPreview() {
    StorageTheme {
        Surface {
            CardProductItem(product = sampleProducts[0])
        }
    }
}