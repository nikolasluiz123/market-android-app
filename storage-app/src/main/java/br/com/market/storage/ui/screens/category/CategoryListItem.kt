package br.com.market.storage.ui.screens.category

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
import br.com.market.storage.R

@Composable
fun CategoryListItem(
    categoryName: String,
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
        val (nameRef) = createRefs()

        br.com.market.market.compose.components.LabeledText(
            modifier = Modifier
                .constrainAs(nameRef) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(parent.top)

                    width = Dimension.fillToConstraints
                },
            label = stringResource(id = R.string.category_list_card_label_name),
            value = categoryName
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryListCardPreview() {
    MarketTheme {
        Surface {
            CategoryListItem("Biscoito", true)
        }
    }
}