package br.com.market.core.inputs.arguments

import androidx.compose.foundation.text.KeyboardOptions

class InputPasswordArgs(
    val titleResId: Int,
    val value: Any? = null,
    val maxLength: Int? = null,
    val keyboardOptions: KeyboardOptions = KeyboardOptions(),
)
