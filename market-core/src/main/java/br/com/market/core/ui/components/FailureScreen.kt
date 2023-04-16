package br.com.market.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.BLUE_600
import br.com.market.core.theme.GREY_800

@Composable
fun FailureScreen(
    onButtonRetryClick: () -> Unit,
    onButtonBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    message: String = "Ocorreu um erro inesperado"
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (messageRef, retryButtonRef, backButtonRef) = createRefs()

            Text(
                text = message,
                color = GREY_800,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.constrainAs(messageRef) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(parent.top)
                }
            )

            Button(
                onClick = onButtonRetryClick,
                colors = ButtonDefaults.buttonColors(containerColor = BLUE_600),
                modifier = Modifier.constrainAs(retryButtonRef) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(messageRef.bottom, margin = 8.dp)
                }
            ) {
                Text(
                    text = "Tentar Novamente",
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            OutlinedButton(
                onClick = onButtonBackClick,
                border = BorderStroke(width = ButtonDefaults.outlinedButtonBorder.width, color = BLUE_600),
                modifier = Modifier.constrainAs(backButtonRef) {
                    linkTo(start = retryButtonRef.start, end = retryButtonRef.end)
                    top.linkTo(retryButtonRef.bottom, margin = 8.dp)

                    width = Dimension.fillToConstraints
                }

            ) {
                Text(
                    text = "Voltar",
                    color = BLUE_600,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}