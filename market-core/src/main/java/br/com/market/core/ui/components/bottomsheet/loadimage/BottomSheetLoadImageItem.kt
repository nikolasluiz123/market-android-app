package br.com.market.core.ui.components.bottomsheet.loadimage

import br.com.market.core.ui.components.bottomsheet.IBottomSheetItem

class BottomSheetLoadImageItem(
    override val option: EnumOptionsBottomSheetLoadImage,
    override val labelResId: Int,
    override val iconResId: Int,
    override val iconDescriptionResId: Int
) : IBottomSheetItem