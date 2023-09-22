package br.com.market.core.ui.states.input

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.VisualTransformation

data class InputTextUIState(
    var titleResId: Int? = null,
    var value: String? = null,
    val maxLength: Int? = null,
    val keyboardOptions: KeyboardOptions = KeyboardOptions(),
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    var onValueChange: (String?) -> Unit = { }
)
