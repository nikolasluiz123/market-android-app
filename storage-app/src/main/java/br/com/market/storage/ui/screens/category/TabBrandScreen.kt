package br.com.market.storage.ui.screens.category

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.theme.MarketTheme
import br.com.market.storage.ui.states.category.CategoryUIState

@Composable
fun TabBrandScreen(state: CategoryUIState = CategoryUIState()) {

}

@Preview
@Composable
fun TabBrandScreenPreview() {
    MarketTheme {
        Surface {
            TabBrandScreen()
        }
    }
}