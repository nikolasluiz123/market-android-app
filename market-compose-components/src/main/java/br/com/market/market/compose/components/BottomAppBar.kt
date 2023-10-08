package br.com.market.market.compose.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
        containerColor = MaterialTheme.colorScheme.secondary
    )
}