package br.com.market.storage.ui.screens.brand

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.core.ui.components.buttons.IconButtonInactivate

@Composable
fun BrandScreenTabProduct(onFabAddClick: () -> Unit = { }) {
    Scaffold(
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    IconButtonInactivate(
                        onClick = {  },
                        enabled = false
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonAdd(onClick = onFabAddClick)
                }
            )
        }
    ) { padding ->
        ConstraintLayout(Modifier.padding(padding)) {

        }
    }
}

@Preview
@Composable
fun BrandScreenTabProductPreview() {

}