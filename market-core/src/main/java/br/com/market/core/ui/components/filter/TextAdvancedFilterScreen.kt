package br.com.market.core.ui.components.filter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme
import br.com.market.core.theme.colorSecondary
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.IconButtonClear
import br.com.market.core.ui.states.filter.TextAdvancedFilterUIState
import br.com.market.core.ui.viewmodel.filter.TextAdvancedFilterViewModel

@Composable
fun TextAdvancedFilterScreen(
    viewModel: TextAdvancedFilterViewModel,
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    TextAdvancedFilterScreen(
        state = state,
        onBackClick = onBackClick,
        onConfirmClick = onConfirmClick,
        onCancelClick = onCancelClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextAdvancedFilterScreen(
    state: TextAdvancedFilterUIState = TextAdvancedFilterUIState(),
    onBackClick: () -> Unit = { },
    onCancelClick: () -> Unit = { },
    onConfirmClick: (String) -> Unit = { }
) {
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(id = state.titleResId!!),
                showMenuWithLogout = false,
                actions = {
                    IconButtonClear()
                },
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        ConstraintLayout(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val (textField, buttonConfirmRef, buttonCancelRef) = createRefs()

            TextField(
                modifier = Modifier
                    .constrainAs(textField) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        linkTo(top = parent.top, bottom = buttonConfirmRef.top, bias = 0F, bottomMargin = 8.dp)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                value = state.value,
                onValueChange = state.onValueChange,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            OutlinedButton(
                modifier = Modifier.constrainAs(buttonConfirmRef) {
                    end.linkTo(parent.end, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                },
                onClick = { onConfirmClick(state.value) },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = colorSecondary),
                border = null
            ) {
                Text(stringResource(R.string.text_advanced_filter_screen_label_confirm), color = Color.White)
            }

            OutlinedButton(
                modifier = Modifier.constrainAs(buttonCancelRef) {
                    end.linkTo(buttonConfirmRef.start, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                },
                onClick = onCancelClick,
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, colorSecondary)
            ) {
                Text(stringResource(R.string.text_advanced_filter_screen_label_cancel), color = colorSecondary)
            }
        }
    }
}

@Preview
@Composable
fun TextAdvancedFilterScreenPreview() {
    MarketTheme {
        Surface {
            TextAdvancedFilterScreen()
        }
    }
}