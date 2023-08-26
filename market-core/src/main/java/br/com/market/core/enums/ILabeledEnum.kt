package br.com.market.core.enums

/**
 * Interface para enumerações com suporte a rótulos.
 *
 * Esta interface define um contrato para enumerações que possuem um recurso de ID de rótulo.
 * O rótulo é geralmente utilizado para representar uma descrição ou nome legível por humanos associado a um valor de enumeração.
 *
 * @property labelResId O ID do recurso de rótulo associado à enumeração.
 * @author Nikolas Luiz Schmitt
 */
interface ILabeledEnum {

    /**
     * O ID do recurso de rótulo associado à enumeração.
     */
    val labelResId: Int
}
