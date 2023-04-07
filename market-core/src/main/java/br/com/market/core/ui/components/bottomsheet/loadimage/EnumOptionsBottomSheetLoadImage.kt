package br.com.market.core.ui.components.bottomsheet.loadimage

import br.com.market.core.ui.components.bottomsheet.IEnumOptionsBottomSheet

enum class EnumOptionsBottomSheetLoadImage(override val index: Int) : IEnumOptionsBottomSheet {
    CAMERA(0),
    GALLERY(1),
    LINK(2);
}