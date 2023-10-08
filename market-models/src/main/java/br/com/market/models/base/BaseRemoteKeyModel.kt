package br.com.market.models.base

abstract class BaseRemoteKeyModel {
    abstract val id: String
    abstract val prevKey: Int?
    abstract val currentPage: Int
    abstract val nextKey: Int?
    abstract val createdAt: Long
}