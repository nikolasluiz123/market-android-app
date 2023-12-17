package br.com.market.market.compose.components.filter

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
import br.com.market.core.inputs.CommonAdvancedFilterItem
import br.com.market.core.inputs.formatter.InputTextFormatter
import br.com.market.core.theme.GREY_800
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.LabeledText

/**
 * Item de filtro avançado.
 *
 * @param item Instância de [CommonAdvancedFilterItem].
 * @param onItemClick Callback para manipular o clique no item.
 *
 * @see CommonAdvancedFilterItem
 * @see LabeledText
 */
@Composable
fun <T> AdvancedFilterItem(
    item: CommonAdvancedFilterItem<T?>,
    onItemClick: ((T?) -> Unit) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .clickable {
                onItemClick {
                    item.value = it
                    item.checked = it != null
                }
            }
    ) {
        val (checkboxRef, labelRef) = createRefs()

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

        val valueFormatted = item.formatter.formatToString(item.value)

        if (valueFormatted == null) {
            Text(
                modifier = Modifier.constrainAs(labelRef) {
                    start.linkTo(checkboxRef.end, margin = 4.dp)
                    top.linkTo(checkboxRef.top)
                    bottom.linkTo(checkboxRef.bottom)
                },
                text = stringResource(id = item.labelResId),
                style = MaterialTheme.typography.titleSmall,
                color = GREY_800
            )
        } else {
            LabeledText(
                modifier = Modifier.constrainAs(labelRef) {
                    start.linkTo(checkboxRef.end, margin = 4.dp)
                    linkTo(top = checkboxRef.top, bottom = checkboxRef.bottom)
                },
                label = stringResource(id = item.labelResId),
                value = valueFormatted,
                maxLinesValue = 1
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
                    formatter = InputTextFormatter()
                )
            ) { }
        }
    }
}