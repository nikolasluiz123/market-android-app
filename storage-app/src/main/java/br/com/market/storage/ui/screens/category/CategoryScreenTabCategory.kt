package br.com.market.storage.ui.screens.category

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.OutlinedTextFieldValidation
import br.com.market.core.ui.components.buttons.fab.FloatingActionButtonSave
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.core.ui.components.buttons.IconButtonReactivate
import br.com.market.domain.CategoryDomain
import br.com.market.storage.R
import br.com.market.storage.ui.states.category.CategoryUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoryScreenTabCategory(
    state: CategoryUIState = CategoryUIState(),
    onUpdateEditMode: (Boolean) -> Unit = { },
    onToggleActive: () -> Unit = { },
    onSaveCategoryClick: (Boolean) -> Unit = { },
    isEdit: Boolean = false
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isEditMode by remember(isEdit) {
        mutableStateOf(isEdit)
    }

    var isActive by remember(state.categoryDomain?.active) {
        mutableStateOf(state.categoryDomain?.active ?: true)
    }

    Scaffold(
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    if (isActive) {
                        IconButtonInactivate(
                            enabled = isEditMode,
                            onClick = {
                                onToggleActive()
                                isActive = false

                                scope.launch {
                                    snackbarHostState.showSnackbar("Categoria Inativada com Sucesso")
                                }
                            }
                        )
                    } else {
                        IconButtonReactivate(
                            enabled = isEditMode,
                            onClick = {
                                onToggleActive()
                                isActive = true

                                scope.launch {
                                    snackbarHostState.showSnackbar("Categoria Reativada com Sucesso")
                                }
                            }
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButtonSave(
                        onClick = {
                            isEditMode = saveCategory(state, isActive, isEditMode, onSaveCategoryClick, scope, snackbarHostState)
                            onUpdateEditMode(isEditMode)
                        }
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(modifier = Modifier.padding(8.dp)) {
                    Text(text = it.visuals.message)
                }
            }
        }
    ) {
        ConstraintLayout(
            Modifier
                .padding(it)
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
                        isEditMode = saveCategory(state, isActive, isEditMode, onSaveCategoryClick, scope, snackbarHostState)
                        onUpdateEditMode(isEditMode)
                        defaultKeyboardAction(ImeAction.Done)
                    }
                )
            )
        }
    }
}

@Preview
@Composable
fun TabCategoryScreenPreview() {
    MarketTheme {
        Surface {
            CategoryScreenTabCategory()
        }
    }
}

private fun saveCategory(
    state: CategoryUIState,
    isActive: Boolean,
    isEditMode: Boolean,
    onSaveCategoryClick: (Boolean) -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
): Boolean {
    if (state.onValidate() && isActive) {

        state.categoryDomain = if (isEditMode) {
            state.categoryDomain?.copy(name = state.categoryName)
        } else {
            CategoryDomain(name = state.categoryName)
        }

        onSaveCategoryClick(isEditMode)

        scope.launch {
            snackbarHostState.showSnackbar("Categoria Salva com Sucesso")
        }
    }

    return state.categoryDomain != null
}