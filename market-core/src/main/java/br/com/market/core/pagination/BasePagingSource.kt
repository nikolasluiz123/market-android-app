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
        val offset = getOffset(params)

        val data = getData(limit = params.loadSize, offset = offset)

        val nextKey = getNextKey(data, params, offset)

        return LoadResult.Page(
            data = data,
            prevKey = if (offset == 0) null else offset - 1,
            nextKey = nextKey
        )
    }

    /**
     * Função responsável por retornar o offset utilizado na consulta.
     *
     * Na primeira consulta, o offset deve ser 0. Da segunda em diante o
     * valor depende do tamanho da página, definido em [PagingSource.LoadParams.loadSize].
     *
     * @param params Parâmetros de carregamentos obtidos do [PagingSource.LoadParams]
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getOffset(params: LoadParams<Int>): Int {
        var offset = params.key ?: 0

        if (offset > 0) {
            offset *= params.loadSize
        }

        return offset
    }

    /**
     * Função que recupera qual será a próxima página.
     *
     * Se a quantidade de dados que precisam ser carregados
     * for menor que o valor definido para uma página deve
     * ser retornado null pois só precisamos de 1 página para
     * exibir tudo.
     *
     * Se a quantidade de dados for maior que uma página, a
     * cada chamada a função incrementa 1 no offset.
     *
     * @param data Lista com os dados
     * @param params Parâmetros do [PagingSource.LoadParams]
     * @param offset Offset atual que pode ou não ser incrementado
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getNextKey(
        data: List<DOMAIN>,
        params: LoadParams<Int>,
        offset: Int
    ) = if (data.size < params.loadSize) null else offset + 1
}