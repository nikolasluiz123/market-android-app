package br.com.market.models

/**
 * Uma classe base para as Entidades da base local que contém
 * os atributos comuns.
 *
 * @property synchronized Flag que indica se o registro foi enviado para o serviço.
 * @property active Flag que indica se o registro está ativo.
 *
 * @author Nikolas Luiz Schmitt
 */
abstract class BaseModel {
    abstract var synchronized: Boolean
    abstract var active: Boolean
}