package br.com.market.storage.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.storage.R
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.ui.states.FormProductUiState
import br.com.market.storage.ui.theme.CINZA_300
import br.com.market.storage.ui.theme.StorageTheme
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormProduct(
    state: FormProductUiState = FormProductUiState(),
    onFABSaveProductClick: (ProductDomain) -> Unit = { }
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(100),
            onClick = {
                if (state.onValidate()) {
                    onFABSaveProductClick(
                        ProductDomain(
                            id = state.productId,
                            name = state.productName,
                            imageUrl = state.productImage
                        )
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White
            )
        }
    }) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            val (imageRef, inputImage, inputProductName) = createRefs()

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.productImage)
                    .crossfade(true)
                    .placeholder(R.drawable.placeholder)
                    .build(),
                contentDescription = "Imagem do Produto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
                    .height(200.dp)
                    .background(color = CINZA_300)
            )

            OutlinedTextFieldValidation(
                value = state.productImage,
                onValueChange = state.onProductImageChange,
                error = state.productImageErrorMessage,
                label = {
                    Text("Link da Imagem")
                },
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
                label = {
                    Text("Nome")
                },
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
fun FormProductPreview() {
    StorageTheme {
        Surface {
            FormProduct(FormProductUiState())
        }
    }
}