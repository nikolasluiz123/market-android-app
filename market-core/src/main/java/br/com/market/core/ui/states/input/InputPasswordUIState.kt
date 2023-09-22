package br.com.market.core.ui.states.input

import androidx.compose.foundation.text.KeyboardOptions

data class InputPasswordUIState(
    var titleResId: Int? = null,
    var value: String? = null,
    val maxLength: Int? = null,
    val keyboardOptions: KeyboardOptions = KeyboardOptions(),
    var onValueChange: (String?) -> Unit = { }
)
