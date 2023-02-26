package br.com.market.storage.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import br.com.market.storage.R
import br.com.market.storage.ui.theme.colorPrimary

@Composable
fun StorageAppFloatingActionButton(
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
    onClick: () -> Unit = { }
) {
    StorageAppFloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.label_adicionar),
            tint = Color.White
        )
    }
}

@Composable
fun FloatingActionButtonSave(onClick: () -> Unit = { }) {
    StorageAppFloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(R.string.label_save),
            tint = Color.White
        )
    }
}