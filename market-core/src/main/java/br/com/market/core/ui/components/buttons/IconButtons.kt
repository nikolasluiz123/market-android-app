package br.com.market.core.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.R
import br.com.market.core.theme.GREY_500
import br.com.market.core.theme.GREY_800
import br.com.market.core.theme.MarketTheme

/**
 * Botão com ícone de voltar.
 *
 * @param onClick Ação ao clicar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun IconButtonArrowBack(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.label_voltar)
        )
    }
}

/**
 * Botão com ícone de pesquisa.
 *
 * @param onClick Ação ao clicar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun IconButtonSearch(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.label_pesquisar)
        )
    }
}

/**
 * Botão com ícone de mais opções.
 *
 * @param onClick Ação ao clicar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun IconButtonMoreVert(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
    }
}

/**
 * Botão com ícone de fechar.
 *
 * @param onClick Ação ao clicar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun IconButtonClose(
    buttonModifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    iconColor: Color = Color.Black
) {
    IconButton(modifier = buttonModifier, onClick = onClick) {
        Icon(
            modifier = iconModifier,
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.label_limpar_pesquisa),
            tint = iconColor
        )
    }
}

/**
 * Botão com ícone de deletar.
 *
 * @param onClick Ação ao clicar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun IconButtonInactivate(
    iconColor: Color = GREY_800,
    disabledIconColor: Color = GREY_500,
    enabled: Boolean = true,
    onClick: () -> Unit = { }
) {
    IconButton(
        enabled = enabled,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(contentColor = iconColor, disabledContentColor = disabledIconColor)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_delete_32dp),
            contentDescription = stringResource(R.string.label_inactivate)
        )
    }
}

@Composable
fun IconButtonReactivate(
    iconColor: Color = GREY_800,
    disabledIconColor: Color = GREY_500,
    enabled: Boolean = true,
    onClick: () -> Unit = { }
) {
    IconButton(
        enabled = enabled,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(contentColor = iconColor, disabledContentColor = disabledIconColor)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_reactivate_32dp),
            contentDescription = stringResource(R.string.label_reactivate)
        )
    }
}

@Composable
fun IconButtonLogout(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.ExitToApp, contentDescription = stringResource(R.string.label_logout)
        )
    }
}

@Composable
fun IconButtonSync(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sync_24dp), contentDescription = stringResource(R.string.label_sync)
        )
    }
}

@Composable
fun IconButtonStorage(
    iconColor: Color = GREY_800,
    disabledIconColor: Color = GREY_500,
    enabled: Boolean = true,
    onClick: () -> Unit = { }
) {
    IconButton(
        enabled = enabled,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(contentColor = iconColor, disabledContentColor = disabledIconColor)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_storage_32dp),
            contentDescription = stringResource(R.string.label_storage)
        )
    }
}

@Composable
fun IconButtonCalendar(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_calendar_24dp), contentDescription = stringResource(R.string.label_calendar)
        )
    }
}

@Composable
fun IconButtonTime(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_time_24dp), contentDescription = stringResource(R.string.label_calendar)
        )
    }
}

/**
 * Menu de mais opções.
 *
 * @param menuItems
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun MenuIconButton(
    menuItems: @Composable () -> Unit = { }
) {
    var showMenu by remember { mutableStateOf(false) }

    IconButtonMoreVert { showMenu = !showMenu }

    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
        menuItems()
    }
}

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

@Preview
@Composable
fun IconButtonArrowBackPreview() {
    MarketTheme {
        Surface {
            IconButtonArrowBack()
        }
    }
}

@Preview
@Composable
fun IconButtonSearchPreview() {
    MarketTheme {
        Surface {
            IconButtonSearch()
        }
    }
}

@Preview
@Composable
fun IconButtonMoreVertPreview() {
    MarketTheme {
        Surface {
            IconButtonMoreVert()
        }
    }
}

@Preview
@Composable
fun IconButtonClosePreview() {
    MarketTheme {
        Surface {
            IconButtonClose()
        }
    }
}

@Preview
@Composable
fun IconButtonDeletePreview() {
    MarketTheme {
        Surface {
            IconButtonInactivate()
        }
    }
}

@Preview
@Composable
fun IconButtonSyncPreview() {
    MarketTheme {
        Surface {
            IconButtonSync()
        }
    }
}