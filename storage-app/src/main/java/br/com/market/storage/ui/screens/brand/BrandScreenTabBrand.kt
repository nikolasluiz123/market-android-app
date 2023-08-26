package br.com.market.storage.ui.screens.brand

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import br.com.market.core.ui.components.buttons.IconButtonSearch
import br.com.market.domain.BrandDomain
import br.com.market.storage.R
import br.com.market.storage.ui.screens.category.CategoryScreenTabCategory
import br.com.market.storage.ui.states.brand.BrandUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BrandScreenTaBrand(
    state: BrandUIState = BrandUIState(),
    onUpdateEditMode: (Boolean) -> Unit = { },
    onToggleActive: () -> Unit = { },
    onSaveBrandClick: (Boolean) -> Unit = { },
    isEdit: Boolean = false,
    onNavToBrandLov: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isEditMode by remember(isEdit) {
        mutableStateOf(isEdit)
    }

    var isActive by remember(state.active) {
        mutableStateOf(state.active)
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
                                    snackbarHostState.showSnackbar("Marca Inativada com Sucesso")
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
                                    snackbarHostState.showSnackbar("Marca Reativada com Sucesso")
                                }
                            }
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButtonSave(
                        onClick = {
                            isEditMode = saveBrand(state, isActive, isEditMode, onSaveBrandClick, scope, snackbarHostState)
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
                value = state.brandName,
                onValueChange = state.onBrandNameChange,
                error = state.brandNameErrorMessage,
                label = { Text(text = stringResource(R.string.brand_screen_tab_brand_label_name)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words
                ),
                enabled = isActive,
                keyboardActions = KeyboardActions(
                    onDone = {
                        isEditMode = saveBrand(state, isActive, isEditMode, onSaveBrandClick, scope, snackbarHostState)
                        onUpdateEditMode(isEditMode)
                        defaultKeyboardAction(ImeAction.Done)
                    }
                ),
                trailingIcon = {
                    IconButtonSearch {
                        onNavToBrandLov(state.categoryDomain?.id!!)
                    }
                }
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

private fun saveBrand(
    state: BrandUIState,
    isActive: Boolean,
    isEditMode: Boolean,
    onSaveBrandClick: (Boolean) -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
): Boolean {
    if (state.onValidate() && isActive) {

        state.brandDomain = if (isEditMode) {
            state.brandDomain?.copy(name = state.brandName)
        } else {
            BrandDomain(name = state.brandName)
        }

        onSaveBrandClick(isEditMode)

        scope.launch {
            snackbarHostState.showSnackbar("Marca Salva com Sucesso")
        }
    }

    return state.brandDomain != null
}