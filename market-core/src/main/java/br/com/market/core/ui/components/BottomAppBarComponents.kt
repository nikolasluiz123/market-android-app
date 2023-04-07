package br.com.market.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.market.core.theme.GREY_300
import br.com.market.core.ui.components.buttons.AnimatedSmallFabWithLabel
import br.com.market.core.ui.components.buttons.MarketSmallFabSubAction
import br.com.market.core.ui.components.buttons.fab.FabMultiActionState
import br.com.market.core.ui.components.buttons.fab.SubActionFabItem

/**
 * Componente bottom app bar padrão.
 *
 * @param modifier
 * @param actions
 * @param floatingActionButton
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun MarketBottomAppBar(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = { },
    floatingActionButton: @Composable () -> Unit = { }
) {
    BottomAppBar(
        modifier = modifier,
        actions = actions,
        floatingActionButton = floatingActionButton,
        containerColor = GREY_300
    )
}

@Composable
fun MultiActionsFabBottomAppBar(
    modifier: Modifier = Modifier,
    subActionsFab: List<SubActionFabItem>,
    state: FabMultiActionState,
    actions: @Composable RowScope.() -> Unit = { },
    floatingActionButton: @Composable () -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        subActionsFab.forEach {
            AnimatedSmallFabWithLabel(
                showLabel = true,
                fabContent = {
                    MarketSmallFabSubAction(
                        iconPainter = it.icon,
                        onClick = it.onFabItemClicked,
                        label = it.label,
                        modifier = Modifier.align(Alignment.End)
                    )
                },
                state = state
            )
        }

        MarketBottomAppBar(
            modifier = modifier,
            actions = actions,
            floatingActionButton = floatingActionButton
        )
    }
}