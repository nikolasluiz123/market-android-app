package br.com.market.storage.ui.states

/**
 * Classe base para estados das telas do APP, contém os
 * atributos comuns entre as telas.
 *
 * @author Nikolas Luiz Schmitt
 */
abstract class BaseUiState {
    abstract val serverMessage: String
    abstract val showDialogMessage: Boolean
    abstract val onToggleMessageDialog: (String) -> Unit

    abstract val showLoading: Boolean
    abstract val onToggleLoading: () -> Unit

    abstract val onValidate: () -> Boolean
}