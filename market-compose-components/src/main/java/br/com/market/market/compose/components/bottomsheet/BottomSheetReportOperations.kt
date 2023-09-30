package br.com.market.market.compose.components.bottomsheet

import androidx.compose.runtime.Composable
import br.com.market.core.R
import br.com.market.core.ui.components.bottomsheet.report.BottomSheetReportOperationsItem
import br.com.market.core.ui.components.bottomsheet.report.EnumReportBottomSheetOptions

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