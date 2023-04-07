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
import br.com.market.core.R
import br.com.market.core.theme.BLUE_50
import br.com.market.core.theme.GREY_800
import br.com.market.core.ui.components.bottomsheet.loadimage.BottomSheetLoadImageItem
import br.com.market.core.ui.components.bottomsheet.loadimage.EnumOptionsBottomSheetLoadImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    items: List<IBottomSheetItem>,
    onDismissRequest: () -> Unit,
    onItemClickListener: (IEnumOptionsBottomSheet) -> Unit
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
    }
}

@Composable
fun BottomSheetLoadImage(
    onDismissRequest: () -> Unit,
    onItemClickListener: (IEnumOptionsBottomSheet) -> Unit
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

