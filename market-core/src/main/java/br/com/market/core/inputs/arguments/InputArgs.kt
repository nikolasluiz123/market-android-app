package br.com.market.core.inputs.arguments

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Classe base para os argumentos de um filtro avançado.
 *
 * Esta classe define os argumentos básicos para um filtro avançado, incluindo um recurso de ID de título
 * e um valor associado ao filtro.
 *
 * @param titleResId O ID do recurso de string que representa o título do filtro.
 * @param value O valor associado ao filtro. Pode ser nulo.
 * @constructor Cria uma instância de [InputArgs] com o ID do recurso de título e o valor associado.
 * @property titleResId O ID do recurso de string que representa o título do filtro.
 * @property value O valor associado ao filtro.
 * @author Nikolas Luiz Schmitt
 */
open class InputArgs(
    val titleResId: Int,
    val value: Any? = null,
    val maxLength: Int? = null,
    val keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    val visualTransformation: VisualTransformation = VisualTransformation.None,
)
