package br.com.market.market.compose.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

@Composable
fun <T : Any> PagedVerticalList(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<T>,
    verticalArrangementSpace: Dp = 0.dp,
    contentPadding: Dp = 0.dp,
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
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey(),
                    contentType = pagingItems.itemContentType(
                    )
                ) { index ->
                    pagingItems[index]?.let {
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