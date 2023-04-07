package br.com.market.core.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.R
import br.com.market.core.theme.BLUE_500
import br.com.market.core.theme.MarketTheme

/**
 * FAB que representa a ação de adicionar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun FloatingActionButtonAdd(
    onClick: () -> Unit = { }
) {
    MarketFloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.label_adicionar),
            tint = Color.White
        )
    }
}

/**
 * FAB que representa a ação de salvar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun FloatingActionButtonSave(modifier: Modifier = Modifier, onClick: () -> Unit = { }) {
    MarketFloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(R.string.label_save),
            tint = Color.White
        )
    }
}

/**
 * FAB padrão do APP
 *
 * @param containerColor Cor de fundo do botão.
 * @param onClick Ação realizada ao clicar.
 * @param content Conteúdo do botão, normalmente um ícone.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun MarketFloatingActionButton(
    modifier: Modifier = Modifier,
    containerColor: Color = BLUE_500,
    onClick: () -> Unit = { },
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        containerColor = containerColor,
        onClick = onClick
    ) {
        content()
    }
}

@Preview
@Composable
fun FloatingActionButtonAddPreview() {
    MarketTheme {
        Surface {
            FloatingActionButtonAdd()
        }
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