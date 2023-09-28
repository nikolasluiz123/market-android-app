package br.com.market.core.ui.components.bottomsheet

/**
 * Interface que define atributos e comportamentos padrões
 * de qualquer item de bottomsheet.
 *
 * @author Nikolas Luiz Schmitt
 */
interface IBottomSheetItem<T: IEnumOptionsBottomSheet> {
    val option: T
    val iconResId: Int
    val labelResId: Int
    val iconDescriptionResId: Int
}