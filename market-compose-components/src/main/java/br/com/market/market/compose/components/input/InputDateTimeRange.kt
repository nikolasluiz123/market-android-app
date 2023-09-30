package br.com.market.market.compose.components.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.R
import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.extensions.parseToLocalDate
import br.com.market.core.extensions.parseToLocalTime
import br.com.market.core.theme.MarketTheme
import br.com.market.core.theme.colorSecondary
import br.com.market.core.ui.states.filter.DateRangeAdvancedFilterUIState
import br.com.market.market.compose.components.input.viewmodel.InputDateTimeRangeViewModel
import br.com.market.market.compose.components.OutlinedTextFieldValidation
import br.com.market.market.compose.components.button.icons.IconButtonCalendar
import br.com.market.market.compose.components.button.icons.IconButtonClear
import br.com.market.market.compose.components.button.icons.IconButtonTime
import br.com.market.market.compose.components.dialog.TimePickerDialog
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun InputDateTimeRange(
    viewModel: InputDateTimeRangeViewModel,
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (Pair<LocalDateTime?, LocalDateTime?>?) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    InputDateTimeRange(
        state = state,
        onBackClick = onBackClick,
        onConfirmClick = onConfirmClick,
        onCancelClick = onCancelClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDateTimeRange(
    state: DateRangeAdvancedFilterUIState = DateRangeAdvancedFilterUIState(),
    onBackClick: () -> Unit = { },
    onCancelClick: () -> Unit = { },
    onConfirmClick: (Pair<LocalDateTime?, LocalDateTime?>?) -> Unit = {  }
) {
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(id = state.titleResId!!),
                showMenuWithLogout = false,
                actions = {
                    IconButtonClear {
                        state.onDateFromChange(null)
                        state.onTimeFromChange(null)
                        state.onDateToChange(null)
                        state.onTimeToChange(null)
                    }
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
            val (
                inputDateFromRef, inputTimeFromRef, inputDateToRef, inputTimeToRef,
                buttonConfirmRef, buttonCancelRef
            ) = createRefs()
            createHorizontalChain(inputDateFromRef, inputTimeFromRef)
            createHorizontalChain(inputDateToRef, inputTimeToRef)

            var datePickerFromShow by remember { mutableStateOf(false) }
            val datePickerFromState = rememberDatePickerState()

            var timePickerFromShow by remember { mutableStateOf(false) }
            val timePickerFromState = rememberTimePickerState()

            var datePickerToShow by remember { mutableStateOf(false) }
            val datePickerToState = rememberDatePickerState()

            var timePickerToShow by remember { mutableStateOf(false) }
            val timePickerToState = rememberTimePickerState()

            OutlinedTextFieldValidation(
                modifier = Modifier
                    .constrainAs(inputDateFromRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, margin = 8.dp)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.5F
                    }
                    .padding(horizontal = 8.dp),
                value = state.dateFrom,
                onValueChange = { },
                label = { Text(text = stringResource(R.string.date_range_advanced_filter_screen_label_date_from)) },
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
                    IconButtonCalendar { datePickerFromShow = true }
                }
            )

            OutlinedTextFieldValidation(
                modifier = Modifier
                    .constrainAs(inputTimeFromRef) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 8.dp)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.5F
                    }
                    .padding(horizontal = 8.dp),
                value = state.timeFrom,
                onValueChange = { },
                label = { Text(text = stringResource(R.string.date_range_advanced_filter_screen_label_time_from)) },
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
                    IconButtonTime { timePickerFromShow = true }
                }
            )

            if (datePickerFromShow) {
                DatePickerDialog(
                    onDismissRequest = {
                        datePickerFromShow = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerFromState.selectedDateMillis?.let {
                                    val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                                    state.dateFrom = localDate.format(DateTimeFormatter.ofPattern(EnumDateTimePatterns.DATE.pattern))
                                }

                                datePickerFromShow = false
                            },
                        ) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { datePickerFromShow = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerFromState)
                }
            }

            if (timePickerFromShow) {
                TimePickerDialog(
                    title = "Selecionar Hora",
                    onConfirm = {
                        val localTime = LocalTime.of(timePickerFromState.hour, timePickerFromState.minute)
                        state.timeFrom = localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                        timePickerFromShow = false
                    },
                    onCancel = {
                        timePickerFromShow = false
                    }
                ) {
                    TimePicker(state = timePickerFromState)
                }
            }

            OutlinedTextFieldValidation(
                modifier = Modifier
                    .constrainAs(inputDateToRef) {
                        start.linkTo(parent.start)
                        top.linkTo(inputDateFromRef.bottom, margin = 8.dp)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.5F
                    }
                    .padding(horizontal = 8.dp),
                value = state.dateTo,
                onValueChange = { },
                label = { Text(text = stringResource(R.string.date_range_advanced_filter_screen_label_date_to)) },
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
                    IconButtonCalendar { datePickerToShow = true }
                }
            )

            OutlinedTextFieldValidation(
                modifier = Modifier
                    .constrainAs(inputTimeToRef) {
                        end.linkTo(parent.end)
                        top.linkTo(inputTimeFromRef.bottom, margin = 8.dp)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.5F
                    }
                    .padding(horizontal = 8.dp),
                value = state.timeTo,
                onValueChange = { },
                label = { Text(text = stringResource(R.string.date_range_advanced_filter_screen_label_time_to)) },
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
                    IconButtonTime { timePickerToShow = true }
                }
            )

            if (datePickerToShow) {
                DatePickerDialog(
                    onDismissRequest = {
                        datePickerToShow = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerToState.selectedDateMillis?.let {
                                    val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                                    state.dateTo = localDate.format(DateTimeFormatter.ofPattern(EnumDateTimePatterns.DATE.pattern))
                                }

                                datePickerToShow = false
                            },
                        ) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { datePickerToShow = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerToState)
                }
            }

            if (timePickerToShow) {
                TimePickerDialog(
                    title = "Selecionar Hora",
                    onConfirm = {
                        val localTime = LocalTime.of(timePickerToState.hour, timePickerToState.minute)
                        state.timeTo = localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                        timePickerToShow = false
                    },
                    onCancel = {
                        timePickerToShow = false
                    }
                ) {
                    TimePicker(state = timePickerToState)
                }
            }

            OutlinedButton(
                modifier = Modifier.constrainAs(buttonConfirmRef) {
                    end.linkTo(parent.end, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                },
                onClick = {
                    val localDateTimeFrom = getLocalDateTimeFrom(state)
                    val localDateTimeTo = getLocalDateTimeTo(state)
                    val pair = getPairLocalDateTimeRange(localDateTimeFrom, localDateTimeTo)

                    onConfirmClick(pair)
                },
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

private fun getLocalDateTimeFrom(state: DateRangeAdvancedFilterUIState) = when {
    state.dateFrom?.isNotEmpty() == true && state.timeFrom?.isNotEmpty() == true -> {
        LocalDateTime.of(
            state.dateFrom?.parseToLocalDate(EnumDateTimePatterns.DATE),
            state.timeFrom?.parseToLocalTime(EnumDateTimePatterns.TIME)
        )
    }

    state.dateFrom?.isNotEmpty() == true -> {
        LocalDateTime.of(
            state.dateFrom?.parseToLocalDate(EnumDateTimePatterns.DATE),
            LocalTime.MIN
        )
    }

    state.timeFrom?.isNotEmpty() == true -> {
        LocalDateTime.of(
            LocalDate.now(),
            state.timeFrom?.parseToLocalTime(EnumDateTimePatterns.TIME)
        )
    }

    else -> {
        null
    }
}

private fun getLocalDateTimeTo(state: DateRangeAdvancedFilterUIState) = when {
    state.dateTo?.isNotEmpty() == true && state.timeTo?.isNotEmpty() == true -> {
        LocalDateTime.of(
            state.dateTo?.parseToLocalDate(EnumDateTimePatterns.DATE),
            state.timeTo?.parseToLocalTime(EnumDateTimePatterns.TIME)
        )
    }

    state.dateTo?.isNotEmpty() == true -> {
        LocalDateTime.of(
            state.dateTo?.parseToLocalDate(EnumDateTimePatterns.DATE),
            LocalTime.MIN
        )
    }

    state.timeTo?.isNotEmpty() == true -> {
        LocalDateTime.of(
            LocalDate.now(),
            state.timeTo?.parseToLocalTime(EnumDateTimePatterns.TIME)
        )
    }

    else -> {
        null
    }
}

private fun getPairLocalDateTimeRange(
    localDateTimeFrom: LocalDateTime?,
    localDateTimeTo: LocalDateTime?
) = if (localDateTimeFrom != null || localDateTimeTo != null) {
    Pair(localDateTimeFrom, localDateTimeTo)
} else {
    null
}

@Preview
@Composable
fun DateRangeAdvancedFilterPreview() {
    MarketTheme {
        Surface {
            InputDateTimeRange()
        }
    }
}