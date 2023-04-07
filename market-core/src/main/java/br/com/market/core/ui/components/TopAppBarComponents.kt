package br.com.market.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.buttons.IconButtonArrowBack
import br.com.market.core.ui.components.buttons.IconButtonClose
import br.com.market.core.ui.components.buttons.IconButtonSearch
import br.com.market.core.ui.components.buttons.MenuIconButtonWithDefaultActions

/**
 * TopAppBar padrão do APP.
 *
 * @param title Título da app bar
 * @param onNavigationIconClick Ação ao clicar no ícone da esquerda.
 * @param onLogoutClick Ação ao clicar no item de menu Logout.
 * @param actions Ações exibidas a direita da barra.
 * @param menuItems Itens de menu exibidos dentro do MoreOptions.
 * @param colors Cores da barra.
 * @param showNavigationIcon Flag para exibir ícone de navação ou não.
 * @param showMenuWithLogout Flag para exibir o menu com a opção de Logout.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketTopAppBar(
    title: @Composable () -> Unit,
    onNavigationIconClick: () -> Unit,
    onLogoutClick: () -> Unit = { },
    actions: @Composable () -> Unit = { },
    menuItems: @Composable () -> Unit = { },
    colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors(
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
                    menuItems = menuItems,
                )
            }
        }
    )
}

/**
 * Top App Bar com uma passagem facilitada de título e
 * subtítulo.
 *
 * @see MarketTopAppBar
 *
 * @param title String com o título da barra
 * @param subtitle String com o subtítulo da barra
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleMarketTopAppBar(
    title: String,
    subtitle: String? = null,
    onNavigationIconClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    actions: @Composable () -> Unit = { },
    menuItems: @Composable () -> Unit = { },
    colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        titleContentColor = Color.White,
        actionIconContentColor = Color.White,
        navigationIconContentColor = Color.White
    ),
    showNavigationIcon: Boolean = true,
    showMenuWithLogout: Boolean = true,
) {
    TopAppBar(
        title = {
            Column {
                Text(text = title, style = MaterialTheme.typography.titleSmall)

                if (subtitle != null) {
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
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
                    menuItems = menuItems,
                )
            }
        }
    )
}

/**
 * Searchable storage top app bar
 *
 * @param openSearch Flag que indica se deve ser aberto o campo de pesquisa e ajustados os itens de menu.
 * @param searchText Texto pesquisado.
 * @param title Título da app bar
 * @param showNavigationIcon Flag para exibir ícone de navação ou não.
 * @param onSearchChange Listener executado ao alterar o texto do campo de pesquisa.
 * @param onToggleSearch Exibir ou esconder o campo de pesquisa.
 * @param onLogoutClick Ação ao clicar no item de menu Logout.
 * @param onNavigationIconClick Ação ao clicar no ícone da esquerda.
 * @param appBarTextFieldHint Hint do campo de pesquisa.
 * @param showOnlyCustomActions Flag para exibir apenas ações específicas da tela no menu a direita.
 * @param customActions Ações específicas de uma tela.
 * @param menuItems Itens de menu exibidos dentro do MoreOptions.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableMarketTopAppBar(
    openSearch: Boolean,
    searchText: String,
    title: String,
    subtitle: String? = null,
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
    MarketTopAppBar(
        title = {
            TitleSearchableTopAppBar(
                openSearch = openSearch,
                searchText = searchText,
                onSearchChange = onSearchChange,
                title = title,
                subtitle = subtitle,
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

/**
 * Title searchable top app bar
 *
 * @param openSearch Flag que indica se deve ser aberto o campo de pesquisa e ajustados os itens de menu.
 * @param searchText Texto pesquisado.
 * @param onSearchChange Listener executado ao alterar o texto do campo de pesquisa.
 * @param title Título da app bar
 * @param appBarTextFieldHint Hint do campo de pesquisa.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun TitleSearchableTopAppBar(
    openSearch: Boolean,
    searchText: String,
    onSearchChange: (String) -> Unit,
    title: String,
    subtitle: String? = null,
    appBarTextFieldHint: String = stringResource(R.string.top_app_bar_text_field_hint)
) {
    if (openSearch) {
        AppBarTextField(
            value = searchText,
            onValueChange = onSearchChange,
            hint = appBarTextFieldHint
        )
    } else {
        Text(text = title, style = MaterialTheme.typography.titleSmall)

        if (subtitle != null) {
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MarketTopAppBarPreview() {
    MarketTheme {
        Surface {
            MarketTopAppBar(
                title = { Text("Título da Tela") },
                onNavigationIconClick = { },
                onLogoutClick = { }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MarketTopAppBarWithSubtitlePreview() {
    MarketTheme {
        Surface {
            SimpleMarketTopAppBar(
                title = "Título da Tela",
                subtitle = "Subtitulo da Tela",
                showMenuWithLogout = false
            )
        }
    }
}

@Preview
@Composable
fun SearchableMarketTopAppBarPreview() {
    MarketTheme {
        Surface {
            SearchableMarketTopAppBar(
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