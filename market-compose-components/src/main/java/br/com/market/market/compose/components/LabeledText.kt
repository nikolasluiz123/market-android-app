package br.com.market.market.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.theme.MarketTheme
import java.util.UUID

@Composable
fun LabeledText(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    maxLinesValue: Int = Int.MAX_VALUE,
    overflowValue: TextOverflow = TextOverflow.Ellipsis,
    textAlign: TextAlign? = null
) {
    Column(modifier.wrapContentHeight()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = textAlign
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = maxLinesValue,
            overflow = overflowValue,
            textAlign = textAlign
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