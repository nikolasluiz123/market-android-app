package br.com.market.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import br.com.market.core.R
import br.com.market.core.theme.GREY_500

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
                    .fillMaxSize()
                    .constrainAs(lazyColumnRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
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
 * Componente de listagem vertical com pesquisa paginada.
 *
 * @param T
 * @param modifier
 * @param pagingItems
 * @param verticalArrangementSpace
 * @param contentPadding
 * @param refreshLoadStateError
 * @param refreshLoadStateLoading
 * @param appendLoadStateError
 * @param appendLoadStateLoading
 * @param appendLoadStateNotLoading
 * @param prependLoadStateError
 * @param prependLoadStateLoading
 * @param prependLoadStateNotLoading
 * @param emptyStateText
 * @param itemList
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun <T : Any> PagedVerticalListComponent(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<T>,
    verticalArrangementSpace: Dp = 16.dp,
    contentPadding: Dp = 16.dp,
    refreshLoadStateError: @Composable (() -> Unit?)? = null,
    refreshLoadStateLoading: @Composable (() -> Unit?)? = null,
    appendLoadStateError: @Composable (() -> Unit?)? = null,
    appendLoadStateLoading: @Composable (() -> Unit?)? = null,
    appendLoadStateNotLoading: @Composable (() -> Unit?)? = null,
    prependLoadStateError: @Composable (() -> Unit?)? = null,
    prependLoadStateLoading: @Composable (() -> Unit?)? = null,
    prependLoadStateNotLoading: @Composable (() -> Unit?)? = null,
    emptyStateText: String = stringResource(R.string.text_empty_state_default),
    itemList: @Composable (T) -> Unit
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (lazyColumnRef, emptyText) = createRefs()

        if (pagingItems.itemCount > 0) {
            PagedVerticalList(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(lazyColumnRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                pagingItems = pagingItems,
                verticalArrangementSpace = verticalArrangementSpace,
                contentPadding = contentPadding,
                refreshLoadStateError = refreshLoadStateError,
                refreshLoadStateLoading = refreshLoadStateLoading,
                appendLoadStateError = appendLoadStateError,
                appendLoadStateLoading = appendLoadStateLoading,
                appendLoadStateNotLoading = appendLoadStateNotLoading,
                prependLoadStateError = prependLoadStateError,
                prependLoadStateLoading = prependLoadStateLoading,
                prependLoadStateNotLoading = prependLoadStateNotLoading
            ) {
                itemList(it)
            }
        } else {
            EmptyState(
                modifier = Modifier
                    .constrainAs(emptyText) {
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

@Composable
private fun <T : Any> PagedVerticalList(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<T>,
    verticalArrangementSpace: Dp = 16.dp,
    contentPadding: Dp = 16.dp,
    refreshLoadStateError: @Composable (() -> Unit?)? = null,
    refreshLoadStateLoading: @Composable (() -> Unit?)? = null,
    appendLoadStateError: @Composable (() -> Unit?)? = null,
    appendLoadStateLoading: @Composable (() -> Unit?)? = null,
    appendLoadStateNotLoading: @Composable (() -> Unit?)? = null,
    prependLoadStateError: @Composable (() -> Unit?)? = null,
    prependLoadStateLoading: @Composable (() -> Unit?)? = null,
    prependLoadStateNotLoading: @Composable (() -> Unit?)? = null,
    itemList: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpace),
        contentPadding = PaddingValues(contentPadding)
    ) {
        when (pagingItems.loadState.refresh) {
            is LoadState.Error -> {
                item {
                    if (refreshLoadStateError != null) {
                        refreshLoadStateError()
                    } else {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            Text(text = "Ocorreu um Erro", Modifier.clickable {
                                pagingItems.refresh()
                            })
                        }
                    }
                }
            }
            is LoadState.Loading -> {
                item {
                    if (refreshLoadStateLoading != null) {
                        refreshLoadStateLoading()
                    } else {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
            is LoadState.NotLoading -> {
                items(items = pagingItems) { item ->
                    item?.let {
                        itemList(it)
                    }
                }
            }
        }

        when (pagingItems.loadState.append) {
            is LoadState.Error -> {
                item {
                    if (appendLoadStateError != null) {
                        appendLoadStateError()
                    }
                }
            }
            is LoadState.Loading -> {
                item {
                    if (appendLoadStateLoading != null) {
                        appendLoadStateLoading()
                    } else {
                        Box(Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
            is LoadState.NotLoading -> {
                item {
                    if (appendLoadStateNotLoading != null) {
                        appendLoadStateNotLoading()
                    }
                }
            }
        }

        when (pagingItems.loadState.prepend) {
            is LoadState.Error -> {
                item {
                    if (prependLoadStateError != null) {
                        prependLoadStateError()
                    }
                }
            }
            is LoadState.NotLoading -> {
                item {
                    if (prependLoadStateLoading != null) {
                        prependLoadStateLoading()
                    }
                }
            }
            is LoadState.Loading -> {
                item {
                    if (prependLoadStateNotLoading != null) {
                        prependLoadStateNotLoading()
                    } else {
                        Box(Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
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
        color = GREY_500
    )
}