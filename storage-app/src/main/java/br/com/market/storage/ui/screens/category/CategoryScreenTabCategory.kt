package br.com.market.storage.ui.screens.category

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.inputs.arguments.InputArgs
import br.com.market.core.interfaces.ISaveCallback
import br.com.market.core.interfaces.ITextInputNavigationCallback
import br.com.market.core.theme.GREY_900
import br.com.market.core.theme.MarketTheme
import br.com.market.market.compose.components.FormField
import br.com.market.market.compose.components.MarketBottomAppBar
import br.com.market.market.compose.components.button.fab.FloatingActionButtonSave
import br.com.market.market.compose.components.button.icons.IconButtonInactivate
import br.com.market.market.compose.components.button.icons.IconButtonReactivate
import br.com.market.storage.R
import br.com.market.storage.ui.states.category.CategoryUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoryScreenTabCategory(
    state: CategoryUIState = CategoryUIState(),
    onUpdateEditMode: (Boolean) -> Unit = { },
    onToggleActive: () -> Unit = { },
    onSaveCategoryClick: ISaveCallback? = null,
    isEdit: Boolean = false,
    textInputCallback: ITextInputNavigationCallback? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isEditMode by remember(isEdit) {
        mutableStateOf(isEdit)
    }

    var isActive by remember(state.categoryDomain?.active) {
        mutableStateOf(state.categoryDomain?.active ?: true)
    }

    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    if (isActive) {
                        IconButtonInactivate(
                            enabled = isEditMode,
                            onClick = {
                                state.onShowDialog?.onShow(
                                    type = EnumDialogType.CONFIRMATION,
                                    message = context.getString(R.string.category_screen_tab_category_inactivate_question),
                                    onConfirm = {
                                        onToggleActive()
                                        isActive = false

                                        scope.launch {
                                            snackbarHostState.showSnackbar(context.getString(R.string.category_screen_tab_category_inactivated_success))
                                        }
                                    },
                                    onCancel = { }
                                )
                            }
                        )
                    } else {
                        IconButtonReactivate(
                            enabled = isEditMode,
                            onClick = {
                                onToggleActive()
                                isActive = true

                                scope.launch {
                                    snackbarHostState.showSnackbar(context.getString(R.string.category_screen_tab_category_reactivated_success))
                                }
                            }
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButtonSave(
                        onClick = {
                            isEditMode = saveCategory(state, isActive, onSaveCategoryClick, scope, snackbarHostState, context)
                            onUpdateEditMode(isEditMode)
                        }
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(modifier = Modifier.padding(8.dp), containerColor = GREY_900, contentColor = Color.White) {
                    Text(text = it.visuals.message)
                }
            }
        }
    ) {
        ConstraintLayout(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val (inputNameRef) = createRefs()

            FormField(
                modifier = Modifier.constrainAs(inputNameRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0f)
                    top.linkTo(parent.top)

                    width = Dimension.fillToConstraints
                },
                labelResId = R.string.category_screen_tab_category_label_name,
                field = state.nameField,
                onNavigateClick = {
                    textInputCallback?.onNavigate(
                        args = InputArgs(
                            titleResId = R.string.category_screen_tab_category_title_input_name,
                            value = state.nameField.value,
                            maxLength = 255,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            )
                        ),
                        callback = { value -> state.nameField.onChange(value ?: "") }
                    )
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

private fun saveCategory(
    state: CategoryUIState,
    isActive: Boolean,
    onSaveCategoryClick: ISaveCallback?,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context
): Boolean {
    if (state.onValidate() && isActive) {
        state.onToggleLoading()

        onSaveCategoryClick?.onSave(
            onSuccess = {
                state.onToggleLoading()
                scope.launch {
                    snackbarHostState.showSnackbar(context.getString(R.string.category_screen_tab_category_success_save_message))
                }
            },
            onError = { message ->
                state.onToggleLoading()
                state.onShowDialog?.onShow(
                    type = EnumDialogType.ERROR,
                    message = message,
                    onConfirm = {},
                    onCancel = {}
                )
            }
        )
    }

    return state.categoryDomain != null
}