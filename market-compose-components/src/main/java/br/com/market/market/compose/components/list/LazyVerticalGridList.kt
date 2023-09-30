package br.com.market.market.compose.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Lazy vertical grid list
 *
 * @param T Tipo do dado exibido.
 * @param modifier Modificadores específicos.
 * @param items Lista de itens que serão carregados.
 * @param numberColumns Número de colunas.
 * @param arrangementSpace Espaçamento vertical e horizontal em cada item.
 * @param contentPadding Espaço ao redor da lista.
 * @param itemList Composable que define qual será o card do item. Pode ser usado outro tipo de container além do card.
 * @receiver
 */
@Composable
private fun <T> LazyVerticalGridList(
    modifier: Modifier = Modifier,
    items: List<T>,
    numberColumns: Int = 2,
    arrangementSpace: Dp = 16.dp,
    contentPadding: Dp = 16.dp,
    itemList: @Composable (T) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(numberColumns),
        verticalArrangement = Arrangement.spacedBy(arrangementSpace),
        horizontalArrangement = Arrangement.spacedBy(arrangementSpace),
        contentPadding = PaddingValues(contentPadding)
    ) {
        items(items = items) { item ->
            itemList(item)
        }
    }
}