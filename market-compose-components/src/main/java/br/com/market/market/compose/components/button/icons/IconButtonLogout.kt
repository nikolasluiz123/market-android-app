package br.com.market.market.compose.components.button.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.market.core.R

@Composable
fun IconButtonLogout(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = stringResource(R.string.label_logout)
        )
    }
}