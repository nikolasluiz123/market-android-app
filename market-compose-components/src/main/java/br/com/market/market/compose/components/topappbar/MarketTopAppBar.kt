package br.com.market.market.compose.components.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.button.icons.IconButtonArrowBack
import br.com.market.market.compose.components.button.icons.MenuIconButton
import br.com.market.market.compose.components.button.icons.MenuIconButtonWithDefaultActions

/**
 * TopAppBar padrão do APP.
 *
 * @param title Título da app bar
 * @param onBackClick Ação ao clicar no ícone da esquerda.
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
    onBackClick: () -> Unit,
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
    showMenu: Boolean = false
) {
    TopAppBar(
        title = title,
        colors = colors,
        navigationIcon = {
            if (showNavigationIcon) {
                IconButtonArrowBack(onClick = onBackClick)
            }
        },
        actions = {
            actions()

            if (showMenuWithLogout) {
                MenuIconButtonWithDefaultActions(
                    onLogoutClick = onLogoutClick,
                    menuItems = menuItems,
                )
            } else if (showMenu) {
                MenuIconButton(menuItems)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MarketTopAppBarPreview() {
    MarketTheme {
        Surface {
            MarketTopAppBar(
                title = { Text("Título da Tela") },
                onBackClick = { },
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