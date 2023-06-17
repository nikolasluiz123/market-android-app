package br.com.market.storage.ui.screens.movement

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.OutlinedTextFieldValidation
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.FloatingActionButtonSave
import br.com.market.core.ui.components.buttons.IconButtonCalendar
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.core.ui.components.buttons.IconButtonReactivate
import br.com.market.core.ui.components.buttons.IconButtonSearch
import br.com.market.core.ui.components.buttons.IconButtonTime
import br.com.market.core.ui.components.dialog.TimePickerDialog
import br.com.market.domain.StorageOperationsHistoryDomain
import br.com.market.enums.EnumOperationType.*
import br.com.market.storage.R
import br.com.market.storage.ui.states.MovementUIState
import br.com.market.storage.ui.viewmodels.movements.MovementViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun MovementScreen(
    viewModel: MovementViewModel,
    onBackClick: () -> Unit,
    onNavToProductLov: (String, String, (String) -> Unit) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    MovementScreen(
        state = state,
        onBackClick = onBackClick,
        onNavToProductLov = { categoryId, brandId ->
            onNavToProductLov(categoryId, brandId) { productId ->
                viewModel.loadProductById(productId)
            }
        },
        onSaveMovementClick = viewModel::saveMovement
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementScreen(
    state: MovementUIState = MovementUIState(),
    onBackClick: () -> Unit = { },
    onNavToProductLov: (String, String) -> Unit = { _, _ -> },
    onToggleActive: () -> Unit = { },
    onSaveMovementClick: () -> Unit = { }
) {
    var isEditMode by remember(state.storageOperationsHistoryDomain) {
        mutableStateOf(state.storageOperationsHistoryDomain != null)
    }

    var isActive by remember(state.storageOperationsHistoryDomain?.active) {
        mutableStateOf(state.storageOperationsHistoryDomain?.active ?: true)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            val title = when (state.operationType!!) {
                Input -> "Nova Entrada"
                ScheduledInput -> "Agendar Entrada"
                Output -> "Nova Baixa"
                else -> ""
            }

            SimpleMarketTopAppBar(
                title = title,
                subtitle = state.productDomain?.name ?: state.brandDomain?.name ?: "",
                showMenuWithLogout = false,
                onBackClick = onBackClick
            )
        },
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
                                    snackbarHostState.showSnackbar("Movimentação Inativada com Sucesso")
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
                                    snackbarHostState.showSnackbar("Movimentação Reativada com Sucesso")
                                }
                            }
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButtonSave(
                        onClick = {
                            isEditMode = saveMovement(state, isActive, isEditMode, onSaveMovementClick, scope, snackbarHostState, context)
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
        val scrollState = rememberScrollState()

        var datePickerShow by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()

        var timePickerShow by remember { mutableStateOf(false) }
        val timePickerState = rememberTimePickerState()

        ConstraintLayout(
            Modifier
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            val (inputProductRef, inputQuantityRef, inputDateRef, inputTimeRef, inputDescriptionRef) = createRefs()
            createHorizontalChain(inputDateRef, inputTimeRef)

            if (state.productId == null) {
                OutlinedTextFieldValidation(
                    modifier = Modifier
                        .constrainAs(inputProductRef) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)

                            width = Dimension.fillToConstraints
                        }
                        .padding(horizontal = 8.dp),
                    value = state.productName,
                    onValueChange = { },
                    error = state.productNameErrorMessage,
                    label = { Text(text = stringResource(R.string.movement_screen_label_product)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    enabled = true,
                    readOnly = true,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }
                    ),
                    trailingIcon = {
                        IconButtonSearch {
                            onNavToProductLov(state.categoryId!!, state.brandDomain?.id!!)
                        }
                    }
                )
            }

            OutlinedTextFieldValidation(
                value = state.quantity,
                onValueChange = state.onQuantityChange,
                error = state.quantityErrorMessage,
                label = { Text(text = stringResource(R.string.product_screen_label_quantity)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                enabled = true,
                modifier = Modifier
                    .constrainAs(inputQuantityRef) {
                        top.linkTo(inputProductRef.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 8.dp)
            )

            if (ScheduledInput == state.operationType) {
                OutlinedTextFieldValidation(
                    modifier = Modifier
                        .constrainAs(inputDateRef) {
                            start.linkTo(parent.start)
                            top.linkTo(inputQuantityRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(horizontal = 8.dp),
                    value = state.datePrevision,
                    onValueChange = { },
                    error = state.datePrevisionErrorMessage,
                    label = { Text(text = stringResource(R.string.movement_screen_label_date)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    enabled = true,
                    readOnly = true,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }
                    ),
                    trailingIcon = {
                        IconButtonCalendar { datePickerShow = true }
                    }
                )

                OutlinedTextFieldValidation(
                    modifier = Modifier
                        .constrainAs(inputTimeRef) {
                            end.linkTo(parent.end)
                            top.linkTo(inputQuantityRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(horizontal = 8.dp),
                    value = state.timePrevision,
                    onValueChange = { },
                    error = state.timePrevisionErrorMessage,
                    label = { Text(text = stringResource(R.string.movement_screen_label_time)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    enabled = true,
                    readOnly = true,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }
                    ),
                    trailingIcon = {
                        IconButtonTime { timePickerShow = true }
                    }
                )

                if (datePickerShow) {
                    DatePickerDialog(
                        onDismissRequest = {
                            datePickerShow = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    datePickerState.selectedDateMillis?.let {
                                        val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                                        state.datePrevision = localDate.format(DateTimeFormatter.ofPattern(EnumDateTimePatterns.DATE_PATTER.pattern))
                                    }

                                    datePickerShow = false
                                },
                            ) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { datePickerShow = false }) {
                                Text("Cancelar")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                if (timePickerShow) {
                    TimePickerDialog(
                        title = "Selecionar Hora",
                        onConfirm = {
                            val localTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            state.timePrevision = localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                            timePickerShow = false
                        },
                        onCancel = {
                            timePickerShow = false
                        }
                    ) {
                        TimePicker(state = timePickerState)
                    }
                }
            }

            if (Output == state.operationType) {
                OutlinedTextFieldValidation(
                    value = state.description ?: "",
                    onValueChange = state.onDescriptionChange,
                    error = state.descriptionErrorMessage,
                    label = { Text(text = stringResource(R.string.product_screen_label_description)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    enabled = true,
                    modifier = Modifier
                        .constrainAs(inputDescriptionRef) {
                            top.linkTo(inputQuantityRef.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                            width = Dimension.fillToConstraints
                        }
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

private fun saveMovement(
    state: MovementUIState,
    isActive: Boolean,
    isEditMode: Boolean,
    onSaveMovementClick: () -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context
): Boolean {
    scope.launch {
        if (state.onValidate() && isActive) {
            val datePrevision = state.datePrevision?.let {
                LocalDate.parse(it, DateTimeFormatter.ofPattern(EnumDateTimePatterns.DATE_PATTER.pattern))
            }

            val timePrevision = state.timePrevision?.let {
                LocalTime.parse(it, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            }

            val localDateTimePrevision = datePrevision?.atTime(timePrevision)

            val dateRealization = when (state.operationType) {
                Output, Input -> LocalDateTime.now()
                else -> null
            }

            val userId = context.dataStore.data.first().toPreferences()[PreferencesKey.USER]!!

            state.storageOperationsHistoryDomain = if (isEditMode) {
                state.storageOperationsHistoryDomain?.copy(
                    productId = state.productDomain?.id,
                    quantity = state.quantity.toInt(),
                    datePrevision = localDateTimePrevision,
                    dateRealization = dateRealization,
                    operationType = state.operationType,
                    description = state.description,
                    userId = userId
                )
            } else {
                StorageOperationsHistoryDomain(
                    productId = state.productDomain?.id,
                    quantity = state.quantity.toInt(),
                    datePrevision = localDateTimePrevision,
                    dateRealization = dateRealization,
                    operationType = state.operationType,
                    description = state.description,
                    userId = userId
                )
            }

            onSaveMovementClick()

            snackbarHostState.showSnackbar("Movimentação Salva com Sucesso")
        }
    }
    return state.storageOperationsHistoryDomain != null
}

@Preview
@Composable
fun MovementScreenPreview() {
    MarketTheme {
        Surface {
            MovementScreen()
        }
    }
}