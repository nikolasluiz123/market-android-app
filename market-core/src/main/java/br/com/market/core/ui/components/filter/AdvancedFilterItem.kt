package br.com.market.core.ui.components.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.filter.CommonAdvancedFilterItem
import br.com.market.core.filter.formatter.StringAdvancedFilterFormatter
import br.com.market.core.theme.MarketTheme

@Composable
fun AdvancedFilterItem(
    item: CommonAdvancedFilterItem,
    onItemClick: ((Any) -> Unit) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .clickable {
                onItemClick {
                    item.value = it
                }
            }
    ) {
        val (checkboxRef, labelRef, valueRef) = createRefs()

        var checked by remember(item.checked) { mutableStateOf(item.checked) }

        Checkbox(
            modifier = Modifier.constrainAs(checkboxRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            },
            checked = checked,
            onCheckedChange = {
                checked = it
                item.checked = it
            }
        )

        if (item.value == null) {
            Text(
                modifier = Modifier.constrainAs(labelRef) {
                    start.linkTo(checkboxRef.end, margin = 4.dp)
                    top.linkTo(checkboxRef.top)
                    bottom.linkTo(checkboxRef.bottom)
                },
                text = stringResource(id = item.labelResId),
                style = MaterialTheme.typography.labelLarge
            )
        } else {
            Text(
                modifier = Modifier.constrainAs(labelRef) {
                    start.linkTo(checkboxRef.end, margin = 4.dp)
                    linkTo(top = parent.top, bottom = valueRef.top)
                },
                text = stringResource(id = item.labelResId),
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                modifier = Modifier.constrainAs(valueRef) {
                    start.linkTo(checkboxRef.end, margin = 4.dp)
                    linkTo(top = labelRef.bottom, bottom = parent.bottom)
                },
                text = item.formatter.formatToString(item.value),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun AdvancedFilterItemPreview() {
    MarketTheme {
        Surface {
            AdvancedFilterItem(
                CommonAdvancedFilterItem(
                    labelResId = R.string.app_name,
                    identifier = "teste",
                    formatter = StringAdvancedFilterFormatter()
                )
            ) { }
        }
    }
}