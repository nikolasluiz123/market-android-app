package br.com.market.market.compose.components.lov.items

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
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.LabeledText
import br.com.market.market.compose.components.R

@Composable
fun MarketLovListItem(
    name: String,
    address: String,
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
        val (nameRef, addressRef) = createRefs()

        LabeledText(
            modifier = Modifier
                .constrainAs(nameRef) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(parent.top)

                    width = Dimension.fillToConstraints
                },
            label = stringResource(id = R.string.market_lov_label_name),
            value = name
        )

        LabeledText(
            modifier = Modifier
                .constrainAs(addressRef) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(nameRef.bottom)

                    width = Dimension.fillToConstraints
                },
            label = stringResource(id = R.string.market_lov_label_address),
            value = address
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MarketLovListItemPreview() {
    MarketTheme {
        Surface {
            MarketLovListItem("Supermercado TOP", "Rua Júlio Michel 222")
        }
    }
}