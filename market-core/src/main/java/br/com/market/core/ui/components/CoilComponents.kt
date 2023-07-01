package br.com.market.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.theme.GREY_900_TRANSPARENT
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.buttons.IconButtonClose
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/**
 * Componente de exibição de imagens usando a lib do Coil.
 *
 * Foi implementado definindo algumas configurações padrões para
 * todas exibição no APP.
 *
 * @param imageModifier Modificadores específicos para cada tela.
 * @param data Fonte de dados para exibir a imagem, por ser uma URL, por exemplo.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun CoilImageViewer(
    containerModifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    data: Any? = null,
    contentScale: ContentScale = ContentScale.Crop,
    showDeleteButton: Boolean = false,
    onDeleteButtonClick: () -> Unit = { }
) {
    ConstraintLayout(containerModifier) {
        val (buttonDeleteRef) = createRefs()

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data)
                .crossfade(true)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .build(),
            contentDescription = stringResource(R.string.label_image),
            contentScale = contentScale,
            modifier = imageModifier
        )

        if (showDeleteButton) {
            IconButtonClose(
                iconModifier = Modifier.size(20.dp),
                buttonModifier = Modifier
                    .background(color = GREY_900_TRANSPARENT, shape = RoundedCornerShape(percent = 100))
                    .size(40.dp)
                    .constrainAs(buttonDeleteRef) {
                        top.linkTo(parent.top, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                    },
                iconColor = Color.White,
                onClick = onDeleteButtonClick
            )
        }

    }
}

@Preview
@Composable
fun CoilImageViewerPreview() {
    MarketTheme {
        Surface {
            CoilImageViewer()
        }
    }
}