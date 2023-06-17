package br.com.market.storage.ui.screens.product

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.extensions.parseToDouble
import br.com.market.core.extensions.readBytes
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.OutlinedTextFieldValidation
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.bottomsheet.BottomSheetLoadImage
import br.com.market.core.ui.components.bottomsheet.IEnumOptionsBottomSheet
import br.com.market.core.ui.components.buttons.*
import br.com.market.core.ui.components.dialog.DialogMessage
import br.com.market.domain.ProductDomain
import br.com.market.domain.ProductImageDomain
import br.com.market.enums.EnumUnit
import br.com.market.storage.R
import br.com.market.storage.ui.component.ProductImageHorizontalGallery
import br.com.market.storage.ui.states.product.ProductUIState
import br.com.market.storage.ui.viewmodels.product.ProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProductScreen(
    viewModel: ProductViewModel,
    onBackClick: () -> Unit,
    onStorageButtonClick: (String, String, String) -> Unit,
    onBottomSheetLoadImageItemClick: (IEnumOptionsBottomSheet, (Uri) -> Unit) -> Unit,
    onProductImageClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ProductScreen(
        state = state,
        onBackClick = onBackClick,
        onToggleActive = viewModel::toggleProductActive,
        onStorageButtonClick = onStorageButtonClick,
        onBottomSheetLoadImageItemClick = onBottomSheetLoadImageItemClick,
        onSaveProductClick = viewModel::saveProduct,
        onProductImageClick = onProductImageClick,
        onAddProductImage = viewModel::addImage,
        onToggleActiveProductImage = viewModel::toggleImageActive
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    state: ProductUIState = ProductUIState(),
    onBackClick: () -> Unit = { },
    onToggleActive: () -> Unit = { },
    onStorageButtonClick: (String, String, String) -> Unit = { _,_,_ -> },
    onBottomSheetLoadImageItemClick: (IEnumOptionsBottomSheet, (Uri) -> Unit) -> Unit = { _, _ -> },
    onSaveProductClick: () -> Unit = { },
    onProductImageClick: (String) -> Unit = { },
    onAddProductImage: (ByteArray) -> Unit = { },
    onToggleActiveProductImage: (ByteArray, String?) -> Unit = { _, _ -> }
) {
    var isEditMode by remember(state.productDomain) {
        mutableStateOf(state.productDomain != null)
    }

    var isActive by remember(state.productDomain?.active) {
        mutableStateOf(state.productDomain?.active ?: true)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
                        onClick = {
                            onStorageButtonClick(state.categoryId!!, state.brandDomain?.id!!, state.productDomain?.id!!)
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonSave(
                        onClick = {
                            isEditMode = saveProduct(state, isActive, isEditMode, onSaveProductClick, scope, snackbarHostState)
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
                .verticalScroll(scrollState)
        ) {
            val (galleryRef, inputNameRef, inputPriceRef, inputQuantityRef, inputUnityRef) = createRefs()
            createHorizontalChain(inputQuantityRef, inputUnityRef)

            var openBottomSheet by rememberSaveable { mutableStateOf(false) }
            val images = remember(state.images) {
                val stateList = mutableStateListOf<ProductImageDomain>()
                stateList.addAll(state.images)
                stateList
            }

            ProductImageHorizontalGallery(
                images = images,
                onLoadClick = { openBottomSheet = true },
                onImageClick = {
                    if (isEditMode) {
                        onProductImageClick(it)
                    }
                },
                onDeleteButtonClick = { byteArray, id ->
                    if (images.size == 1 && id != null) {
                        state.onShowDialog("Alerta", "O produto deve conter ao menos uma imagem")
                        return@ProductImageHorizontalGallery
                    }

                    onToggleActiveProductImage(byteArray, id)
                    images.removeIf { image -> image.byteArray.contentEquals(byteArray) }
                },
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
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
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
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                enabled = isActive,
                modifier = Modifier
                    .constrainAs(inputQuantityRef) {
                        top.linkTo(inputPriceRef.bottom)
                        start.linkTo(parent.start)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.5F
                    }
                    .padding(horizontal = 8.dp)
            )

            var expanded by remember { mutableStateOf(false) }
            val units = EnumUnit.values()

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .constrainAs(inputUnityRef) {
                        top.linkTo(inputPriceRef.bottom)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.5F
                    }
                    .padding(end = 8.dp)
            ) {
                OutlinedTextFieldValidation(
                    value = state.productQuantityUnit?.let { stringResource(id = it.labelResId) } ?: "",
                    label = { Text(text = stringResource(R.string.product_screen_label_measure)) },
                    onValueChange = { },
                    error = state.productQuantityUnitErrorMessage,
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    units.forEach { enumUnit ->
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = enumUnit.labelResId)) },
                            onClick = {
                                state.productQuantityUnit = enumUnit
                                expanded = false
                            }
                        )
                    }
                }
            }

            DialogMessage(
                title = state.dialogTitle,
                show = state.showDialog,
                onDismissRequest = { state.onHideDialog() },
                message = state.dialogMessage
            )

            if (openBottomSheet) {
                BottomSheetLoadImage(
                    onDismissRequest = {
                        openBottomSheet = false
                    },
                    onItemClickListener = {
                        onBottomSheetLoadImageItemClick(it) { uri ->
                            if (state.images.size < 3) {
                                onAddProductImage(context.readBytes(uri)!!)
                            } else {
                                state.onShowDialog("Atenção", "São permitidas apenas 3 fotos por produto.")
                            }
                        }
                    }
                )
            }
        }
    }
}

private fun saveProduct(
    state: ProductUIState,
    isActive: Boolean,
    isEditMode: Boolean,
    onSaveProductClick: () -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
): Boolean {
    if (state.onValidate() && isActive) {

        state.productDomain = if (isEditMode) {
            state.productDomain?.copy(
                name = state.productName,
                price = state.productPrice.parseToDouble(),
                quantity = state.productQuantity.parseToDouble(),
                quantityUnit = state.productQuantityUnit,
                images = state.images
            )
        } else {
            ProductDomain(
                name = state.productName,
                price = state.productPrice.parseToDouble(),
                quantity = state.productQuantity.parseToDouble(),
                quantityUnit = state.productQuantityUnit,
                images = state.images
            )
        }

        onSaveProductClick()

        scope.launch {
            snackbarHostState.showSnackbar("Produto Salvo com Sucesso")
        }
    }

    return state.productDomain != null
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