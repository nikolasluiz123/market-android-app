package br.com.market.commerce.util

object CPFUtil {

    fun isValid(cpf: String): Boolean {
        val cpfNumbers = cpf.replace("[^0-9]".toRegex(), "")

        if (cpfNumbers.length != 11) {
            return false
        }

        val digit1 = calculateDigit(cpfNumbers.substring(0, 9))
        val digit2 = calculateDigit(cpfNumbers.substring(0, 9) + digit1)

        return cpfNumbers.substring(9, 10).toInt() == digit1 && cpfNumbers.substring(10, 11).toInt() == digit2
    }

    fun format(cpf: String?): String {
        if (cpf.isNullOrEmpty()) return ""

        val cpfClear = cpf.replace(Regex("[^0-9]"), "")
        return "${cpfClear.substring(0, 3)}.${cpfClear.substring(3, 6)}.${cpfClear.substring(6, 9)}-${cpfClear.substring(9, 11)}"
    }

    fun unFormat(cpf: String?): String {
        if (cpf.isNullOrEmpty()) return ""
        return cpf.replace(Regex("[^0-9]"), "")
    }

    private fun calculateDigit(base: String): Int {
        var sum = 0
        var weight = base.length + 1

        for (char in base) {
            sum += char.toString().toInt() * weight
            weight--
        }

        val result = sum % 11
        return if (result < 2) 0 else 11 - result
    }
}