package br.com.market.core.ui.components.buttons.fab

import androidx.compose.ui.graphics.painter.Painter

class SubActionFabItem(
    val icon: Painter,
    val label: String = "",
    val onFabItemClicked: () -> Unit = { }
)