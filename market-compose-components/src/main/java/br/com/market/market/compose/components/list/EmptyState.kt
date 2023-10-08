package br.com.market.market.compose.components.list

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

/**
 * Componente que exibe um texto quando a lista está vazia.
 *
 * @param modifier Modificadores específicos.
 * @param text Text a ser exibido.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    text: String = stringResource(br.com.market.core.R.string.text_empty_state_default)
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}