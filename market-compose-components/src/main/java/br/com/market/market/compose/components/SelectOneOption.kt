package br.com.market.market.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.list.LazyVerticalList

@Composable
fun SelectOneOption(
    modifier: Modifier = Modifier,
    items: List<Pair<String, Int>>,
    onDismiss: () -> Unit = { },
    onItemClick: (Pair<String, Int>) -> Unit = { }
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface {
            LazyVerticalList(
                modifier = modifier.widthIn(min = 300.dp),
                items = items,
                verticalArrangementSpace = 16.dp,
                contentPadding = 16.dp
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(it) },
                    text = it.first,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun SelectOneOptionPreview() {
    MarketTheme {
        Surface {
            SelectOneOption(
                items = emptyList()
            )
        }
    }
}