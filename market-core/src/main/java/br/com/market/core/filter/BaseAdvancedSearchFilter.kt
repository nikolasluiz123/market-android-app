package br.com.market.core.filter

abstract class BaseAdvancedSearchFilter<T>(simpleFilter: String?) : BaseSearchFilter(simpleFilter) {
    abstract var filters: T
}