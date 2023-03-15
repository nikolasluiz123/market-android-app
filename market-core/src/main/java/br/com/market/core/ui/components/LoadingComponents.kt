package br.com.market.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
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
        LinearProgressIndicator(modifier.fillMaxWidth(), color = BLUE_900)
    }
}

/**
 * Indicador de progesso circular.
 *
 * @param show Flag que indica se deve ou não ser exibido o componente.
 * @param label Texto exibido abaixo do indicador.
 * @param modifier Modificadores específicos.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun MarketCircularBlockUIProgressIndicator(
    show: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    if (show) {
        Popup(alignment = Alignment.Center) {
            ConstraintLayout(modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .pointerInput(Unit) { }
                .zIndex(999999F)) {

                val (loadingRef, textRef) = createRefs()
                CircularProgressIndicator(
                    color = BLUE_900,
                    modifier = Modifier.constrainAs(loadingRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                )
                Text(
                    label,
                    modifier = Modifier.constrainAs(textRef) {
                        start.linkTo(loadingRef.start)
                        top.linkTo(loadingRef.bottom, margin = 8.dp)
                        end.linkTo(loadingRef.end)
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = BLUE_900
                )

            }
        }
    }
}