package br.com.market.market.compose.components.button.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import br.com.market.core.R

@Composable
fun IconButtonVisibility(passwordVisible: Boolean, onClick: () -> Unit) {
    lateinit var description: String
    lateinit var image: ImageVector

    if (passwordVisible) {
        image = ImageVector.vectorResource(R.drawable.ic_visibility_24)
        description = stringResource(R.string.description_icon_hide_password)
    } else {
        image = ImageVector.vectorResource(R.drawable.ic_visibility_off_24)
        description = stringResource(R.string.description_icon_show_password)
    }

    IconButton(onClick = onClick) {
        Icon(imageVector = image, description)
    }
}