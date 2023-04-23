package br.com.market.storage.ui.screens.category

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.OutlinedTextFieldValidation
import br.com.market.storage.R
import br.com.market.storage.ui.states.category.CategoryUIState

@Composable
fun TabCategoryScreen(
    state: CategoryUIState = CategoryUIState(),
    isActive: Boolean,
    onSendClick: () -> Unit = { }
) {
    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val inputNameRef = createRef()

        OutlinedTextFieldValidation(
            modifier = Modifier.constrainAs(inputNameRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)

                width = Dimension.fillToConstraints
            },
            value = state.categoryName,
            onValueChange = state.onCategoryNameChange,
            error = state.categoryNameErrorMessage,
            label = { Text(text = stringResource(R.string.category_screen_tab_category_label_name)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Words
            ),
            enabled = isActive,
            keyboardActions = KeyboardActions(
                onDone = {
                    onSendClick()
                    defaultKeyboardAction(ImeAction.Done)
                }
            )
        )
    }
}

@Preview
@Composable
fun TabCategoryScreenPreview() {
    MarketTheme {
        Surface {
            TabCategoryScreen(isActive = true)
        }
    }
}