package br.com.market.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.theme.CINZA_500

/**
 * Componente de listagem vertical.
 *
 * @param T Tipo do dado exibido.
 * @param modifier Modificadores específicos.
 * @param items Lista de itens que serão carregados.
 * @param emptyStateText Texto exibido quando a lista for vazia.
 * @param itemList Composable que define qual será o card do item. Pode ser usado outro tipo de container além do card.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun <T> LazyVerticalListComponent(
    modifier: Modifier = Modifier,
    items: List<T>,
    emptyStateText: String = stringResource(R.string.text_empty_state_default),
    itemList: @Composable (T) -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (lazyColumnRef, emptyText) = createRefs()

        if (items.isNotEmpty()) {
            LazyVerticalList(
                modifier = Modifier
                    .constrainAs(lazyColumnRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxSize(),
                items = items
            ) {
                itemList(it)
            }
        } else {
            EmptyState(
                modifier = Modifier.constrainAs(emptyText) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = emptyStateText
            )
        }
    }
}

/**
 * Componente de listagem vertical em colunas, normalmente duas colunas.
 *
 * @param T Tipo do dado exibido.
 * @param modifier Modificadores específicos.
 * @param items Lista de itens que serão carregados.
 * @param isSearching Flag que indica se está pesquisando para alterar o tipo de visualização.
 * @param emptyStateText Texto exibido quando a lista for vazia.
 * @param itemList Composable que define qual será o card do item. Pode ser usado outro tipo de container além do card.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun <T> LazyVerticalGridComponent(
    modifier: Modifier = Modifier,
    items: List<T>,
    isSearching: Boolean,
    emptyStateText: String = stringResource(R.string.text_empty_state_default),
    itemList: @Composable (T) -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (lazyColumnRef, lazyGridRef, emptyText) = createRefs()

        if (items.isNotEmpty()) {
            if (isSearching) {
                LazyVerticalList(
                    modifier = Modifier
                        .constrainAs(lazyColumnRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxSize(),
                    items = items
                ) {
                    itemList(it)
                }
            } else {
                LazyVerticalGridList(
                    modifier = Modifier
                        .constrainAs(lazyGridRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxSize(),
                    items = items
                ) {
                    itemList(it)
                }
            }
        } else {
            EmptyState(
                modifier = Modifier.constrainAs(emptyText) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = emptyStateText
            )
        }
    }
}

/**
 * Componente de listagem vertical.
 *
 * @param T Tipo do dado exibido.
 * @param modifier Modificadores específicos.
 * @param items Lista de itens que serão carregados.
 * @param verticalArrangementSpace Espaço entre cada item.
 * @param contentPadding Espaço ao redor da lista.
 * @param itemList Composable que define qual será o card do item. Pode ser usado outro tipo de container além do card.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
private fun <T> LazyVerticalList(
    modifier: Modifier = Modifier,
    items: List<T>,
    verticalArrangementSpace: Dp = 16.dp,
    contentPadding: Dp = 16.dp,
    itemList: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpace),
        contentPadding = PaddingValues(contentPadding)
    ) {
        items(items = items) { item ->
            itemList(item)
        }
    }
}

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

/**
 * Componente que exibe um texto quando a lista está vazia.
 *
 * @param modifier Modificadores específicos.
 * @param text Text a ser exibido.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.text_empty_state_default)
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = CINZA_500
    )
}