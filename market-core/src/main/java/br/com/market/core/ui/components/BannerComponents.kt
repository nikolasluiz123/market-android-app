package br.com.market.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.R.drawable
import br.com.market.core.theme.BLUE_50
import br.com.market.core.theme.GREY_800
import br.com.market.core.theme.MarketTheme

@Composable
fun Banner(
    isVisible: MutableState<Boolean>,
    message: String,
    modifier: Modifier = Modifier,
    labelButtonConfirm: String = "Confirmar",
    labelButtonCancel: String = "Cancelar"
) {
    AnimatedVisibility(
        visible = isVisible.value,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
            .fillMaxWidth()
            .background(color = BLUE_50)
    ) {
        ConstraintLayout(Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)) {

            val (imageRef, messageRef, buttonCancelRef, buttonConfirmRef) = createRefs()

            Image(
                painter = painterResource(id = drawable.ic_calendar_32dp),
                contentDescription = null,
                modifier = Modifier.constrainAs(imageRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = GREY_800,
                modifier = Modifier.constrainAs(messageRef) {
                    start.linkTo(imageRef.end, 8.dp)
                    top.linkTo(imageRef.top)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }
            )

            TextButton(
                modifier = Modifier.constrainAs(buttonCancelRef) {
                    end.linkTo(buttonConfirmRef.start)
                    top.linkTo(buttonConfirmRef.top)
                    bottom.linkTo(buttonConfirmRef.bottom)
                },
                onClick = { isVisible.value = false }
            ) {
                Text(text = labelButtonCancel, style = MaterialTheme.typography.labelMedium)
            }

            TextButton(
                modifier = Modifier.constrainAs(buttonConfirmRef) {
                    top.linkTo(messageRef.bottom, margin = 8.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                onClick = { isVisible.value = false }
            ) {
                Text(text = labelButtonConfirm, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Preview
@Composable
fun BannerPreview() {
    MarketTheme {
        Surface {
            val state = remember { mutableStateOf(true) }

            Banner(
                isVisible = state,
                message = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
            )
        }
    }
}