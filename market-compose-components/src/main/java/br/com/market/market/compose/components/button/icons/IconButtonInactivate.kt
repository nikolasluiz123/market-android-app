package br.com.market.market.compose.components.button.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.R
import br.com.market.core.theme.GREY_500
import br.com.market.core.theme.GREY_800
import br.com.market.core.theme.MarketTheme

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



@Preview
@Composable
fun IconButtonInactivatePreview() {
    MarketTheme {
        Surface {
            IconButtonInactivate()
        }
    }
}