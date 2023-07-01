package br.com.market.storage.ui.states

data class AboutUIState(
    var imageLogo: Any? = null,
    var companyName: String = "",
    var deviceName: String = "",
    var deviceId: String = "",
    var showBack: Boolean = true
)
