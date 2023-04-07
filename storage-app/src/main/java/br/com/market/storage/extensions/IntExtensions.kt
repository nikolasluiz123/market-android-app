package br.com.market.storage.extensions

import br.com.market.enums.EnumUnit

fun Int.formatQuantityIn(unit: EnumUnit): String {
    return when (unit) {
        EnumUnit.KILO ->  "$this KG"
        EnumUnit.GRAM -> "$this G"
        EnumUnit.LITER -> "$this L"
        EnumUnit.MILLILITER -> "$this ML"
        EnumUnit.UNIT -> if(this > 1) "$this Unidades" else "$this Unidade"
        else -> ""
    }
}