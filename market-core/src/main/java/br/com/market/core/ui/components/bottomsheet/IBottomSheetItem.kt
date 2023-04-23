package br.com.market.core.ui.components.bottomsheet

/**
 * Interface que define atributos e comportamentos padrões
 * de qualquer item de bottomsheet.
 *
 * @author Nikolas Luiz Schmitt
 */
interface IBottomSheetItem {
    val option: IEnumOptionsBottomSheet
    val iconResId: Int
    val labelResId: Int
    val iconDescriptionResId: Int
}