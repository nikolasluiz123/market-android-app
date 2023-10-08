package br.com.market.market.compose.components.button.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.market.core.R
import br.com.market.core.theme.GREY_500

@Composable
fun IconButtonStorage(
    disabledIconColor: Color = GREY_500,
    enabled: Boolean = true,
    onClick: () -> Unit = { }
) {
    IconButton(
        enabled = enabled,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(disabledContentColor = disabledIconColor)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_storage_32dp),
            contentDescription = stringResource(R.string.label_storage)
        )
    }
}