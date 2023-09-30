package br.com.market.market.compose.components.button.fab

import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.market.core.theme.BLUE_500

/**
 * FAB padrão do aplicativo.
 *
 * @param modifier O modificador para aplicar ao componente.
 * @param containerColor A cor de fundo do botão.
 * @param onClick A ação a ser realizada quando o botão é clicado.
 * @param content O conteúdo do botão, geralmente um ícone.
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