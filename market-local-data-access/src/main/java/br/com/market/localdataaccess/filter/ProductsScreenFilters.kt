package br.com.market.localdataaccess.filter

import br.com.market.core.inputs.values.FilterValue

class ProductsScreenFilters(
    var category: FilterValue<Pair<String, String>?> = FilterValue(),
    var brand: FilterValue<Pair<String, String>?> = FilterValue(),
    var market: FilterValue<Pair<String, String>?> = FilterValue(),
    var priceRange: FilterValue<Pair<Double, Double>> = FilterValue(),
    var onlyInStock: FilterValue<Boolean> = FilterValue(),
)