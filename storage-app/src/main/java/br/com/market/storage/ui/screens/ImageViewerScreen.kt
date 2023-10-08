package br.com.market.storage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.theme.*
import br.com.market.domain.ProductImageDomain
import br.com.market.market.compose.components.CoilImageViewer
import br.com.market.market.compose.components.MarketBottomAppBar
import br.com.market.market.compose.components.bottomsheet.BottomSheetLoadImage
import br.com.market.market.compose.components.button.fab.FloatingActionButtonSave
import br.com.market.market.compose.components.button.icons.IconButtonInactivate
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import br.com.market.storage.ui.states.ImageViewerUIState
import br.com.market.storage.ui.viewmodels.ImageViewerViewModel

@Composable
fun ImageViewerScreen(
    viewModel: ImageViewerViewModel,
    onBackClick: () -> Unit,
    onAfterDeleteImage: () -> Unit,
    onAfterSaveProductImage: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ImageViewerScreen(
        state = state,
        onBackClick = onBackClick,
        onButtonInactivateClick = {
            viewModel.toggleImageActive()
            onAfterDeleteImage()
        },
        onFABSaveClick = {
            viewModel.save(it)
            onAfterSaveProductImage()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerScreen(
    state: ImageViewerUIState = ImageViewerUIState(),
    onBackClick: () -> Unit = { },
    onButtonInactivateClick: () -> Unit = { },
    onFABSaveClick: (ProductImageDomain) -> Unit = { }
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Imagem do Produto",
                subtitle = state.productName,
                showMenuWithLogout = false,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    IconButtonInactivate(onClick = onButtonInactivateClick)
                },
                floatingActionButton = {
                    FloatingActionButtonSave {
                        onFABSaveClick(state.productImageDomain!!)
                    }
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
        ConstraintLayout(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val (headerRef, imageRef) = createRefs()
            ConstraintLayout(
                Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.secondary)
                    .constrainAs(headerRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(parent.top)
                    }
            ) {
                val (labelSwitchButtonRef, switchButtonRef) = createRefs()

                Text(
                    text = "Imagem Principal do Produto",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.constrainAs(labelSwitchButtonRef) {
                        start.linkTo(parent.start, margin = 56.dp)
                        top.linkTo(switchButtonRef.top)
                        bottom.linkTo(switchButtonRef.bottom)
                    },
                    color = MaterialTheme.colorScheme.onSecondary
                )

                val principalImage = state.productImageDomain?.principal ?: false
                var isPrincipalImage by remember(principalImage) {
                    mutableStateOf(principalImage)
                }

                Switch(
                    checked = isPrincipalImage,
                    onCheckedChange = {
                        isPrincipalImage = it
                        state.productImageDomain?.principal = it
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.background,
                        checkedBorderColor = Color.Transparent,
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier.constrainAs(switchButtonRef) {
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(parent.top)
                    }
                )
            }

            var openBottomSheet by rememberSaveable { mutableStateOf(false) }

            CoilImageViewer(
                containerModifier = Modifier.constrainAs(imageRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0F)
                    top.linkTo(headerRef.bottom, margin = 8.dp)
                },
                imageModifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clickable { openBottomSheet = true },
                data = state.productImageDomain?.byteArray,
            )

            if (openBottomSheet) {
                BottomSheetLoadImage(
                    onDismissRequest = {
                        openBottomSheet = false
                    },
                    onItemClickListener = {
//                        onBottomSheetLoadImageItemClick(it) { uri ->
//                            state.productImageDomain?.byteArray = context.readBytes(uri)
//                        }
                    }
                )
            }
        }
    }

}

@Preview
@Composable
fun ImageViewerScreenPreview() {
    MarketTheme {
        Surface {
            ImageViewerScreen()
        }
    }
}