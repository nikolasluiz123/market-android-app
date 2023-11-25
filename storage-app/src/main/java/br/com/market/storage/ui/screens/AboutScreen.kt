package br.com.market.storage.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.CoilImageViewer
import br.com.market.market.compose.components.LabeledText
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import br.com.market.storage.R
import br.com.market.storage.ui.states.AboutUIState
import br.com.market.storage.ui.viewmodels.AboutViewModel
import java.util.UUID

@Composable
fun AboutScreen(
    viewModel: AboutViewModel,
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    AboutScreen(
        state = state,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    state: AboutUIState = AboutUIState(),
    onBackClick: () -> Unit = { },
) {
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(R.string.about_screen_top_bar_title),
                onBackClick = onBackClick,
                showMenuWithLogout = false
            )
        }
    ) { padding ->
        ConstraintLayout(Modifier.padding(padding).fillMaxSize()) {
            val (imageLogoRef, companyNameRef, marketNameRef, marketAddressRef, deviceIdRef, deviceNameRef) = createRefs()

            CoilImageViewer(
                containerModifier = Modifier.constrainAs(imageLogoRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 8.dp)
                }.fillMaxWidth(),
                imageModifier = Modifier.fillMaxWidth().height(200.dp),
                data = state.imageLogo,
                contentScale = ContentScale.Fit
            )

            LabeledText(
                modifier = Modifier.constrainAs(companyNameRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0.0F, startMargin = 8.dp)
                    top.linkTo(imageLogoRef.bottom, margin = 16.dp)
                },
                label = "Empresa",
                value = state.companyName
            )

            LabeledText(
                modifier = Modifier.constrainAs(marketNameRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0.0F, startMargin = 8.dp)
                    top.linkTo(companyNameRef.bottom, margin = 16.dp)
                },
                label = "Loja",
                value = state.marketName
            )

            LabeledText(
                modifier = Modifier.constrainAs(marketAddressRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0.0F, startMargin = 8.dp)
                    top.linkTo(marketNameRef.bottom, margin = 16.dp)
                },
                label = "Endereço",
                value = state.marketAddress
            )

            LabeledText(
                modifier = Modifier.constrainAs(deviceIdRef) {
                    linkTo(start = companyNameRef.start, end = parent.end, bias = 0.0F)
                    top.linkTo(marketAddressRef.bottom, margin = 16.dp)
                },
                label = "Nome do Dispositivo",
                value = state.deviceName
            )

            LabeledText(
                modifier = Modifier.constrainAs(deviceNameRef) {
                    linkTo(start = deviceIdRef.start, end = parent.end, bias = 0.0F)
                    top.linkTo(deviceIdRef.bottom, margin = 16.dp)
                },
                label = "Identificador",
                value = state.deviceId
            )
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    MarketTheme {
        Surface {
            AboutScreen(
                state = AboutUIState(
                    imageLogo = R.drawable.logo_amigao,
                    companyName = "Assuvali",
                    marketName = "Supermercado TOP",
                    marketAddress = "Rua Francisco Vahldieck, 1881",
                    deviceId = UUID.randomUUID().toString(),
                    deviceName = "Dispositivo Nikolas Estoque"
                )
            )
        }
    }
}