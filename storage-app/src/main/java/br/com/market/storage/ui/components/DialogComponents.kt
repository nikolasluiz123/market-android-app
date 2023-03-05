package br.com.market.storage.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.storage.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogError(
    show: Boolean,
    onDismissRequest: () -> Unit,
    errorMessage: String
) {
    if (show) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = onDismissRequest
        ) {
            Surface(
                shape = MaterialTheme.shapes.large
            ) {
                ConstraintLayout {
                    val (titleRef, messageRef, okButtonRef) = createRefs()

                    Text(
                        modifier = Modifier.constrainAs(titleRef) {
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top, margin = 8.dp)

                            width = Dimension.fillToConstraints
                        },
                        text = stringResource(R.string.error_dialog_title),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        modifier = Modifier.constrainAs(messageRef) {
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(titleRef.bottom, margin = 8.dp)
                            bottom.linkTo(okButtonRef.top)

                            width = Dimension.fillToConstraints
                        },
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    TextButton(
                        modifier = Modifier.constrainAs(okButtonRef) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                        onClick = onDismissRequest
                    ) {
                        Text(text = stringResource(id = R.string.dialog_button_ok_label))
                    }
                }
            }
        }
    }
}