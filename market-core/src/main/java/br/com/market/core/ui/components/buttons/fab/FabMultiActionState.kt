package br.com.market.core.ui.components.buttons.fab

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FabMultiActionState {
    var currentState by mutableStateOf(EnumFabMultiActionState.COLLAPSED)

    var transition: Transition<EnumFabMultiActionState>? = null

    val stateChange: () -> Unit = {
        currentState = if (transition?.currentState == EnumFabMultiActionState.EXPANDED ||
            transition?.isRunning == true && transition?.targetState == EnumFabMultiActionState.EXPANDED
        ) {
            EnumFabMultiActionState.COLLAPSED
        } else {
            EnumFabMultiActionState.EXPANDED
        }
    }
}