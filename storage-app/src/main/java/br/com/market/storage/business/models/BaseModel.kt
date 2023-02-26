package br.com.market.storage.business.models

abstract class BaseModel {
    abstract val synchronized: Boolean
    abstract val active: Boolean
}