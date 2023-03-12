package br.com.market.storage.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.storage.R
import br.com.market.storage.ui.theme.StorageTheme

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
fun IconButtonClose(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.label_limpar_pesquisa)
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
fun IconButtonDelete(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.label_delete)
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
    StorageTheme {
        Surface {
            IconButtonArrowBack()
        }
    }
}

@Preview
@Composable
fun IconButtonSearchPreview() {
    StorageTheme {
        Surface {
            IconButtonSearch()
        }
    }
}

@Preview
@Composable
fun IconButtonMoreVertPreview() {
    StorageTheme {
        Surface {
            IconButtonMoreVert()
        }
    }
}

@Preview
@Composable
fun IconButtonClosePreview() {
    StorageTheme {
        Surface {
            IconButtonClose()
        }
    }
}

@Preview
@Composable
fun IconButtonDeletePreview() {
    StorageTheme {
        Surface {
            IconButtonDelete()
        }
    }
}