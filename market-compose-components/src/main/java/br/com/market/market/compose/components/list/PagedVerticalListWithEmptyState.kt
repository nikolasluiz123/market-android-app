package br.com.market.market.compose.components.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.LazyPagingItems

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
fun <T : Any> PagedVerticalListWithEmptyState(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<T>,
    verticalArrangementSpace: Dp = 0.dp,
    contentPadding: Dp = 0.dp,
    emptyStateText: String = stringResource(br.com.market.core.R.string.text_empty_state_default),
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
                contentPadding = contentPadding
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