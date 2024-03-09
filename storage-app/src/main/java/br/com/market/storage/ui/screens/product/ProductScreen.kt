package br.com.market.storage.ui.screens.product

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.callbacks.INumberInputNavigationCallback
import br.com.market.core.callbacks.IServiceOperationCallback
import br.com.market.core.callbacks.ITextInputNavigationCallback
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.extensions.launchImageOnly
import br.com.market.core.extensions.openCamera
import br.com.market.core.extensions.processBytesOfImage
import br.com.market.core.extensions.requestCameraPermission
import br.com.market.core.extensions.requestGalleryPermission
import br.com.market.core.extensions.verifyCameraPermissionGranted
import br.com.market.core.extensions.verifyGalleryPermissionGranted
import br.com.market.core.inputs.arguments.InputArgs
import br.com.market.core.inputs.arguments.InputNumberArgs
import br.com.market.core.inputs.formatter.InputNumberFormatter
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.bottomsheet.loadimage.EnumOptionsBottomSheetLoadImage
import br.com.market.core.utils.MediaUtils.openCameraLauncher
import br.com.market.core.utils.MediaUtils.openGalleryLauncher
import br.com.market.core.utils.PermissionUtils.requestPermissionLauncher
import br.com.market.enums.EnumUnit
import br.com.market.market.compose.components.FormField
import br.com.market.market.compose.components.MarketBottomAppBar
import br.com.market.market.compose.components.MarketSnackBar
import br.com.market.market.compose.components.SelectOneOption
import br.com.market.market.compose.components.bottomsheet.BottomSheetLoadImage
import br.com.market.market.compose.components.button.fab.FloatingActionButtonSave
import br.com.market.market.compose.components.button.icons.IconButtonInactivate
import br.com.market.market.compose.components.button.icons.IconButtonReactivate
import br.com.market.market.compose.components.button.icons.IconButtonStorage
import br.com.market.market.compose.components.dialog.MarketDialog
import br.com.market.market.compose.components.loading.MarketLinearProgressIndicator
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
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
    onProductImageClick: (String) -> Unit,
    onBottomSheetLoadImageLinkClick: (((ByteArray) -> Unit)) -> Unit,
    textInputCallback: ITextInputNavigationCallback,
    numberInputCallback: INumberInputNavigationCallback
) {
    val state by viewModel.uiState.collectAsState()

    ProductScreen(
        state = state,
        onBackClick = onBackClick,
        onToggleActive = viewModel::toggleProductActive,
        onStorageButtonClick = onStorageButtonClick,
        saveProductCallback = viewModel::saveProduct,
        onProductImageClick = onProductImageClick,
        onAddProductImage = viewModel::addImage,
        onToggleActiveProductImage = viewModel::toggleImageActive,
        onBottomSheetLoadImageLinkClick = onBottomSheetLoadImageLinkClick,
        textInputCallback = textInputCallback,
        numberInputCallback = numberInputCallback
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    state: ProductUIState = ProductUIState(),
    onBackClick: () -> Unit = { },
    onToggleActive: () -> Unit = { },
    onStorageButtonClick: (String, String, String) -> Unit = { _, _, _ -> },
    saveProductCallback: IServiceOperationCallback? = null,
    onProductImageClick: (String) -> Unit = { },
    onAddProductImage: (ByteArray) -> Unit = { },
    onToggleActiveProductImage: (ByteArray, String?) -> Unit = { _, _ -> },
    onBottomSheetLoadImageLinkClick: (((ByteArray) -> Unit)) -> Unit = { },
    textInputCallback: ITextInputNavigationCallback? = null,
    numberInputCallback: INumberInputNavigationCallback? = null
) {
    LaunchedEffect(state.internalErrorMessage) {
        if (state.internalErrorMessage.isNotEmpty()) {
            state.onShowDialog?.onShow(type = EnumDialogType.ERROR, message = state.internalErrorMessage, onConfirm = {}, onCancel = {})
            state.internalErrorMessage = ""
        }
    }

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
            MarketLinearProgressIndicator(show = state.showLoading)

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
                            isEditMode = saveProduct(state, isActive, saveProductCallback, scope, snackbarHostState, context)
                        }
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                MarketSnackBar(it)
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
            var showSelectOneQuantityUnit by remember { mutableStateOf(false) }

            MarketDialog(
                type = state.dialogType,
                show = state.showDialog,
                onDismissRequest = { state.onHideDialog() },
                message = state.dialogMessage,
                onConfirm = state.onConfirm,
                onCancel = state.onCancel
            )

            ProductImageHorizontalGallery(
                modifier = Modifier
                    .constrainAs(galleryRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .padding(8.dp),
                images = state.images,
                onLoadClick = { openBottomSheet = true },
                onImageClick = {
                    if (isEditMode) {
                        onProductImageClick(it)
                    }
                },
                onDeleteButtonClick = { byteArray, id ->
                    if (state.images.size == 1 && id != null) {
                        state.onShowDialog?.onShow(
                            type = EnumDialogType.ERROR,
                            message = context.getString(R.string.product_screen_required_photo_validation_message),
                            onConfirm = {},
                            onCancel = {}
                        )
                        return@ProductImageHorizontalGallery
                    }

                    onToggleActiveProductImage(byteArray, id)
                    state.images.removeIf { image -> image.byteArray.contentEquals(byteArray) }
                },
                contentScale = ContentScale.Fit
            )

            FormField(
                modifier = Modifier.constrainAs(inputNameRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(galleryRef.bottom)

                    width = Dimension.fillToConstraints
                },
                labelResId = R.string.product_screen_label_name,
                field = state.name,
                onNavigateClick = {
                    textInputCallback?.onNavigate(
                        args = InputArgs(
                            titleResId = R.string.product_screen_title_input_name,
                            value = state.name.value,
                            maxLength = 255,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            )
                        ),
                        callback = { value -> state.name.onChange(value ?: "") }
                    )
                }
            )

            FormField(
                modifier = Modifier.constrainAs(inputPriceRef) {
                    start.linkTo(inputNameRef.start)
                    end.linkTo(inputNameRef.end)
                    top.linkTo(inputNameRef.bottom)

                    width = Dimension.fillToConstraints
                },
                labelResId = R.string.product_screen_label_price,
                field = state.price,
                onNavigateClick = {
                    numberInputCallback?.onNavigate(
                        args = InputNumberArgs(
                            titleResId = R.string.product_screen_title_input_price,
                            value = state.price.value,
                            integer = false,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done,
                                autoCorrect = false
                            )
                        ),
                        callback = {
                            val formatter = InputNumberFormatter(integer = false)
                            state.price.onChange(formatter.formatToString(it) ?: "")
                        }
                    )
                }
            )

            FormField(
                modifier = Modifier.constrainAs(inputQuantityRef) {
                    top.linkTo(inputPriceRef.bottom)
                    start.linkTo(parent.start)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.5F
                },
                labelResId = R.string.product_screen_label_quantity,
                field = state.quantity,
                onNavigateClick = {
                    numberInputCallback?.onNavigate(
                        args = InputNumberArgs(
                            titleResId = R.string.product_screen_title_input_quantity,
                            value = state.quantity.value,
                            integer = false
                        ),
                        callback = {
                            val formatter = InputNumberFormatter(integer = false)
                            state.quantity.onChange(formatter.formatToString(it) ?: "")
                        }
                    )
                }
            )

            val units = EnumUnit.entries.map { Pair(stringResource(id = it.labelResId), it.ordinal) }

            FormField(
                modifier = Modifier.constrainAs(inputUnityRef) {
                    top.linkTo(inputPriceRef.bottom)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.5F
                },
                labelResId = R.string.product_screen_label_measure,
                field =  state.quantityUnit,
                onNavigateClick = { showSelectOneQuantityUnit = true }
            )

            if (showSelectOneQuantityUnit) {
                SelectOneOption(
                    items = units,
                    onDismiss = { showSelectOneQuantityUnit = false },
                    onItemClick = {
                        state.quantityUnit.onChange(it.first)
                        showSelectOneQuantityUnit = false
                    }
                )
            }

            MarketDialog(
                type = state.dialogType,
                show = state.showDialog,
                onDismissRequest = { state.onHideDialog() },
                message = state.dialogMessage
            )

            var cameraURI by remember { mutableStateOf<Uri?>(null) }
            val requestPermissionLauncher = requestPermissionLauncher()

            val openCamera = openCameraLauncher { success ->
                if (success) {
                    cameraURI?.let { uri ->
                        onAddProductImage(context.processBytesOfImage(uri)!!)
                    }
                }
            }

            val openGallery = openGalleryLauncher { uri ->
                uri?.let { onAddProductImage(context.processBytesOfImage(it)!!) }
            }

            if (openBottomSheet) {
                if (state.images.size < 3) {
                    BottomSheetLoadImage(
                        onDismissRequest = {
                            openBottomSheet = false
                        },
                        onItemClickListener = {
                            when (it) {
                                EnumOptionsBottomSheetLoadImage.CAMERA -> {
                                    if (context.verifyCameraPermissionGranted()) {
                                        context.openCamera(openCamera) { uri ->
                                            cameraURI = uri
                                        }
                                    } else {
                                        requestPermissionLauncher.requestCameraPermission()
                                    }
                                }

                                EnumOptionsBottomSheetLoadImage.GALLERY -> {
                                    if (context.verifyGalleryPermissionGranted()) {
                                        openGallery.launchImageOnly()
                                    } else {
                                        requestPermissionLauncher.requestGalleryPermission()
                                    }
                                }

                                EnumOptionsBottomSheetLoadImage.LINK -> {
                                    onBottomSheetLoadImageLinkClick(onAddProductImage)
                                }
                            }
                        }
                    )
                } else {
                    state.onShowDialog?.onShow(
                        type = EnumDialogType.ERROR,
                        message = stringResource(R.string.product_screen_qtd_photo_validation_message),
                        onConfirm = {},
                        onCancel = {}
                    )
                }
            }
        }
    }
}

private fun saveProduct(
    state: ProductUIState,
    isActive: Boolean,
    saveProductCallback: IServiceOperationCallback?,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context
): Boolean {
    if (state.onValidate() && isActive) {
        state.onToggleLoading()

        saveProductCallback?.onExecute(
            onSuccess = {
                state.onToggleLoading()
                scope.launch {
                    snackbarHostState.showSnackbar(context.getString(R.string.product_screen_save_success_message))
                }
            },
            onError = { message ->
                state.onToggleLoading()
                state.onShowDialog?.onShow(
                    type = EnumDialogType.ERROR,
                    message = message,
                    onConfirm = {},
                    onCancel = {}
                )
            }
        )
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