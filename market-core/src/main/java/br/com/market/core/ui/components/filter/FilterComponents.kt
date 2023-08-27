package br.com.market.core.ui.components.filter

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import br.com.market.core.R
import br.com.market.core.filter.CommonAdvancedFilterItem
import br.com.market.core.ui.components.LazyVerticalListComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleFilter(
    modifier: Modifier = Modifier,
    onSimpleFilterChange: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    items:  List<CommonAdvancedFilterItem>,
    content: @Composable (CommonAdvancedFilterItem) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    SearchBar(
        modifier = modifier,
        query = text,
        onQueryChange = {
            text = it
            onSimpleFilterChange(text)
        },
        onSearch = { onSimpleFilterChange(text) },
        active = active,
        onActiveChange = onActiveChange,
        placeholder = {
            Text(
                text = stringResource(R.string.advanced_filter_screen_search_for),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        colors = SearchBarDefaults.colors(
            containerColor = Color.Transparent,
            inputFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
            dividerColor = DividerDefaults.color
        ),
        shape = SearchBarDefaults.fullScreenShape
    ) {
        LazyVerticalListComponent(items = items) { item ->
            content(item)
            Divider(Modifier.fillMaxWidth())
        }
    }
}