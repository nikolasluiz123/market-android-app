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

    /**
     * Função para obter a configuração de paginação
     * padrão.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun defaultPagingConfig() = PagingConfig(
        pageSize = 50,
        prefetchDistance = 25,
        enablePlaceholders = false,
        initialLoadSize = 50
    )

    /**
     * Função para obter uma configuração de paginação
     * customizada baseada no [pageSize] informado
     *
     * @param pageSize Tamanho desejado de cada página.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun customPagingConfig(pageSize: Int) = PagingConfig(
        pageSize = pageSize,
        prefetchDistance = pageSize / 2,
        enablePlaceholders = false,
        initialLoadSize = pageSize
    )
}