package br.com.market.market.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.market.core.theme.GREY_900

@Composable
fun MarketSnackBar(data: SnackbarData) {
    Snackbar(modifier = Modifier.padding(8.dp), containerColor = GREY_900, contentColor = Color.White) {
        Text(text = data.visuals.message)
    }
}