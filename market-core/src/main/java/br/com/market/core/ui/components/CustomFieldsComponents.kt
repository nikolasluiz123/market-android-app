package br.com.market.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme

/**
 * Um campo de texto que pode ser posto dentro da AppBar.
 *
 * @param value Value digitado.
 * @param modifier Modificadores do componente.
 * @param onValueChange Listener para alteração do valor.
 * @param hint Dica do que deve ser inserido no campo.
 * @param keyboardOptions Opções de teclado.
 * @param keyboardActions Ações específicas que deverão ser exibidas no teclado.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarTextField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = { },
    hint: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textStyle = MaterialTheme.typography.bodyLarge
    val colors = TextFieldDefaults.textFieldColors(containerColor = Color.Unspecified)

    val textColor = Color.White
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor, lineHeight = 50.sp))

    val focusRequester = FocusRequester()
    SideEffect {
        focusRequester.requestFocus()
    }

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(value, TextRange(value.length)))
    }
    textFieldValue = textFieldValue.copy(text = value)

    CompositionLocalProvider(
        LocalTextSelectionColors provides LocalTextSelectionColors.current
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onValueChange(it.text.replace("\n", ""))
            },
            modifier = modifier
                .fillMaxWidth()
                .heightIn(32.dp)
                .indicatorLine(
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = colors
                )
                .focusRequester(focusRequester),
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = true,
            decorationBox = { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value,
                    visualTransformation = VisualTransformation.None,
                    innerTextField = innerTextField,
                    placeholder = { Text(text = hint, color = Color.White) },
                    singleLine = true,
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = PaddingValues(bottom = 4.dp)
                )
            }
        )
    }
}

/**
 * Campo de texto com comportamentos necessários para feedback de validações.
 *
 * @param value Valor digitado.
 * @param onValueChange Listener executado ao alterar o valor.
 * @param modifier Modificadores do componente.
 * @param enabled Flag para habilitar ou desabilitar o campo.
 * @param readOnly Flag para bloquear a escrita.
 * @param textStyle Estilo do texto.
 * @param label Label do campo.
 * @param placeholder Texto exibido como dica.
 * @param leadingIcon Ícone a direita.
 * @param error Mensagem de erro de alguma validação.
 * @param isError Flag que indica se o campo está com erro.
 * @param trailingIcon Ícone a esquerda do campo.
 * @param visualTransformation Alterações visuais da saída do texto digitado no campo.
 * @param keyboardOptions Opções de teclado.
 * @param keyboardActions Ações específicas que deverão ser exibidas no teclado.
 * @param singleLine Flag que indica se o campo se expandirá ao digitar mais que uma linha.
 * @param maxLines Número máximo de linhas que o campo poderá se expandir. Caso single line seja false.
 * @param interactionSource Personalização da aparência ou comportamento do campo dependendo do estado.
 * @param shape Forma do campo.
 * @param colors Cores do campo.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldValidation(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    error: String = "",
    isError: Boolean = error.isNotEmpty(),
    trailingIcon: @Composable (() -> Unit)? = {
        if (error.isNotEmpty())
            Icon(
                Icons.Default.Warning,
                "error",
                tint = MaterialTheme.colorScheme.error
            )
    },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        disabledTextColor = Color.Black
    )
) {
    OutlinedTextField(
        enabled = enabled,
        readOnly = readOnly,
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        supportingText = {
            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}

/**
 * Campo de texto com comportamento padrão para manipulação de senhas.
 *
 * @param value Valor digitado.
 * @param onValueChange Listener executado ao alterar o valor.
 * @param modifier Modificadores do componente.
 * @param label Label do campo.
 * @param error Mensagem de erro de alguma validação.
 * @param keyboardOptions Opções de teclado.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun OutlinedTextFieldPasswordValidation(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    error: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextFieldValidation(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        error = error,
        label = label,
        keyboardOptions = keyboardOptions,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            lateinit var description: String
            lateinit var image: ImageVector

            if (passwordVisible) {
                image = ImageVector.vectorResource(R.drawable.ic_visibility_24)
                description = stringResource(R.string.description_icon_hide_password)
            } else {
                image = ImageVector.vectorResource(R.drawable.ic_visibility_off_24)
                description = stringResource(R.string.description_icon_show_password)
            }

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        }
    )
}

@Preview
@Composable
fun OutlinedTextFieldValidationPreview() {
    MarketTheme {
        Surface {
            OutlinedTextFieldValidation(
                label = {
                    Text("Label")
                },
                value = "Valor digitado pelo usuário",
                onValueChange = { },
                error = "Mensagem de erro."
            )
        }
    }
}

@Preview
@Composable
fun OutlinedTextFieldValidationWithoutErrorPreview() {
    MarketTheme {
        Surface {
            OutlinedTextFieldValidation(
                label = {
                    Text("Label")
                },
                value = "Valor digitado pelo usuário",
                onValueChange = { }
            )
        }
    }
}



@Preview
@Composable
fun AppBarTextFieldPreview() {
    MarketTheme {
        Surface {
            AppBarTextField(value = "Arroz")
        }
    }
}