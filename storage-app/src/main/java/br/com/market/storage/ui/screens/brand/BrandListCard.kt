package br.com.market.storage.ui.screens.brand

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.MarketTheme
import br.com.market.core.theme.colorCardActive
import br.com.market.storage.R

@Composable
fun BrandListCard(brandName: String, onItemClick: () -> Unit = { }) {
    Card(
        Modifier.fillMaxWidth().clickable { onItemClick() },
        colors = CardDefaults.cardColors(containerColor = colorCardActive)
    ) {
        ConstraintLayout(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            val (labelNameRef, nameRef) = createRefs()

            Text(
                text = stringResource(id = R.string.brand_list_card_label_name),
                modifier = Modifier
                    .constrainAs(labelNameRef) {
                        linkTo(start = parent.start, end = parent.end)
                        top.linkTo(parent.top)

                        width = Dimension.fillToConstraints
                    },
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = brandName,
                modifier = Modifier.constrainAs(nameRef) {
                    linkTo(start = labelNameRef.start, end = labelNameRef.end)
                    top.linkTo(labelNameRef.bottom)

                    width = Dimension.fillToConstraints
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryListCardPreview() {
    MarketTheme {
        Surface {
            BrandListCard("Nestle")
        }
    }
}