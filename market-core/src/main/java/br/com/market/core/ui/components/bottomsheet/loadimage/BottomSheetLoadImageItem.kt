package br.com.market.core.ui.components.bottomsheet.loadimage

import br.com.market.core.ui.components.bottomsheet.IBottomSheetItem

/**
 * Classe que represent um item no bottomsheet de carregamento de
 * imagens.
 *
 * @property option O valor do item usado em comparações para executar x ou y ação
 * @property labelResId Label exibido no item do bottomsheet
 * @property iconResId Ícone exibido no item do bottomsheet
 * @property iconDescriptionResId Descrição do ícone exibido no bottomsheet
 *
 * @author Nikolas Luiz Schmitt
 */
class BottomSheetLoadImageItem(
    override val option: EnumOptionsBottomSheetLoadImage,
    override val labelResId: Int,
    override val iconResId: Int,
    override val iconDescriptionResId: Int
) : IBottomSheetItem