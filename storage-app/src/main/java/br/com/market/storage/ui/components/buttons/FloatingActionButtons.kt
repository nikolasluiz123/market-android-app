package br.com.market.storage.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import br.com.market.storage.R
import br.com.market.storage.ui.theme.colorPrimary

@Composable
fun FloatingActionButton(
    containerColor: Color = colorPrimary,
    onClick: () -> Unit = { },
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        containerColor = containerColor,
        onClick = onClick
    ) {
        content()
    }
}

@Composable
fun FloatingActionButtonAdd(
    containerColor: Color = colorPrimary,
    onClick: () -> Unit = { }
) {
    FloatingActionButton(
        containerColor = containerColor,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.label_adicionar),
            tint = Color.White
        )
    }
}