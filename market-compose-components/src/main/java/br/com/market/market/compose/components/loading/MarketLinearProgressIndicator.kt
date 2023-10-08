package br.com.market.market.compose.components.loading

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.market.core.theme.BLUE_900

/**
 * Indicador de progresso em linha.
 *
 * @param show Flag que indica se deve ou não ser exibido o componente.
 * @param modifier Modificadores específicos.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun MarketLinearProgressIndicator(show: Boolean, modifier: Modifier = Modifier) {
    if (show) {
        LinearProgressIndicator(modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary)
    }
}