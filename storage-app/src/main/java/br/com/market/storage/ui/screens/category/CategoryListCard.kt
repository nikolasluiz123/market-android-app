package br.com.market.storage.ui.screens.category

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
import br.com.market.core.theme.*
import br.com.market.storage.R

@Composable
fun CategoryListCard(
    categoryName: String,
    active: Boolean,
    onItemClick: () -> Unit = { }
) {
    Card(
        Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick()
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (active) colorCardActive else colorCardInactive
        )
    ) {
        ConstraintLayout(
            Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            val (labelNameRef, nameRef) = createRefs()
            val textColor = if (active) colorTextActive else colorTextInactive

            Text(
                text = stringResource(id = R.string.category_list_card_label_name),
                modifier = Modifier
                    .constrainAs(labelNameRef) {
                        linkTo(start = parent.start, end = parent.end)
                        top.linkTo(parent.top)

                        width = Dimension.fillToConstraints
                    },
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )

            Text(
                text = categoryName,
                modifier = Modifier.constrainAs(nameRef) {
                    linkTo(start = labelNameRef.start, end = labelNameRef.end)
                    top.linkTo(labelNameRef.bottom)

                    width = Dimension.fillToConstraints
                },
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryListCardPreview() {
    MarketTheme {
        Surface {
            CategoryListCard("Biscoito", true)
        }
    }
}