package br.com.market.storage.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.storage.ui.states.FormProductUiState
import br.com.market.storage.ui.theme.StorageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandDialog(
    state: FormProductUiState = FormProductUiState(),
    onDissmissDialog: (Boolean) -> Unit = { },
    onCancelClick: () -> Unit = { },
    onConfirmClick: () -> Unit = { }
) {
    Dialog(onDismissRequest = { onDissmissDialog(false) }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            ConstraintLayout(Modifier.fillMaxWidth().wrapContentHeight()) {
                val (titleRef, cancelButtonRef, confirmButtonRef,
                    inputNameRef, inputQdtRef) = createRefs()

                Text(
                    modifier = Modifier.constrainAs(titleRef) {
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(parent.top, margin = 8.dp)
                    },
                    text = "Criando uma Marca",
                    style = MaterialTheme.typography.titleSmall
                )

                OutlinedTextField(
                    modifier = Modifier.constrainAs(inputNameRef) {
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(titleRef.bottom, margin = 8.dp)
                    },
                    value = state.brandName,
                    onValueChange = state.onBrandNameChange,
                    label = {
                        Text(text = "Nome")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    modifier = Modifier.constrainAs(inputQdtRef) {
                        start.linkTo(inputNameRef.start)
                        end.linkTo(inputNameRef.end)
                        top.linkTo(inputNameRef.bottom, margin = 8.dp)
                    },
                    value = state.brandQtd,
                    onValueChange = state.onBrandQtdChange,
                    label = {
                        Text(text = "Quantidade")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                TextButton(
                    modifier = Modifier.constrainAs(cancelButtonRef) {
                        start.linkTo(parent.start, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        top.linkTo(inputQdtRef.bottom, margin = 8.dp)
                    },
                    onClick = {
                        onDissmissDialog(false)
                        onCancelClick()
                    }
                ) {
                    Text(text = "Cancelar")
                }

                TextButton(
                    modifier = Modifier.constrainAs(confirmButtonRef) {
                        end.linkTo(parent.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        top.linkTo(inputQdtRef.bottom, margin = 8.dp)
                    },
                    onClick = {
                        onDissmissDialog(false)
                        onConfirmClick()
                    }
                ) {
                    Text(text = "Confirmar")
                }

            }
        }
    }
}

@Preview
@Composable
fun BrandDialogPreview() {
    StorageTheme {
        Surface {
            BrandDialog()
        }
    }
}