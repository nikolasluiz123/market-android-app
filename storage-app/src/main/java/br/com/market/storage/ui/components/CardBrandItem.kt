package br.com.market.storage.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.storage.ui.domains.ProductBrandDomain
import br.com.market.storage.ui.theme.*

@Composable
fun CardBrandItem(
    productBrandDomain: ProductBrandDomain,
    onClick: () -> Unit = { }
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = colorCard)
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val (brandLabelRef, brandNameRef, countRef, countLabelRef) = createRefs()

            createHorizontalChain(brandLabelRef, countLabelRef)
            createHorizontalChain(brandNameRef, countRef)

            Text(
                modifier = Modifier.constrainAs(brandLabelRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)

                    horizontalChainWeight = 0.7F

                    width = Dimension.fillToConstraints
                }.padding(end = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = colorPrimary,
                text = "Produto"
            )

            Text(
                modifier = Modifier.constrainAs(brandNameRef) {
                    top.linkTo(brandLabelRef.bottom)
                    start.linkTo(brandLabelRef.start)

                    horizontalChainWeight = 0.7F

                    width = Dimension.fillToConstraints
                }.padding(end = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                color = colorPrimary,
                text = "${productBrandDomain.productName} ${productBrandDomain.brandName}"
            )

            Text(
                modifier = Modifier.constrainAs(countLabelRef) {
                    top.linkTo(brandLabelRef.top)
                    start.linkTo(brandLabelRef.end)
                    end.linkTo(parent.end)

                    horizontalChainWeight = 0.3F

                    width = Dimension.fillToConstraints
                },
                style = MaterialTheme.typography.bodyMedium,
                color = colorPrimary,
                text = "Quantidade"
            )

            Text(
                modifier = Modifier.constrainAs(countRef) {
                    top.linkTo(countLabelRef.bottom)
                    end.linkTo(countLabelRef.end)

                    horizontalChainWeight = 0.3F

                    width = Dimension.fillToConstraints
                },
                style = MaterialTheme.typography.titleMedium,
                color = colorPrimary,
                text = productBrandDomain.count.toString()
            )
        }
    }
}

@Preview
@Composable
fun CardBrandItemPreview() {
    StorageTheme {
        Surface {
            CardBrandItem(ProductBrandDomain(productName = "Arroz", brandName = "Dalfovo", count = 4))
        }
    }
}