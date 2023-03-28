package br.com.market.models.base

import java.util.*

abstract class CompanyModel : BaseModel() {
    abstract var companyId: UUID?
}