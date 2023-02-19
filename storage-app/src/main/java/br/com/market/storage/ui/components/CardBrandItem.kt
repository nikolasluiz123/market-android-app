package br.com.market.storage.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.theme.tertiary
import br.com.market.storage.ui.domains.BrandDomain

@Composable
fun CardBrandItem(
    brandDomain: BrandDomain,
    onClick: () -> Unit = { }
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors(containerColor = tertiary)
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(8.dp)) {
            val (brandNameRef, countRef) = createRefs()

            Text(
                modifier = Modifier.constrainAs(brandNameRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                text = brandDomain.name
            )

            Text(
                modifier = Modifier.constrainAs(countRef) {
                    top.linkTo(brandNameRef.top)
                    end.linkTo(parent.end)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                text = brandDomain.count.toString()
            )
        }
    }
}

@Preview
@Composable
fun CardBrandItemLightPreview() {
    StorageTheme {
        Surface {
            CardBrandItem(BrandDomain(name = "Arroz Dalfovo", count = 4))
        }
    }
}

@Preview
@Composable
fun CardBrandItemDarkPreview() {
    StorageTheme {
        Surface {
            CardBrandItem(BrandDomain(name = "Arroz Dalfovo", count = 4))
        }
    }
}