package br.com.market.storage.extensions

import br.com.market.core.extensions.format
import br.com.market.enums.EnumUnit

fun Double.formatQuantityIn(unit: EnumUnit): String {
    return when (unit) {
        EnumUnit.KILO ->  "${this.format()} KG"
        EnumUnit.GRAM -> "${this.format()} G"
        EnumUnit.LITER -> "${this.format()} L"
        EnumUnit.MILLILITER -> "${this.format()} ML"
        EnumUnit.UNIT -> if(this > 1) "${this.format()} Unidades" else "${this.format()} Unidade"
        else -> ""
    }
}