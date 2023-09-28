package br.com.market.core.ui.components.bottomsheet.report

import br.com.market.core.ui.components.bottomsheet.IBottomSheetItem

/**
 * Classe que representa um item no bottomsheet com de operações
 * para realizar com relatórios PDF.
 *
 * @property option O valor do item usado em comparações para executar x ou y ação
 * @property labelResId Label exibido no item do bottomsheet
 * @property iconResId Ícone exibido no item do bottomsheet
 * @property iconDescriptionResId Descrição do ícone exibido no bottomsheet
 *
 * @author Nikolas Luiz Schmitt
 */
class BottomSheetReportOperationsItem(
    override val option: EnumReportBottomSheetOptions,
    override val labelResId: Int,
    override val iconResId: Int,
    override val iconDescriptionResId: Int
) : IBottomSheetItem<EnumReportBottomSheetOptions>