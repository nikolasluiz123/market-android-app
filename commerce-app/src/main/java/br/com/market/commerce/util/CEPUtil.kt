package br.com.market.commerce.util

object CEPUtil {

    fun isValid(cep: String): Boolean {
        val cepNumbers = cep.replace("[^0-9]".toRegex(), "")
        return cepNumbers.length == 8
    }

    fun format(cep: String?): String {
        if (cep.isNullOrEmpty()) return ""

        val cepClear = cep.replace(Regex("[^0-9]"), "")
        return "${cepClear.substring(0, 5)}-${cepClear.substring(5, 8)}"
    }

    fun unFormat(cep: String?): String {
        if (cep.isNullOrEmpty()) return ""

        return cep.replace(Regex("[^0-9]"), "")
    }
}