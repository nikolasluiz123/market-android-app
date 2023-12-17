package br.com.market.storage.ui.screens.brand

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.callbacks.IServiceOperationCallback
import br.com.market.core.callbacks.ITextInputNavigationCallback
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.inputs.arguments.InputArgs
import br.com.market.core.theme.MarketTheme
import br.com.market.domain.BrandDomain
import br.com.market.market.compose.components.FormField
import br.com.market.market.compose.components.MarketBottomAppBar
import br.com.market.market.compose.components.MarketSnackBar
import br.com.market.market.compose.components.button.fab.FloatingActionButtonSave
import br.com.market.market.compose.components.button.icons.IconButtonInactivate
import br.com.market.market.compose.components.button.icons.IconButtonReactivate
import br.com.market.market.compose.components.button.icons.IconButtonSearch
import br.com.market.storage.R
import br.com.market.storage.ui.screens.category.CategoryScreenTabCategory
import br.com.market.storage.ui.states.brand.BrandUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BrandScreenTaBrand(
    state: BrandUIState = BrandUIState(),
    onUpdateEditMode: (Boolean) -> Unit = { },
    toggleActive: IServiceOperationCallback? = null,
    saveBrandCallback: IServiceOperationCallback? = null,
    isEdit: Boolean = false,
    textInputCallback: ITextInputNavigationCallback? = null,
    onNavToBrandLov: (categoryId: String) -> Unit = { }
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
                                state.onShowDialog?.onShow(
                                    type = EnumDialogType.CONFIRMATION,
                                    message = context.getString(R.string.brand_screen_tab_brand_inactivate_question),
                                    onConfirm = {
                                        state.onToggleLoading()
                                        toggleActive?.onExecute(
                                            onSuccess = {
                                                isActive = false

                                                scope.launch {
                                                    snackbarHostState.showSnackbar(context.getString(R.string.brand_screen_tab_brand_inactivate_success_message))
                                                }
                                                state.onToggleLoading()
                                            },
                                            onError = {
                                                state.onHideDialog()
                                                state.onToggleLoading()
                                                state.onShowDialog.onShow(type = EnumDialogType.ERROR, message = it, onConfirm = {}, onCancel = {})
                                            }
                                        )
                                    },
                                    onCancel = { }
                                )
                            }
                        )
                    } else {
                        IconButtonReactivate(
                            enabled = isEditMode,
                            onClick = {
                                state.onShowDialog?.onShow(
                                    type = EnumDialogType.CONFIRMATION,
                                    message = context.getString(R.string.brand_screen_tab_brand_reactivate_question),
                                    onConfirm = {
                                        state.onToggleLoading()
                                        toggleActive?.onExecute(
                                            onSuccess = {
                                                isActive = true

                                                scope.launch {
                                                    snackbarHostState.showSnackbar(context.getString(R.string.brand_screen_tab_brand_reactivate_success_message))
                                                }
                                                state.onToggleLoading()
                                            },
                                            onError = {
                                                state.onHideDialog()
                                                state.onToggleLoading()
                                                state.onShowDialog.onShow(type = EnumDialogType.ERROR, message = it, onConfirm = {}, onCancel = {})
                                            }
                                        )
                                    },
                                    onCancel = { }
                                )
                            }
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButtonSave(
                        onClick = {
                            isEditMode = saveBrand(state, isActive, isEditMode, saveBrandCallback, scope, snackbarHostState, context)
                            onUpdateEditMode(isEditMode)
                        }
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                MarketSnackBar(it)
            }
        }
    ) {
        ConstraintLayout(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val inputNameRef = createRef()

            FormField(
                modifier = Modifier.constrainAs(inputNameRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0f)
                    top.linkTo(parent.top)

                    width = Dimension.fillToConstraints
                },
                labelResId = R.string.brand_screen_tab_brand_label_name,
                field = state.name,
                onNavigateClick = {
                    textInputCallback?.onNavigate(
                        args = InputArgs(
                            titleResId = R.string.brand_screen_tab_brand_title_input_name,
                            value = state.name.value,
                            maxLength = 255,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            )
                        ),
                        callback = { value -> state.name.onChange(value ?: "") }
                    )
                },
                additionalAction = {
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
    saveBrandCallback: IServiceOperationCallback?,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context
): Boolean {
    if (state.onValidate() && isActive) {
        state.onToggleLoading()

        state.brandDomain = if (isEditMode) {
            state.brandDomain?.copy(name = state.name.value)
        } else {
            BrandDomain(name = state.name.value)
        }

        saveBrandCallback?.onExecute(
            onSuccess = {
                state.onToggleLoading()
                scope.launch {
                    snackbarHostState.showSnackbar(context.getString(R.string.brand_screen_tab_brand_save_success_message))
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

    return state.brandDomain != null
}