package br.com.market.market.compose.components.button.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.theme.MarketTheme

/**
 * Botão com ícone de pesquisa.
 *
 * @param onClick Ação ao clicar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun IconButtonSearch(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Search, contentDescription = stringResource(br.com.market.core.R.string.label_pesquisar)
        )
    }
}



@Preview
@Composable
fun IconButtonSearchPreview() {
    MarketTheme {
        Surface {
            IconButtonSearch()
        }
    }
}