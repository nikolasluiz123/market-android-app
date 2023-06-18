package br.com.market.core.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * Classe base com implementações comuns de um [PagingSource]
 *
 * @param DOMAIN Classe de Domínio utilizada em tela
 *
 * @author Nikolas Luiz Schmitt
 */
abstract class BasePagingSource<DOMAIN: Any> : PagingSource<Int, DOMAIN>() {

    /**
     * Função responsável por retornar os dados em forma de lista.
     *
     * @param offset Utilizado no offset da consulta
     * @param limit Utilizado no limit da consulta
     *
     * @author Nikolas Luiz Schmitt
     */
    abstract suspend fun getData(limit: Int, offset: Int): List<DOMAIN>

    override fun getRefreshKey(state: PagingState<Int, DOMAIN>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DOMAIN> {
        val key = params.key ?: 1

        val offset = getOffset(key = key, loadSize = params.loadSize)

        val data = getData(limit = params.loadSize, offset = offset)

        val nextKey = getNextKey(data = data, key = key, loadSize = params.loadSize)
        val prevKey = getPrevKey(key = key)

        return LoadResult.Page(
            data = data,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    /**
     * Função responsável por retornar a próxima página
     *
     * @param data Lista pesquisada
     * @param key Página atual
     * @param loadSize Quantidade de itens que são carregados
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getNextKey(
        data: List<DOMAIN>,
        key: Int,
        loadSize: Int
    ) = if (data.isEmpty()) null else key + (loadSize / PagingConfigUtils.PAGE_SIZE)

    /**
     * Função responsável por retornar o offset utilizado na consulta
     *
     * @param key Página atual
     * @param loadSize Quantidade de itens que são carregados
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getOffset(key: Int, loadSize: Int): Int {
        var offset = key - 1

        if (offset > 0) {
            offset *= loadSize
        }

        return offset
    }

    /**
     * Função responsável por retornar a página anterior
     *
     * @param key Página atual
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getPrevKey(key: Int) = if (key == 1) null else key - 1

}