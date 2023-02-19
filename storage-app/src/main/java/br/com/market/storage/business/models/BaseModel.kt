package br.com.market.storage.business.models

abstract class BaseModel {
    abstract val sincronized: Boolean
    abstract val active: Boolean
}