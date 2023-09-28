package br.com.market.core.ui.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.market.core.R
import br.com.market.core.theme.BLUE_50
import br.com.market.core.theme.GREY_800
import br.com.market.core.ui.components.bottomsheet.loadimage.BottomSheetLoadImageItem
import br.com.market.core.ui.components.bottomsheet.loadimage.EnumOptionsBottomSheetLoadImage
import br.com.market.core.ui.components.bottomsheet.report.BottomSheetReportOperationsItem
import br.com.market.core.ui.components.bottomsheet.report.EnumReportBottomSheetOptions

/**
 * Componente de bottomsheet que pode ser utilizado
 * de forma genérica
 *
 * @param items Itens que serão exibidos
 * @param onDismissRequest Callback executado ao sair do bottomsheet
 * @param onItemClickListener Callback executado ao clicar no item
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: IEnumOptionsBottomSheet> BottomSheet(
    items: List<IBottomSheetItem<T>>,
    onDismissRequest: () -> Unit,
    onItemClickListener: (T) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
        containerColor = BLUE_50
    ) {
        LazyColumn {
            items(items) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = it.labelResId),
                            style = MaterialTheme.typography.labelLarge,
                            color = GREY_800
                        )
                    },
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = it.iconResId),
                            contentDescription = stringResource(id = it.iconDescriptionResId)
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = BLUE_50),
                    modifier = Modifier.clickable {
                        onItemClickListener(it.option)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))
    }
}

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
fun BottomSheetReportOperations(
    onDismissRequest: () -> Unit,
    onItemClickListener: (EnumReportBottomSheetOptions) -> Unit
) {
    val items = listOf(
        BottomSheetReportOperationsItem(
            option = EnumReportBottomSheetOptions.SHARE,
            labelResId = R.string.label_share_bottom_sheet_report_options,
            iconResId = R.drawable.ic_share_24dp,
            iconDescriptionResId = R.string.label_icon_camera_bottom_sheet_image_description
        ),
        BottomSheetReportOperationsItem(
            option = EnumReportBottomSheetOptions.VIEW,
            labelResId = R.string.label_view_bottom_sheet_report_options,
            iconResId = R.drawable.ic_open_file_24dp,
            iconDescriptionResId = R.string.label_icon_view_file_bottom_sheet_report_description
        ),
        BottomSheetReportOperationsItem(
            option = EnumReportBottomSheetOptions.DELETE,
            labelResId = R.string.label_delete_bottom_sheet_report_options,
            iconResId = R.drawable.ic_delete_24dp,
            iconDescriptionResId = R.string.label_icon_deletar_bottom_sheet_report_description
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

