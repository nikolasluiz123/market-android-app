package br.com.market.storage.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.market.storage.R
import br.com.market.storage.business.models.Product
import br.com.market.storage.sampledata.sampleProducts
import br.com.market.storage.ui.theme.StorageTheme
import coil.compose.AsyncImage

@Composable
fun CardProductItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = product.name,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CardProductItemLightPreview() {
    StorageTheme {
        Surface {
            CardProductItem(product =  sampleProducts[0])
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