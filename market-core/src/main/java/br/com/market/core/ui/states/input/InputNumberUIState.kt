package br.com.market.core.ui.states.input

import androidx.compose.foundation.text.KeyboardOptions

data class InputNumberUIState(
    var titleResId: Int? = null,
    var value: String = "",
    var integer: Boolean = true,
    var maxLength: Int? = null,
    var keyboardOptions: KeyboardOptions = KeyboardOptions(),
    var onValueChange: (String) -> Unit = { }
)
