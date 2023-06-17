package br.com.market.storage.ui.screens.movement

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MultiActionsFabBottomAppBar
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.MarketFloatingActionButtonMultiActions
import br.com.market.core.ui.components.buttons.fab.SubActionFabItem
import br.com.market.core.ui.components.buttons.rememberFabMultiActionsState
import br.com.market.enums.EnumOperationType
import br.com.market.storage.ui.states.MovementsSearchUIState
import br.com.market.storage.ui.viewmodels.movements.MovementsSearchViewModel

@Composable
fun MovementsSearchScreen(
    viewModel: MovementsSearchViewModel,
    onAddMovementClick: (String, String, EnumOperationType, String?) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    MovementsSearchScreen(
        state = state,
        onAddMovementClick = onAddMovementClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsSearchScreen(
    state: MovementsSearchUIState = MovementsSearchUIState(),
    onAddMovementClick: (String, String, EnumOperationType, String?) -> Unit = { _, _, _, _ -> },
    onBackClick: () -> Unit = { }
) {
    val bottomBarState = rememberFabMultiActionsState()

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Movimentações",
                subtitle = state.productName ?: state.brandName,
                showMenuWithLogout = false,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            MultiActionsFabBottomAppBar(
                floatingActionButton = {
                    MarketFloatingActionButtonMultiActions(state = bottomBarState) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.label_adicionar),
                            tint = Color.White
                        )
                    }
                },
                subActionsFab = listOf(
                    SubActionFabItem(
                        icon = painterResource(id = R.drawable.ic_calendar_24dp),
                        label = "Agendar Entrada",
                        onFabItemClicked = {
                            onAddMovementClick(
                                state.categoryId!!,
                                state.brandId!!,
                                EnumOperationType.ScheduledInput,
                                state.productId
                            )
                        }
                    ),
                    SubActionFabItem(
                        icon = painterResource(id = R.drawable.ic_input_storage_24dp),
                        label = "Entrada",
                        onFabItemClicked = {
                            onAddMovementClick(
                                state.categoryId!!,
                                state.brandId!!,
                                EnumOperationType.Input,
                                state.productId
                            )
                        }
                    ),
                    SubActionFabItem(
                        icon = painterResource(id = R.drawable.ic_warning_24dp),
                        label = "Baixa",
                        onFabItemClicked = {
                            onAddMovementClick(
                                state.categoryId!!,
                                state.brandId!!,
                                EnumOperationType.Output,
                                state.productId
                            )
                        }
                    )
                ),
                state = bottomBarState
            )
        }
    ) { padding ->
        ConstraintLayout(Modifier.padding(padding)) {

        }
    }
}

@Preview
@Composable
fun MovementsScreenSearchPreview() {
    MarketTheme {
        Surface {
            MovementsSearchScreen()
        }
    }
}