package br.com.market.core.filter.arguments

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Argumentos para um filtro avançado de números.
 *
 * Esta classe define os argumentos para um filtro avançado de números, incluindo se o número é inteiro ou não,
 * um recurso de ID de título e um valor associado ao filtro.
 *
 * @param integer Indica se o valor do filtro é um número inteiro.
 * @param titleResId O ID do recurso de string que representa o título do filtro.
 * @param value O valor associado ao filtro.
 * @constructor Cria uma instância de [InputNumberArgs] com os atributos fornecidos.
 * @property integer Indica se o valor do filtro é um número inteiro.
 * @property titleResId O ID do recurso de string que representa o título do filtro.
 * @property value O valor associado ao filtro.
 */
class InputNumberArgs(
    val integer: Boolean,
    titleResId: Int,
    value: String = "",
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
    visualTransformation: VisualTransformation = VisualTransformation.None
) : InputArgs(titleResId, value, maxLength, keyboardOptions, visualTransformation)