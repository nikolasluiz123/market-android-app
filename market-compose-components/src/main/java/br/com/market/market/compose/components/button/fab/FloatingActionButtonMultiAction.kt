package br.com.market.market.compose.components.button.fab

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import br.com.market.core.theme.BLUE_500
import br.com.market.core.ui.components.buttons.fab.EnumFabMultiActionState
import br.com.market.core.ui.components.buttons.fab.FabMultiActionState
import br.com.market.core.ui.components.buttons.fab.SubActionFabItem

/**
 * FAB que suporta múltiplas ações.
 *
 * @param state O estado atual do botão.
 * @param modifier O modificador para aplicar ao componente.
 * @param containerColor A cor de fundo do botão.
 * @param onClick A ação a ser realizada quando o botão é clicado.
 * @param content O conteúdo do botão, geralmente um ícone.
 * @uthor Nikolas Luiz Schmitt
 */
@Composable
fun MarketFloatingActionButtonMultiActions(
    state: FabMultiActionState,
    modifier: Modifier = Modifier,
    containerColor: Color = BLUE_500,
    onClick: () -> Unit = { },
    content: @Composable () -> Unit
) {
    val rotation = state.transition?.animateFloat(
        transitionSpec = {
            if (targetState == EnumFabMultiActionState.EXPANDED) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = "",
        targetValueByState = {
            if (it == EnumFabMultiActionState.EXPANDED) 45f else 0f
        }
    )

    FloatingActionButton(
        modifier = modifier,
        containerColor = containerColor,
        onClick = {
            state.stateChange()
            onClick()
        }
    ) {
        Box(Modifier.rotate(rotation?.value ?: 0f)) {
            content()
        }
    }
}

/**
 * Sub-ação para o FAB com suporte a múltiplas ações.
 *
 * @param iconPainter O ícone a ser exibido.
 * @param onClick A ação a ser realizada quando o botão é clicado.
 * @param modifier O modificador para aplicar ao componente.
 * @param label O rótulo a ser exibido.
 * @uthor Nikolas Luiz Schmitt
 */
@Composable
fun MarketSmallFabSubAction(
    iconPainter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null
) {
    SmallFloatingActionButton(
        modifier = modifier.padding(end = 20.dp, bottom = 8.dp),
        containerColor = Color.White,
        onClick = onClick
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (label != null) {
                Text(label, style = MaterialTheme.typography.titleSmall, color = BLUE_500)
            }

            Spacer(modifier = Modifier.size(8.dp))

            Icon(
                painter = iconPainter,
                contentDescription = null,
                tint = BLUE_500
            )
        }
    }
}

/**
 * FAB com animações para suportar rótulos.
 *
 * @param modifier O modificador para aplicar ao componente.
 * @param showLabel Indica se o rótulo deve ser exibido.
 * @param fabContent O conteúdo do FAB.
 * @param state O estado do FAB com suporte a múltiplas ações.
 * @param labelContent O conteúdo do rótulo.
 * @uthor Nikolas Luiz Schmitt
 */
@Composable
fun AnimatedSmallFabWithLabel(
    modifier: Modifier = Modifier,
    showLabel: Boolean,
    fabContent: @Composable () -> Unit,
    state: FabMultiActionState,
    labelContent: @Composable () -> Unit = { }
) {
    val alpha: State<Float>? = state.transition?.animateFloat(
        transitionSpec = {
            tween(durationMillis = 200)
        },
        label = "",
        targetValueByState = {
            if (it == EnumFabMultiActionState.EXPANDED) 1f else 0f
        }
    )
    val scale: State<Float>? = state.transition?.animateFloat(
        label = "",
        targetValueByState = {
            if (it == EnumFabMultiActionState.EXPANDED) 1.0f else 0f
        }
    )
    Row(
        modifier = modifier
            .alpha(animateFloatAsState((alpha?.value ?: 0f)).value)
            .scale(animateFloatAsState(targetValue = scale?.value ?: 0f).value),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showLabel) {
            labelContent.invoke()
        }
        fabContent.invoke()
    }
}

/**
 * Retorna o estado do FAB com suporte a múltiplas ações.
 *
 * @return O estado do FAB.
 * @uthor Nikolas Luiz Schmitt
 */
@Composable
fun rememberFabMultiActionsState(): FabMultiActionState {
    val state = remember {
        FabMultiActionState()
    }

    state.transition = updateTransition(targetState = state.currentState, label = "")

    return state
}

/**
 * Exibe as sub-ações do FAB com suporte a múltiplas ações.
 *
 * @param modifier O modificador para aplicar ao componente.
 * @param state O estado do FAB com suporte a múltiplas ações.
 * @param subActionsFab A lista de sub-ações.
 * @uthor Nikolas Luiz Schmitt
 */
@Composable
fun SmallFabActions(
    modifier: Modifier,
    state: FabMultiActionState,
    subActionsFab: List<SubActionFabItem>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        if (state.currentState == EnumFabMultiActionState.EXPANDED) {
            subActionsFab.forEach {
                AnimatedSmallFabWithLabel(
                    showLabel = true,
                    fabContent = {
                        MarketSmallFabSubAction(
                            iconPainter = it.icon,
                            onClick = it.onFabItemClicked,
                            label = it.label
                        )
                    },
                    state = state
                )
            }
        }
    }
}