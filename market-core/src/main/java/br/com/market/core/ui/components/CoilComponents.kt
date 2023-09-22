package br.com.market.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.theme.GREY_900_TRANSPARENT
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.buttons.IconButtonClose
import coil.compose.SubcomposeAsyncImage

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
            model = data,
            contentDescription = stringResource(R.string.label_image),
            contentScale = contentScale,
            modifier = imageModifier,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            error = {
                if (data.toString().isEmpty()) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        Image(
                            painter = painterResource(id = R.drawable.placeholder),
                            contentDescription = stringResource(id = R.string.placeholder),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        Image(
                            painter = painterResource(id = R.drawable.warnin_100),
                            contentDescription = stringResource(id = R.string.load_image_error),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
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