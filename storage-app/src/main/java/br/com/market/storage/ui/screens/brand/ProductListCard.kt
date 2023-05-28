package br.com.market.storage.ui.screens.brand

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.extensions.formatToCurrency
import br.com.market.core.theme.MarketTheme
import br.com.market.core.theme.colorCardActive
import br.com.market.core.theme.colorCardInactive
import br.com.market.core.ui.components.CoilImageViewer
import br.com.market.enums.EnumUnit
import br.com.market.storage.extensions.formatQuantityIn

@Composable
fun ProductListCard(
    name: String,
    price: Double,
    quantity: Double,
    quantityUnit: EnumUnit,
    image: Any,
    active: Boolean,
    onItemClick: () -> Unit = { }
) {
    Card(
        modifier  = Modifier.clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (active) colorCardActive else colorCardInactive
        )
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)

        ) {
            val (labelNameRef, nameRef,
                labelPriceRef, priceRef,
                labelQuantityRef, quantityRef, imageRef) = createRefs()

            createHorizontalChain(labelNameRef, imageRef)
            createHorizontalChain(nameRef, imageRef)

            createHorizontalChain(labelPriceRef, labelQuantityRef, imageRef)
            createHorizontalChain(priceRef, quantityRef, imageRef)

            Text(
                text = "Nome",
                modifier = Modifier
                    .constrainAs(labelNameRef) {
                        linkTo(start = parent.start, end = imageRef.start, startMargin = 8.dp)
                        top.linkTo(parent.top)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.7F
                    }
                    .padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = name,
                modifier = Modifier.constrainAs(nameRef) {
                    linkTo(start = parent.start, end = imageRef.start, startMargin = 8.dp)
                    top.linkTo(labelNameRef.bottom)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.7F
                },
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Preço",
                modifier = Modifier.constrainAs(labelPriceRef) {
                    start.linkTo(nameRef.start)
                    top.linkTo(nameRef.bottom, margin = 8.dp)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.35F
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = price.formatToCurrency(),
                modifier = Modifier
                    .constrainAs(priceRef) {
                        start.linkTo(labelPriceRef.start)
                        top.linkTo(labelPriceRef.bottom)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.35F
                    }
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Quantidade",
                modifier = Modifier.constrainAs(labelQuantityRef) {
                    start.linkTo(labelPriceRef.end)
                    top.linkTo(labelPriceRef.top)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.35F
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = quantity.formatQuantityIn(quantityUnit),
                modifier = Modifier
                    .constrainAs(quantityRef) {
                        start.linkTo(labelPriceRef.start)
                        top.linkTo(labelPriceRef.bottom)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.35F
                    }
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )

            CoilImageViewer(
                containerModifier = Modifier
                    .constrainAs(imageRef) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                        horizontalChainWeight = 0.3F
                    },
                data = image,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListCardPreview() {
    MarketTheme {
        Surface {
            ProductListCard(
                name = "Wafer de Chocolate com Avelã",
                price = 4.99,
                quantity = 110.0,
                quantityUnit = EnumUnit.GRAM,
                image = 0,
                active = true
            )
        }
    }
}