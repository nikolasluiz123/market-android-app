package br.com.market.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.market.core.R

/**
 * Dialog para exibição de mensagens
 *
 * @param title Título da dialog.
 * @param show Flag para exibir a dialog ou não.
 * @param onDismissRequest Ação ao fechar a dialog
 * @param message Mensagem
 * @param onDialogOkClick Ação específica ao clicar no botão OK.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun DialogMessage(
    title: String,
    show: Boolean,
    onDismissRequest: () -> Unit,
    message: String,
    onDialogOkClick: () -> Unit = { }
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title, style = MaterialTheme.typography.headlineMedium) },
            text = { Text(text = message, style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                        onDialogOkClick()
                    }
                ) {
                    Text(text = stringResource(id = R.string.dialog_button_ok_label))
                }
            }
        )
    }
}