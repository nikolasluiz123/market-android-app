package br.com.market.storage.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.storage.R
import br.com.market.storage.sampledata.sampleProducts
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.ui.theme.CINZA_500
import br.com.market.storage.ui.theme.StorageTheme

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

@Preview
@Composable
fun LazyVerticalGridComponentPreview() {
    StorageTheme {
        Surface {
            LazyVerticalGridComponent(
                items = sampleProducts,
                isSearching = false,
            ) {
                CardProductItem(product = it)
            }
        }
    }
}

@Preview
@Composable
fun LazyVerticalGridComponentSearchingPreview() {
    StorageTheme {
        Surface {
            LazyVerticalGridComponent(
                items = sampleProducts,
                isSearching = true,
            ) {
                CardProductItem(product = it)
            }
        }
    }
}

@Preview
@Composable
fun LazyVerticalGridComponentEmptyPreview() {
    StorageTheme {
        Surface {
            LazyVerticalGridComponent(
                items = emptyList<ProductDomain>(),
                isSearching = false,
            ) {
                CardProductItem(product = it)
            }
        }
    }
}