package br.com.market.core.ui.components.bottomsheet.loadimage

import br.com.market.core.ui.components.bottomsheet.IEnumOptionsBottomSheet

/**
 * Enumerador com as possíbilidades de escolha dentro do bottomsheet
 * de carregamento de imagens.
 *
 * @property index Índice da opção.
 *
 * @author Nikolas Luiz Schmitt
 */
enum class EnumOptionsBottomSheetLoadImage(override val index: Int) : IEnumOptionsBottomSheet {
    CAMERA(0),
    GALLERY(1),
    LINK(2);
}