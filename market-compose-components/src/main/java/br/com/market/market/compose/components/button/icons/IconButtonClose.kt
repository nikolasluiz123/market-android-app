package br.com.market.market.compose.components.button.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme

/**
 * Botão com ícone de fechar.
 *
 * @param onClick Ação ao clicar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun IconButtonClose(
    buttonModifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    iconColor: Color = Color.Black
) {
    IconButton(modifier = buttonModifier, onClick = onClick) {
        Icon(
            modifier = iconModifier,
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.label_limpar_pesquisa),
            tint = iconColor
        )
    }
}


@Preview
@Composable
fun IconButtonClosePreview() {
    MarketTheme {
        Surface {
            IconButtonClose()
        }
    }
}