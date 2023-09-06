package br.com.market.core.pagination

import androidx.paging.PagingConfig

/**
 * Object utilitário que contém funções para obter
 * diferentes [PagingConfig] de acordo com a necessidade
 * de cada tela de consulta.
 *
 * @author Nikolas Luiz Schmitt
 */
object PagingConfigUtils {

    const val PAGE_SIZE = 50
    private const val INITIAL_LOAD_SIZE = PAGE_SIZE
    private const val PREFETCH_DISTANCE = PAGE_SIZE * 2

    /**
     * Função para obter a configuração de paginação
     * padrão.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun defaultPagingConfig() = PagingConfig(
        pageSize = PAGE_SIZE,
        prefetchDistance = PREFETCH_DISTANCE,
        enablePlaceholders = false,
        initialLoadSize = INITIAL_LOAD_SIZE
    )
}