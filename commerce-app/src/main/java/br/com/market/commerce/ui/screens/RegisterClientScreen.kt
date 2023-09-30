package br.com.market.commerce.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.commerce.R
import br.com.market.commerce.ui.state.RegisterClientUiState
import br.com.market.commerce.ui.viewmodel.RegisterClientViewModel
import br.com.market.commerce.util.CEPUtil
import br.com.market.commerce.util.CPFUtil
import br.com.market.core.inputs.arguments.InputArgs
import br.com.market.core.inputs.arguments.InputNumberArgs
import br.com.market.core.inputs.arguments.InputPasswordArgs
import br.com.market.core.inputs.formatter.InputNumberFormatter
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.mask.CEPMaskTransformation
import br.com.market.core.ui.mask.CPFMaskTransformation
import br.com.market.market.compose.components.FormField
import br.com.market.market.compose.components.MarketBottomAppBar
import br.com.market.market.compose.components.button.fab.FloatingActionButtonSave
import br.com.market.market.compose.components.button.icons.IconButtonVisibility
import br.com.market.market.compose.components.dialog.DialogMessage
import br.com.market.market.compose.components.loading.MarketLinearProgressIndicator
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import kotlinx.coroutines.launch

@Composable
fun RegisterClientScreen(
    viewModel: RegisterClientViewModel,
    onBackClick: () -> Unit,
    onNavigateToTextInput: (args: InputArgs, callback: (String?) -> Unit) -> Unit,
    onNavigateToNumberInput: (args: InputNumberArgs, callback: (Number?) -> Unit) -> Unit,
    onNavigateToPasswordInput: (args: InputPasswordArgs, callback: (String?) -> Unit) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    RegisterClientScreen(
        state = state,
        onBackClick = onBackClick,
        onSaveClient = viewModel::save,
        onNavigateToTextInput = onNavigateToTextInput,
        onNavigateToNumberInput = onNavigateToNumberInput,
        onNavigateToPasswordInput = onNavigateToPasswordInput,
        onSearchAddressInformationByCep = viewModel::onSearchAddressInformationByCep
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterClientScreen(
    state: RegisterClientUiState = RegisterClientUiState(),
    onBackClick: () -> Unit = { },
    onSaveClient: (callback: (PersistenceResponse) -> Unit) -> Unit = { },
    onNavigateToTextInput: (args: InputArgs, callback: (String?) -> Unit) -> Unit = { _, _ -> },
    onNavigateToNumberInput: (args: InputNumberArgs, callback: (Number?) -> Unit) -> Unit = { _, _ -> },
    onNavigateToPasswordInput: (args: InputPasswordArgs, callback: (String?) -> Unit) -> Unit = { _, _ -> },
    onSearchAddressInformationByCep: (callback: (MarketServiceResponse) -> Unit) -> Unit = { }
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(R.string.register_client_screen_label_title),
                showMenuWithLogout = false,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            MarketBottomAppBar(
                floatingActionButton = {
                    FloatingActionButtonSave(
                        onClick = {
                            if (state.onValidate()) {
                                onSaveClient { response ->
                                    if (response.success) {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(context.getString(R.string.register_client_screen_message_success_save))
                                        }
                                    } else {
                                        state.onToggleMessageDialog(response.error!!)
                                    }
                                }
                            }
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
    ) { padding ->
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val (loadingRef, containerRef) = createRefs()

            MarketLinearProgressIndicator(
                state.showLoading,
                Modifier
                    .constrainAs(loadingRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )

            ConstraintLayout(
                Modifier
                    .constrainAs(containerRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        linkTo(top = loadingRef.bottom, bottom = parent.bottom, bias = 0f)
                    }
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(scrollState)
            ) {
                val (
                    nameRef, emailRef, passwordRef,
                    cpfRef, cepRef, stateRef, cityRef,
                    publicPlaceRef, numberRef, complementRef
                ) = createRefs()

                DialogMessage(
                    title = stringResource(br.com.market.core.R.string.error_dialog_title),
                    show = state.showDialogMessage,
                    onDismissRequest = { state.onToggleMessageDialog("") },
                    message = state.serverMessage
                )

                FormField(
                    modifier = Modifier.constrainAs(nameRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(parent.top)

                        width = Dimension.fillToConstraints
                    },
                    labelResId = R.string.register_client_screen_label_name,
                    field = state.name,
                    onNavigateClick = {
                        onNavigateToTextInput(
                            InputArgs(
                                titleResId = R.string.register_client_screen_title_input_name,
                                value = state.name.value,
                                maxLength = 255,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Done
                                )
                            )
                        ) { state.name.onChange(it ?: "") }
                    }
                )

                FormField(
                    modifier = Modifier.constrainAs(cpfRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(nameRef.bottom)

                        width = Dimension.fillToConstraints
                    },
                    field = state.cpf,
                    labelResId = R.string.register_client_screen_label_cpf,
                    onNavigateClick = {
                        onNavigateToTextInput(
                            InputArgs(
                                titleResId = R.string.register_client_screen_title_input_cpf,
                                value = CPFUtil.unFormat(state.cpf.value),
                                maxLength = 11,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                visualTransformation = CPFMaskTransformation()
                            )
                        ) { state.cpf.onChange(CPFUtil.format(it)) }
                    }
                )

                FormField(
                    modifier = Modifier.constrainAs(emailRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(cpfRef.bottom)

                        width = Dimension.fillToConstraints
                    },
                    field = state.email,
                    labelResId = R.string.register_client_screen_label_email,
                    onNavigateClick = {
                        onNavigateToTextInput(
                            InputArgs(
                                titleResId = R.string.register_client_screen_title_input_email,
                                value = state.email.value,
                                maxLength = 255,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Email
                                )
                            )
                        ) { state.email.onChange(it ?: "") }
                    }
                )

                var passwordVisible by rememberSaveable { mutableStateOf(false) }

                FormField(
                    modifier = Modifier.constrainAs(passwordRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(emailRef.bottom)

                        width = Dimension.fillToConstraints
                    },
                    field = state.password,
                    labelResId = R.string.register_client_screen_label_password,
                    onNavigateClick = {
                        onNavigateToPasswordInput(
                            InputPasswordArgs(
                                titleResId = R.string.register_client_screen_title_input_password,
                                value = state.password.value,
                                maxLength = 255,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                )
                            )
                        ) { state.password.onChange(it ?: "") }
                    },
                    additionalAction = {
                        IconButtonVisibility(passwordVisible = passwordVisible) {
                            passwordVisible = !passwordVisible
                        }
                    },
                    hideValue = !passwordVisible
                )

                FormField(
                    modifier = Modifier.constrainAs(cepRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(passwordRef.bottom)

                        width = Dimension.fillToConstraints
                    },
                    field = state.cep,
                    labelResId = R.string.register_client_screen_label_cep,
                    onNavigateClick = {
                        onNavigateToTextInput(
                            InputArgs(
                                titleResId = R.string.register_client_screen_title_input_cep,
                                maxLength = 8,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                value = CEPUtil.unFormat(state.cep.value),
                                visualTransformation = CEPMaskTransformation()
                            )
                        ) {
                            state.onToggleLoading()

                            state.cep.onChange(CEPUtil.format(it))
                            onSearchAddressInformationByCep { response ->
                                if (!response.success) {
                                    state.onToggleMessageDialog(response.error!!)
                                }

                                state.onToggleLoading()
                            }
                        }
                    }
                )

                FormField(
                    modifier = Modifier.constrainAs(stateRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(cepRef.bottom)

                        width = Dimension.fillToConstraints
                    },
                    field = state.state,
                    labelResId = R.string.register_client_screen_label_state,
                    onNavigateClick = {
                        onNavigateToTextInput(
                            InputArgs(
                                titleResId = R.string.register_client_screen_title_input_state,
                                value = state.state.value,
                                maxLength = 255,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Done
                                )
                            )
                        ) { state.state.onChange(it ?: "") }
                    }
                )

                FormField(
                    modifier = Modifier.constrainAs(cityRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(stateRef.bottom)

                        width = Dimension.fillToConstraints
                    },
                    field = state.city,
                    labelResId = R.string.register_client_screen_label_city,
                    onNavigateClick = {
                        onNavigateToTextInput(
                            InputArgs(
                                titleResId = R.string.register_client_screen_title_input_city,
                                value = state.city.value,
                                maxLength = 255,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Done
                                )
                            )
                        ) { state.city.onChange(it ?: "") }
                    }
                )

                FormField(
                    modifier = Modifier.constrainAs(publicPlaceRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(cityRef.bottom)

                        width = Dimension.fillToConstraints
                    },
                    field = state.publicPlace,
                    labelResId = R.string.register_client_screen_label_public_place,
                    onNavigateClick = {
                        onNavigateToTextInput(
                            InputArgs(
                                titleResId = R.string.register_client_screen_title_input_public_place,
                                value = state.publicPlace.value,
                                maxLength = 255,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Done
                                )
                            )
                        ) { state.publicPlace.onChange(it ?: "") }
                    }
                )

                FormField(
                    modifier = Modifier.constrainAs(numberRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(publicPlaceRef.bottom)

                        width = Dimension.fillToConstraints
                    },
                    field = state.number,
                    labelResId = R.string.register_client_screen_label_number,
                    onNavigateClick = {
                        onNavigateToNumberInput(
                            InputNumberArgs(
                                integer = true,
                                titleResId = R.string.register_client_screen_title_input_number,
                                maxLength = 255,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                )
                            )
                        ) {
                            val formatter = InputNumberFormatter(integer = true)
                            state.number.onChange(formatter.formatToString(it) ?: "")
                        }
                    }
                )

                FormField(
                    modifier = Modifier.constrainAs(complementRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0f)
                        top.linkTo(numberRef.bottom)

                        width = Dimension.fillToConstraints
                    },
                    field = state.complement,
                    labelResId = R.string.register_client_screen_label_complement,
                    onNavigateClick = {
                        onNavigateToTextInput(
                            InputArgs(
                                titleResId = R.string.register_client_screen_title_input_complement,
                                value = state.complement.value,
                                maxLength = 255,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Done
                                )
                            )
                        ) { state.complement.onChange(it ?: "") }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun RegisterClientPreview() {
    MarketTheme {
        Surface {
            RegisterClientPreview()
        }
    }
}
