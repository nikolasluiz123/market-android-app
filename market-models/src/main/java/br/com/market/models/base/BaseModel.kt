package br.com.market.models.base

import java.util.*

/**
 * Uma classe base para as Entidades da base local que contém
 * os atributos comuns.
 *
 * @property id Identificador da Entidade.
 * @property synchronized Flag que indica se o registro foi enviado para o serviço.
 * @property active Flag que indica se o registro está ativo.
 *
 * @author Nikolas Luiz Schmitt
 */
abstract class BaseModel {
    abstract var id: UUID
    abstract var synchronized: Boolean
    abstract var active: Boolean
}