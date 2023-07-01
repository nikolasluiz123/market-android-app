package br.com.market.sdo

data class ThemeDefinitionsSDO(
    var colorPrimary: String,
    var colorSecondary: String,
    var colorTertiary: String,
    var imageLogo: ByteArray,
    var id: Long? = null,
    var active: Boolean = true,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ThemeDefinitionsSDO

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
