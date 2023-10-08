package br.com.market.commerce.ui.screens.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.commerce.R
import br.com.market.core.extensions.formatToCurrency
import br.com.market.core.theme.MarketTheme
import br.com.market.enums.EnumUnit
import br.com.market.market.common.extensions.formatQuantityIn
import br.com.market.market.compose.components.CoilImageViewer
import br.com.market.market.compose.components.LabeledText

@Composable
fun ProductListItem(
    name: String,
    price: Double,
    quantity: Double,
    quantityUnit: EnumUnit,
    image: Any,
    active: Boolean,
    onItemClick: () -> Unit = { }
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onItemClick()
            }
    ) {
        val (nameRef, priceRef, quantityRef, imageRef) = createRefs()

        createHorizontalChain(nameRef, imageRef)
        createHorizontalChain(priceRef, quantityRef, imageRef)

        LabeledText(
            modifier = Modifier
                .constrainAs(nameRef) {
                    linkTo(start = parent.start, end = imageRef.start, startMargin = 8.dp)
                    top.linkTo(parent.top)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.7F
                },
            label = stringResource(R.string.product_list_item_label_name),
            value = name
        )

        LabeledText(
            modifier = Modifier.constrainAs(priceRef) {
                start.linkTo(nameRef.start)
                top.linkTo(nameRef.bottom, margin = 8.dp)

                width = Dimension.fillToConstraints
                horizontalChainWeight = 0.35F
            },
            label = stringResource(R.string.product_list_item_label_price),
            value = price.formatToCurrency()
        )

        LabeledText(
            modifier = Modifier.constrainAs(quantityRef) {
                start.linkTo(priceRef.end)
                top.linkTo(priceRef.top)

                width = Dimension.fillToConstraints
                horizontalChainWeight = 0.35F
            },
            label = stringResource(R.string.product_list_item_label_quantity),
            value = quantity.formatQuantityIn(quantityUnit)
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

@Preview(showBackground = true)
@Composable
fun ProductListCardPreview() {
    MarketTheme {
        Surface {
            ProductListItem(
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