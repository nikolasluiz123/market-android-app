package br.com.market.market.compose.components.button.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.theme.MarketTheme

/**
 * Botão com ícone de voltar.
 *
 * @param onClick Ação ao clicar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun IconButtonArrowBack(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(br.com.market.core.R.string.label_voltar)
        )
    }
}


@Preview
@Composable
fun IconButtonArrowBackPreview() {
    MarketTheme {
        Surface {
            IconButtonArrowBack()
        }
    }
}