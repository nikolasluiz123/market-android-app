package br.com.market.sdo

class AuthenticationResultSDO(
    var company: CompanySDO,
    var market: MarketSDO,
    var device: DeviceSDO,
    var user: UserSDO,
    var token: String,
    var userLocalId: String,
)