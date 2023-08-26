package br.com.market.core.ui.components.buttons.fab

import androidx.compose.ui.graphics.painter.Painter

/**
 * Item de sub-ação para o FAB com suporte a múltiplas ações.
 *
 * @param icon O ícone a ser exibido.
 * @param label O rótulo a ser exibido.
 * @param onFabItemClicked A ação a ser executada quando o item de FAB é clicado.
 * @constructor Cria um item de sub-ação.
 * @uthor Nikolas Luiz Schmitt
 */
class SubActionFabItem(
    val icon: Painter,
    val label: String = "",
    val onFabItemClicked: () -> Unit = { }
)
