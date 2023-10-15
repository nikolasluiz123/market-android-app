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
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.R

@Composable
fun UserLovListItem(name: String, onItemClick: () -> Unit = { }) {
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
            modifier = Modifier.constrainAs(nameRef) {
                linkTo(start = parent.start, end = parent.end, bias = 0f)
                top.linkTo(parent.top)
            },
            label = stringResource(id = R.string.user_lov_list_item_label_name),
            value = name
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserLovListItemPreview() {
    MarketTheme {
        Surface {
            UserLovListItem("Nikolas")
        }
    }
}