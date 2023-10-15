package br.com.market.commerce.ui.screens.product

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.commerce.ui.viewmodel.products.ProductsAdvancedFilterViewModel
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.states.filter.AdvancedFilterUIState

@Composable
fun ProductsAdvancedFilterScreen(
    viewModel: ProductsAdvancedFilterViewModel,
) {
    val state by viewModel.uiState.collectAsState()

    ProductsAdvancedFilterScreen(state = state)
}

@Composable
fun ProductsAdvancedFilterScreen(
    state: AdvancedFilterUIState = AdvancedFilterUIState(),
) {
    ConstraintLayout(Modifier.fillMaxSize()) {

    }
}

@Preview
@Composable
fun AdvancedFilterScreenPreview() {
    Surface {
        MarketTheme {
            ProductsAdvancedFilterScreen()
        }
    }
}