package br.com.market.storage.ui.screens.formproduct

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.storage.R
import br.com.market.storage.ui.components.CoilImageViewer
import br.com.market.storage.ui.components.OutlinedTextFieldValidation
import br.com.market.storage.ui.components.buttons.FloatingActionButtonSave
import br.com.market.storage.ui.states.FormProductUiState
import br.com.market.storage.ui.theme.StorageTheme

/**
 * Tab do Produto
 *
 * @param state Estado da tela onde a tab é usada.
 * @param onFABSaveProductClick Listener executado ao clicar no botão de salvar o produto.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabProduct(
    state: FormProductUiState = FormProductUiState(),
    onFABSaveProductClick: () -> Unit = { }
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButtonSave {
               onFABSaveProductClick()
            }
        }
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            val (imageRef, inputImage, inputProductName) = createRefs()

            CoilImageViewer(
                modifier = Modifier
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
                    .height(200.dp),
                data = state.productImage
            )

            OutlinedTextFieldValidation(
                value = state.productImage,
                onValueChange = state.onProductImageChange,
                error = state.productImageErrorMessage,
                label = { Text(stringResource(R.string.form_product_screen_tab_product_label_link_image)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.constrainAs(inputImage) {
                    start.linkTo(parent.start, margin = 8.dp)
                    top.linkTo(imageRef.bottom, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)

                    width = Dimension.fillToConstraints
                }
            )

            OutlinedTextFieldValidation(
                value = state.productName,
                onValueChange = state.onProductNameChange,
                error = state.productNameErrorMessage,
                label = { Text(stringResource(R.string.form_product_screen_tab_product_label_name)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.constrainAs(inputProductName) {
                    start.linkTo(inputImage.start)
                    top.linkTo(inputImage.bottom, margin = 8.dp)
                    end.linkTo(inputImage.end)

                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TabProductPreview() {
    StorageTheme {
        Surface {
            TabProduct(FormProductUiState())
        }
    }
}