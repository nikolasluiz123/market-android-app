package br.com.market.market.compose.components.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

/**
 * Componente de listagem vertical.
 *
 * @param T Tipo do dado exibido.
 * @param containerModifier Modificadores específicos.
 * @param items Lista de itens que serão carregados.
 * @param emptyStateText Texto exibido quando a lista for vazia.
 * @param itemList Composable que define qual será o card do item. Pode ser usado outro tipo de container além do card.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun <T> LazyVerticalListWithEmptyState(
    modifier: Modifier = Modifier,
    items: List<T>,
    emptyStateText: String = stringResource(br.com.market.core.R.string.text_empty_state_default),
    verticalArrangementSpace: Dp = 0.dp,
    contentPadding: Dp = 0.dp,
    itemList: @Composable (T) -> Unit,
) {
    ConstraintLayout(modifier = modifier) {
        val (lazyColumnRef, emptyText) = createRefs()

        if (items.isNotEmpty()) {
            LazyVerticalList(
                modifier = Modifier
                    .constrainAs(lazyColumnRef) {
                        linkTo(
                            start = parent.start,
                            top = parent.top,
                            end = parent.end,
                            bottom = parent.bottom,
                            verticalBias = 0F,
                            horizontalBias = 0F
                        )
                    },
                items = items,
                verticalArrangementSpace = verticalArrangementSpace,
                contentPadding = contentPadding
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