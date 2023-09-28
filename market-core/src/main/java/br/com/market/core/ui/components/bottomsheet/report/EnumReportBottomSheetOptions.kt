package br.com.market.core.ui.components.bottomsheet.report

import br.com.market.core.ui.components.bottomsheet.IEnumOptionsBottomSheet

/**
 * Enumerador com as possíbilidades de escolha dentro do bottomsheet
 * de um relatório.
 *
 * @property index Índice da opção.
 *
 * @author Nikolas Luiz Schmitt
 */
enum class EnumReportBottomSheetOptions(override val index: Int) : IEnumOptionsBottomSheet {
    SHARE(0),
    VIEW(1),
    DELETE(2);
}