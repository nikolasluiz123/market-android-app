package br.com.market.storage.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.storage.sampledata.sampleProducts
import br.com.market.storage.ui.theme.StorageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = { onSearchChange(it) },
        modifier = modifier,
        shape = RoundedCornerShape(100),
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Ícone Pesquisa") },
        label = { Text(text = "Produto") },
        placeholder = { Text(text = "O que você procura?") }
    )
}

@Preview
@Composable
fun SearchTextFieldLightPreview() {
    StorageTheme {
        Surface {
            SearchTextField(searchText = "Arroz")
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchTextFieldDarkPreview() {
    StorageTheme {
        Surface {
            SearchTextField(searchText = "Arroz")
        }
    }
}