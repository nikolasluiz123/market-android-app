package br.com.market.core.ui.components.buttons

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.market.core.R
import br.com.market.core.theme.BLUE_500
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.buttons.fab.EnumFabMultiActionState
import br.com.market.core.ui.components.buttons.fab.FabMultiActionState
import br.com.market.core.ui.components.buttons.fab.SubActionFabItem

/**
 * FAB que representa a ação de adicionar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun FloatingActionButtonAdd(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    MarketFloatingActionButton(
        modifier = modifier,
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

/**
 * FAB que suporta multiplas ações
 *
 * @param state
 * @param modifier
 * @param containerColor
 * @param onClick
 * @param content
 *
 * @author Nikolas Luiz Schmitt
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

@Composable
fun rememberFabMultiActionsState(): FabMultiActionState {
    val state = remember {
        FabMultiActionState()
    }

    state.transition = updateTransition(targetState = state.currentState, label = "")

    return state
}

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