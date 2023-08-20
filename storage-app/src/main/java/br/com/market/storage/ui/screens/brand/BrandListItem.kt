package br.com.market.storage.ui.screens.brand

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.MarketTheme
import br.com.market.core.theme.colorCardActive
import br.com.market.core.theme.colorCardInactive
import br.com.market.core.ui.components.LabeledText
import br.com.market.storage.R

@Composable
fun BrandListItem(brandName: String, active: Boolean, onItemClick: () -> Unit = { }) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = if (active) colorCardActive else colorCardInactive)
            .clickable {
                onItemClick()
            }
    ) {
        val (nameRef) = createRefs()

        LabeledText(
            modifier = Modifier
                .constrainAs(nameRef) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(parent.top)

                    width = Dimension.fillToConstraints
                },
            label = stringResource(id = R.string.brand_list_card_label_name),
            value = brandName
        )
    }
}

@Composable
fun BrandListCardSearch(brandName: String, active: Boolean, onItemClick: () -> Unit = { }) {
    ConstraintLayout(
        Modifier
            .background(color = if (active) colorCardActive else colorCardInactive)
            .clickable { onItemClick() }
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

@Preview(showBackground = true)
@Composable
fun CategoryListCardPreview() {
    MarketTheme {
        Surface {
            BrandListItem("Nestle", active = true)
        }
    }
}