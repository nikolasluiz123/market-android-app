package br.com.market.storage.ui.screens.product

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.OutlinedTextFieldValidation
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.*
import br.com.market.core.ui.components.image.HorizontalGallery
import br.com.market.storage.R
import br.com.market.storage.ui.states.product.ProductUIState
import br.com.market.storage.ui.viewmodels.product.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductScreen(
    viewModel: ProductViewModel,
    onBackClick: () -> Unit,
    onStorageButtonClick: () -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()

    ProductScreen(
        state = state,
        onBackClick = onBackClick,
        onToggleActive = {

        },
        onStorageButtonClick = onStorageButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    state: ProductUIState = ProductUIState(),
    onBackClick: () -> Unit = { },
    onToggleActive: () -> Unit = { },
    onStorageButtonClick: () -> Unit = { }
) {
    var isEditMode by remember(state.productDomain) {
        mutableStateOf(state.productDomain != null)
    }

    var isActive by remember(state.productDomain?.active) {
        mutableStateOf(state.productDomain?.active ?: true)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            val title = "Marca ${state.brandDomain?.name}"
            val subtitle = if (isEditMode) state.productDomain?.name else "Novo Produto"

            SimpleMarketTopAppBar(
                title = title,
                subtitle = subtitle,
                showMenuWithLogout = false,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    if (isActive) {
                        IconButtonInactivate(
                            enabled = isEditMode,
                            onClick = {
                                onToggleActive()
                                isActive = false

                                scope.launch {
                                    snackbarHostState.showSnackbar("Produto Inativado com Sucesso")
                                }
                            }
                        )
                    } else {
                        IconButtonReactivate(
                            enabled = isEditMode,
                            onClick = {
                                onToggleActive()
                                isActive = true

                                scope.launch {
                                    snackbarHostState.showSnackbar("Produto Reativado com Sucesso")
                                }
                            }
                        )
                    }

                    IconButtonStorage(
                        enabled = isEditMode,
                        onClick = onStorageButtonClick
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonSave(
                        onClick = {

                        }
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(modifier = Modifier.padding(8.dp)) {
                    Text(text = it.visuals.message)
                }
            }
        }
    ) { padding ->
        val scrollState = rememberScrollState()

        ConstraintLayout(
            Modifier
                .padding(padding)
                .verticalScroll(scrollState)) {
            val (galleryRef, inputNameRef, inputPriceRef, inputQuantityRef, inputUnityRef) = createRefs()
            createHorizontalChain(inputQuantityRef, inputUnityRef)

            HorizontalGallery(
                images = listOf(
                    "https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                    "https://images.pexels.com/photos/2294477/pexels-photo-2294477.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                    "https://images.pexels.com/photos/3584910/pexels-photo-3584910.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                ),
                onLoadClick = { },
                modifier = Modifier
                    .constrainAs(galleryRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .padding(8.dp)
            )

            OutlinedTextFieldValidation(
                value = state.productName,
                onValueChange = state.onProductNameChange,
                error = state.productNameErrorMessage,
                label = { Text(text = stringResource(R.string.product_screen_label_name)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                ),
                enabled = isActive,
                modifier = Modifier.constrainAs(inputNameRef) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(galleryRef.bottom)

                    width = Dimension.fillToConstraints
                }
            )

            OutlinedTextFieldValidation(
                value = state.productPrice,
                onValueChange = state.onProductPriceChange,
                error = state.productPriceErrorMessage,
                label = { Text(text = stringResource(R.string.product_screen_label_price)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                ),
                enabled = isActive,
                modifier = Modifier.constrainAs(inputPriceRef) {
                    start.linkTo(inputNameRef.start)
                    end.linkTo(inputNameRef.end)
                    top.linkTo(inputNameRef.bottom)

                    width = Dimension.fillToConstraints
                }
            )

            OutlinedTextFieldValidation(
                value = state.productQuantity,
                onValueChange = state.onProductQuantityChange,
                error = state.productQuantityErrorMessage,
                label = { Text(text = stringResource(R.string.product_screen_label_quantity)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                ),
                enabled = isActive,
                modifier = Modifier.constrainAs(inputQuantityRef) {
                    top.linkTo(inputPriceRef.bottom)
                    start.linkTo(parent.start)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.5F
                }.padding(horizontal = 8.dp)
            )

            var expanded by remember { mutableStateOf(false) }
            val units = listOf("Quilo", "Grama", "Unidade", "Litro", "Mililitro")
            var selectedText by remember { mutableStateOf("") }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.constrainAs(inputUnityRef) {
                    top.linkTo(inputPriceRef.bottom)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.5F
                }.padding(end = 8.dp)
            ) {
                OutlinedTextFieldValidation(
                    value = selectedText,
                    label = { Text(text = stringResource(R.string.product_screen_label_measure)) },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { /*TODO*/ }) {
                    units.forEach {item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProductScreenPreview() {
    MarketTheme {
        Surface {
            ProductScreen()
        }
    }
}