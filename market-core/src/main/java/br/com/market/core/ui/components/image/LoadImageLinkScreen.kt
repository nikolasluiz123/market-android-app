package br.com.market.core.ui.components.image

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.R
import br.com.market.core.theme.GREY_300
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.CoilImageViewer
import br.com.market.core.ui.components.OutlinedTextFieldValidation
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.FloatingActionButtonSave
import br.com.market.core.ui.states.LoadImageLinkUIState
import br.com.market.core.ui.viewmodel.LoadImageLinkViewModel

@Composable
fun LoadImageLinkScreen(
    viewModel: LoadImageLinkViewModel,
    onNavigationIconClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    LoadImageLinkScreen(state = state, onNavigationIconClick = onNavigationIconClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadImageLinkScreen(
    state: LoadImageLinkUIState = LoadImageLinkUIState(),
    onNavigationIconClick: () -> Unit = { },
) {
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Carregar Imagem",
                subtitle = "Novo Produto",
                onNavigationIconClick = onNavigationIconClick,
                showMenuWithLogout = false
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = { },
                floatingActionButton = {
                    FloatingActionButtonSave()
                },
                containerColor = GREY_300
            )
        }
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val (imageRef, inputLinkRef) = createRefs()

            CoilImageViewer(
                Modifier
                    .constrainAs(imageRef) {
                        linkTo(start = parent.start, end = parent.end)
                        top.linkTo(parent.top)

                        width = Dimension.fillToConstraints

                    }
                    .height(250.dp)
                    .padding(8.dp),
                data = state.link
            )

            OutlinedTextFieldValidation(
                modifier = Modifier
                    .constrainAs(inputLinkRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(imageRef.bottom)

                        width = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 8.dp),
                value = state.link,
                onValueChange = state.onLinkChange,
                error = state.linkErrorMessage,
                label = { Text(text = stringResource(R.string.load_image_link_screen_label_link)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done
                )
            )
        }
    }
}

@Preview
@Composable
fun LoadImageLinkScreenPreview() {
    MarketTheme {
        Surface {
            LoadImageLinkScreen()
        }
    }
}