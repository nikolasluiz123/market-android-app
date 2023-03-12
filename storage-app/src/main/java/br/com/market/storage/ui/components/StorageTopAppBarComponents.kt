package br.com.market.storage.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.storage.R
import br.com.market.storage.ui.components.buttons.*
import br.com.market.storage.ui.theme.StorageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageTopAppBar(
    title: @Composable () -> Unit,
    onNavigationIconClick: () -> Unit,
    onLogoutClick: () -> Unit = { },
    actions: @Composable () -> Unit = { },
    menuItems: @Composable () -> Unit = { },
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        titleContentColor = Color.White,
        actionIconContentColor = Color.White,
        navigationIconContentColor = Color.White
    ),
    showNavigationIcon: Boolean = true,
    showMenuWithLogout: Boolean = true,
) {
    TopAppBar(
        title = title,
        colors = colors,
        navigationIcon = {
            if (showNavigationIcon) {
                IconButtonArrowBack(onClick = onNavigationIconClick)
            }
        },
        actions = {
            actions()

            if (showMenuWithLogout) {
                MenuIconButtonWithDefaultActions(
                    onLogoutClick = onLogoutClick,
                    menuItens = menuItems,
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableStorageTopAppBar(
    openSearch: Boolean,
    searchText: String,
    title: String,
    showNavigationIcon: Boolean = false,
    onSearchChange: (String) -> Unit,
    onToggleSearch: () -> Unit,
    onLogoutClick: () -> Unit,
    onNavigationIconClick: () -> Unit = { },
    appBarTextFieldHint: String = stringResource(R.string.top_app_bar_text_field_hint),
    showOnlyCustomActions: Boolean = false,
    customActions: @Composable () -> Unit = { },
    menuItems: @Composable () -> Unit = { }
) {
    StorageTopAppBar(
        title = {
            TitleSearchableTopAppBar(
                openSearch = openSearch,
                searchText = searchText,
                onSearchChange = onSearchChange,
                title = title,
                appBarTextFieldHint = appBarTextFieldHint
            )
        },
        showNavigationIcon = openSearch || showNavigationIcon,
        onNavigationIconClick = if (openSearch) onToggleSearch else onNavigationIconClick,
        actions = {
            if (!showOnlyCustomActions) {
                if (!openSearch) {
                    IconButtonSearch(onClick = onToggleSearch)
                } else {
                    IconButtonClose { onSearchChange("") }
                }
            }
            
            customActions()
        },
        onLogoutClick = onLogoutClick,
        menuItems = menuItems
    )
}

@Composable
fun TitleSearchableTopAppBar(
    openSearch: Boolean,
    searchText: String,
    onSearchChange: (String) -> Unit,
    title: String,
    appBarTextFieldHint: String = stringResource(R.string.top_app_bar_text_field_hint)
) {
    if (openSearch) {
        AppBarTextField(
            value = searchText,
            onValueChange = onSearchChange,
            hint = appBarTextFieldHint
        )
    } else {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun StorageTopAppBarPreview() {
    StorageTheme {
        Surface {
            StorageTopAppBar(
                title = { Text("Título da Tela") },
                onNavigationIconClick = { },
                onLogoutClick = { }
            )
        }
    }
}

@Preview
@Composable
fun SearchableStorageTopAppBarPreview() {
    StorageTheme {
        Surface {
            SearchableStorageTopAppBar(
                openSearch = true,
                searchText = "",
                title = "Título da Tela",
                onSearchChange = { },
                onToggleSearch = { },
                onLogoutClick = { }
            )
        }
    }
}