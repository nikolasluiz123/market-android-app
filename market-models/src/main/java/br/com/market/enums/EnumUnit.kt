package br.com.market.enums

import br.com.market.core.enums.ILabeledEnum
import br.com.market.models.R

enum class EnumUnit(override val labelResId: Int) : ILabeledEnum {

    KILO(R.string.enum_unit_label_kilo),
    GRAM(R.string.enum_unit_label_gram),
    UNIT(R.string.enum_unit_label_unity),
    LITER(R.string.enum_unit_label_liter),
    MILLILITER(R.string.enum_unit_label_milliliter)

//
//    CUBIC_METER(0),
//    CUBIC_CENTIMETER(0)
}