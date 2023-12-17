package br.com.market.core.ui.states.filter

import br.com.market.core.inputs.CommonAdvancedFilterItem

data class AdvancedFilterUIState(var filters: List<CommonAdvancedFilterItem<*>> = emptyList())


