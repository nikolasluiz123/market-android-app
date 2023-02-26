package br.com.market.storage.ui.screens.formproduct

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.storage.R
import br.com.market.storage.ui.components.OutlinedTextFieldValidation
import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.states.FormProductUiState
import br.com.market.storage.ui.theme.StorageTheme

@Composable
fun BrandDialog(
    state: FormProductUiState = FormProductUiState(),
    onDissmissDialog: () -> Unit = { },
    onCancelClick: () -> Unit = { },
    onConfirmClick: (BrandDomain) -> Unit = { }
) {
    Dialog(onDismissRequest = onDissmissDialog) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            ConstraintLayout(
                Modifier.fillMaxWidth()
            ) {
                val (titleRef, cancelButtonRef, confirmButtonRef,
                    inputNameRef, inputQdtRef) = createRefs()

                Text(
                    modifier = Modifier.constrainAs(titleRef) {
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(parent.top, margin = 8.dp)
                    },
                    text = stringResource(R.string.brand_dialog_title),
                    style = MaterialTheme.typography.titleSmall
                )

                OutlinedTextFieldValidation(
                    modifier = Modifier.constrainAs(inputNameRef) {
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        top.linkTo(titleRef.bottom, margin = 8.dp)

                        width = Dimension.fillToConstraints
                    },
                    value = state.brandName,
                    onValueChange = state.onBrandNameChange,
                    error = state.brandNameErrorMessage,
                    label = {
                        Text(text = stringResource(R.string.brand_dialog_label_name))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextFieldValidation(
                    modifier = Modifier.constrainAs(inputQdtRef) {
                        start.linkTo(inputNameRef.start)
                        end.linkTo(inputNameRef.end)
                        top.linkTo(inputNameRef.bottom, margin = 8.dp)

                        width = Dimension.fillToConstraints
                    },
                    value = state.brandQtd,
                    onValueChange = state.onBrandQtdChange,
                    error = state.qtdBrandErrorMessage,
                    label = {
                        Text(text = stringResource(R.string.brand_dialog_label_qtd))
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
                        onDissmissDialog()
                        onCancelClick()
                    }
                ) {
                    Text(text = stringResource(id = R.string.dialog_button_cancel_label))
                }

                TextButton(
                    modifier = Modifier.constrainAs(confirmButtonRef) {
                        end.linkTo(parent.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        top.linkTo(inputQdtRef.bottom, margin = 8.dp)
                    },
                    onClick = {
                        if (state.onValidateBrand()) {
                            onConfirmClick(
                                BrandDomain(
                                    id = state.brandId,
                                    name = state.brandName,
                                    count = state.brandQtd.toInt()
                                )
                            )
                            onDissmissDialog()
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.dialog_button_confirm_label))
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