package br.com.market.market.compose.components.bottomsheet

import androidx.compose.runtime.Composable
import br.com.market.core.R
import br.com.market.core.ui.components.bottomsheet.loadimage.BottomSheetLoadImageItem
import br.com.market.core.ui.components.bottomsheet.loadimage.EnumOptionsBottomSheetLoadImage

/**
 * Componente de bottomsheet específico para carregamento de imagens,
 * contém as opções padrões para tal ação.
 *
 * @param onDismissRequest Callback executado ao sair do bottomsheet
 * @param onItemClickListener Callback executado ao clicar no item
 *
 * @see BottomSheet
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun BottomSheetLoadImage(
    onDismissRequest: () -> Unit,
    onItemClickListener: (EnumOptionsBottomSheetLoadImage) -> Unit
) {
    val items = listOf(
        BottomSheetLoadImageItem(
            option = EnumOptionsBottomSheetLoadImage.CAMERA,
            labelResId = R.string.label_camera_bottom_sheet_image,
            iconResId = R.drawable.ic_camera_24dp,
            iconDescriptionResId = R.string.label_icon_camera_bottom_sheet_image_description
        ),
        BottomSheetLoadImageItem(
            option = EnumOptionsBottomSheetLoadImage.GALLERY,
            labelResId = R.string.label_gallery_bottom_sheet_image,
            iconResId = R.drawable.ic_gallery_24dp,
            iconDescriptionResId = R.string.label_icon_gallery_bottom_sheet_image_description
        ),
        BottomSheetLoadImageItem(
            option = EnumOptionsBottomSheetLoadImage.LINK,
            labelResId = R.string.label_link_bottom_sheet_image,
            iconResId = R.drawable.ic_download_24dp,
            iconDescriptionResId = R.string.label_icon_download_bottom_sheet_image_description
        )
    )

    BottomSheet(
        items = items,
        onDismissRequest = onDismissRequest,
        onItemClickListener = {
            onItemClickListener(it)
            onDismissRequest()
        }
    )
}