package br.com.market.market.compose.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import br.com.market.core.theme.RED_600

@Composable
fun <T : Any> PagedVerticalList(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<T>,
    verticalArrangementSpace: Dp = 0.dp,
    contentPadding: Dp = 0.dp,
    itemList: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpace),
        contentPadding = PaddingValues(contentPadding)
    ) {

        when (val refreshState = pagingItems.loadState.refresh) {
            is LoadState.Error -> {
                item {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(8.dp)
                    ) {
                        val (iconRef, textRef, btnRetryRef) = createRefs()
                        createHorizontalChain(iconRef, textRef)

                        Icon(
                            modifier = Modifier.constrainAs(iconRef) {
                                start.linkTo(parent.start)
                                linkTo(top = textRef.top, bottom = textRef.bottom)

                                horizontalChainWeight = 0.2f
                                width = Dimension.fillToConstraints
                            },
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Erro",
                            tint = RED_600
                        )

                        Text(
                            modifier = Modifier.constrainAs(textRef) {
                                linkTo(top = parent.top, bottom = parent.bottom, bias = 0f)

                                horizontalChainWeight = 0.8f
                                width = Dimension.fillToConstraints
                            },
                            text = refreshState.error.message ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )

                        OutlinedButton(
                            modifier = Modifier.constrainAs(btnRetryRef) {
                                end.linkTo(parent.end)
                                linkTo(top = textRef.bottom, bottom = parent.bottom, bias = 0f)
                            },
                            onClick = pagingItems::refresh
                        ) {
                            Text(
                                text = "Recarregar",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }

            is LoadState.Loading -> {
                item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
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
                // Não precisa ser feito nada quando não estiver carregando
            }

            is LoadState.Loading -> {
                item {
                    Box(Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }

            is LoadState.NotLoading -> {
                // Não precisa ser feito nada quando não estiver carregando
            }
        }

        when (pagingItems.loadState.prepend) {
            is LoadState.Error -> {
                // Ainda não pensei em como exibir um erro nesse momento
            }

            is LoadState.NotLoading -> {
                // Não precisa ser feito nada quando não estiver carregando
            }

            is LoadState.Loading -> {
                item {
                    Box(Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}