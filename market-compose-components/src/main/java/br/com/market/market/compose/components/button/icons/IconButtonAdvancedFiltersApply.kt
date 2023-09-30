package br.com.market.market.compose.components.button.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.R.drawable
import br.com.market.core.theme.MarketTheme

@Composable
fun IconButtonAdvancedFiltersApply(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = drawable.ic_advanced_filter_apply),
            contentDescription = "Filtros Avançados Aplicados"
        )
    }
}


@Preview
@Composable
fun IconButtonAdvancedFiltersApplyPreview() {
    MarketTheme {
        Surface {
            IconButtonAdvancedFiltersApply()
        }
    }
}