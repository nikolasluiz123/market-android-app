package br.com.market.market.compose.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.core.R
import br.com.market.core.theme.BLUE_500
import br.com.market.core.theme.MarketTheme

@Composable
fun InfoChip(
    label: String,
    icon: @Composable () -> Unit,
    backgroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    SuggestionChip(
        onClick = {},
        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
        icon = icon,
        shape = RoundedCornerShape(percent = 100),
        border = SuggestionChipDefaults.suggestionChipBorder(borderColor = borderColor),
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = backgroundColor, labelColor = labelColor, iconContentColor = iconColor
        ),
        modifier = modifier
    )
}

@Preview
@Composable
fun ChipPreview() {
    MarketTheme {
        Surface {
            InfoChip(
                label = "Entrada",
                icon = { Icon(painter = painterResource(id = R.drawable.ic_add_16dp), contentDescription = null) },
                backgroundColor = BLUE_500,
                borderColor = BLUE_500,
                labelColor = Color.White,
                iconColor = Color.White
            )
        }
    }
}