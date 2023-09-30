package br.com.market.storage.ui.component.lov

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.theme.MarketTheme
import br.com.market.domain.UserDomain
import br.com.market.market.compose.components.list.PagedVerticalListWithEmptyState
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import br.com.market.storage.R
import br.com.market.storage.ui.screens.brand.UserLovListItem
import br.com.market.storage.ui.states.user.UserLovUIState
import br.com.market.storage.ui.viewmodels.user.UserLovViewModel
import java.util.*

@Composable
fun UserLov(
    viewModel: UserLovViewModel,
    onBackClick: () -> Unit,
    onItemClick: (Pair<String, String>) -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()

    UserLov(
        state = state,
        onItemClick = onItemClick,
        onFilterChange = {
            viewModel.findUsers(it)
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLov(
    state: UserLovUIState = UserLovUIState(),
    onItemClick: (Pair<String, String>) -> Unit = { },
    onFilterChange: (String) -> Unit = { },
    onBackClick: () -> Unit = { }
) {
    val pagingData = state.users.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            Column {
                SimpleMarketTopAppBar(
                    title = stringResource(R.string.user_lov_title),
                    showMenuWithLogout = false,
                    onBackClick = onBackClick
                )

                var text by rememberSaveable { mutableStateOf("") }
                var active by rememberSaveable { mutableStateOf(false) }

                SearchBar(
                    query = text,
                    onQueryChange = {
                        text = it
                        onFilterChange(text)
                    },
                    onSearch = {
                        active = false
                    },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.user_lov_placeholder_filter),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
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
                    UserList(pagingData, onItemClick)
                }
                if (!active) {
                    Divider(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                }
            }
        }
    ) { padding ->
        ConstraintLayout(modifier = Modifier.padding(padding)) {
            UserList(pagingData, onItemClick)
        }
    }
}

@Composable
private fun UserList(
    pagingData: LazyPagingItems<UserDomain>,
    onItemClick: (Pair<String, String>) -> Unit
) {
    PagedVerticalListWithEmptyState(
        pagingItems = pagingData,
        verticalArrangementSpace = 0.dp,
        contentPadding = 0.dp
    ) { userDomain ->
        UserLovListItem(
            name = userDomain.name,
            onItemClick = {
                onItemClick(Pair(userDomain.id!!, userDomain.name))
            }
        )
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
fun UserLovPreview() {
    MarketTheme {
        Surface {
            UserLov()
        }
    }
}