package br.com.market.market.compose.components.button.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme


@Composable
fun IconButtonSync(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sync_24dp), contentDescription = stringResource(R.string.label_sync)
        )
    }
}


@Preview
@Composable
fun IconButtonSyncPreview() {
    MarketTheme {
        Surface {
            IconButtonSync()
        }
    }
}