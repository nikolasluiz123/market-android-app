package br.com.market.storage.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import br.com.market.storage.R

@Composable
fun IconButtonArrowBack(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(R.string.label_voltar)
        )
    }
}

@Composable
fun IconButtonSearch(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.label_pesquisar)
        )
    }
}

@Composable
fun IconButtonMoreVert(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
    }
}

@Composable
fun IconButtonClose(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.label_limpar_pesquisa)
        )
    }
}

@Composable
fun MenuIconButton(
    menuItens: @Composable () -> Unit = { }
) {
    var showMenu by remember { mutableStateOf(false) }

    IconButtonMoreVert { showMenu = !showMenu }

    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
        menuItens()
    }
}

@Composable
fun MenuIconButtonWithLogout(
    onLogoutClick: () -> Unit,
    menuItens: @Composable () -> Unit = { }
) {
    MenuIconButton {
        DropdownMenuItem(text = { Text(stringResource(R.string.label_logout)) }, onClick = onLogoutClick)
        menuItens()
    }
}