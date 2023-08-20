package br.com.market.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.theme.GREY_800
import br.com.market.core.theme.MarketTheme
import java.util.UUID

@Composable
fun LabeledText(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    maxLinesValue: Int = Int.MAX_VALUE
) {
    Column(modifier.wrapContentHeight()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = GREY_800
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = GREY_800,
            maxLines = maxLinesValue
        )
    }
}

@Preview
@Composable
fun LabeledTextPreview() {
    MarketTheme {
        Surface {
            LabeledText(label = "Identificador", value = UUID.randomUUID().toString())
        }
    }
}