package br.com.market.market.compose.components.button.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme

/**
 * FAB que representa a ação de salvar.
 *
 * @param modifier O modificador para aplicar ao componente.
 * @param onClick A ação a ser realizada quando o botão é clicado.
 * @uthor Nikolas Luiz Schmitt
 */
@Composable
fun FloatingActionButtonSave(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    MarketFloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(R.string.label_save),
        )
    }
}



@Preview
@Composable
fun FloatingActionButtonSavePreview() {
    MarketTheme {
        Surface {
            FloatingActionButtonSave()
        }
    }
}
