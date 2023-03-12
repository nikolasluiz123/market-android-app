package br.com.market.storage.ui.components

import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.market.storage.R
import br.com.market.storage.ui.theme.CINZA_300
import br.com.market.storage.ui.theme.StorageTheme
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/**
 * Componente de exibição de imagens usando a lib do Coil.
 *
 * Foi implementado definindo algumas configurações padrões para
 * todas exibição no APP.
 *
 * @param modifier Modificadores específicos para cada tela.
 * @param data Fonte de dados para exibir a imagem, por ser uma URL, por exemplo.
 *
 * @author Nikolas Luiz Schmitt
 */
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