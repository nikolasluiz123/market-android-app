package br.com.market.storage.ui.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import br.com.market.storage.R
import br.com.market.storage.ui.theme.CINZA_300
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun CoilImageViewer(
    modifier: Modifier = Modifier,
    data: Any
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .crossfade(true)
            .placeholder(R.drawable.placeholder)
            .build(),
        contentDescription = stringResource(R.string.label_image),
        contentScale = ContentScale.Crop,
        modifier = modifier.background(color = CINZA_300)
    )
}