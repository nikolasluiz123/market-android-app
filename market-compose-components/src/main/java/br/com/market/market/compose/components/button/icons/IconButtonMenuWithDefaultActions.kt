package br.com.market.market.compose.components.button.icons

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.market.core.R

/**
 * Menu de mais opções com a ação de logout, que é uma
 * ação comum.
 *
 * @param onLogoutClick
 * @param menuItems
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun MenuIconButtonWithDefaultActions(
    onLogoutClick: () -> Unit = { },
    menuItems: @Composable () -> Unit = { }
) {
    MenuIconButton {
        menuItems()
        DropdownMenuItem(text = { Text(stringResource(R.string.label_logout)) }, onClick = onLogoutClick)
    }
}