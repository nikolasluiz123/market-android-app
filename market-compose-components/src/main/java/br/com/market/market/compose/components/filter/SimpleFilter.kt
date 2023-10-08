package br.com.market.market.compose.components.filter

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleFilter(
    modifier: Modifier = Modifier,
    onSimpleFilterChange: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    placeholderResId: Int,
    trailingIcon: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    SearchBar(
        modifier = modifier.padding(0.dp),
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
                text = stringResource(placeholderResId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = Color.Transparent,
            inputFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
                focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
            ),
            dividerColor = MaterialTheme.colorScheme.outlineVariant
        ),
        shape = SearchBarDefaults.fullScreenShape,
        trailingIcon = trailingIcon
    ) {
        content()
    }
}