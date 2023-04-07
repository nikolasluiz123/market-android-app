package br.com.market.storage.ui.screens.testes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.R
import br.com.market.core.ui.components.Banner
import br.com.market.core.ui.components.HorizontalGallery
import br.com.market.core.ui.components.MultiActionsFabBottomAppBar
import br.com.market.core.ui.components.bottomsheet.BottomSheetLoadImage
import br.com.market.core.ui.components.bottomsheet.IEnumOptionsBottomSheet
import br.com.market.core.ui.components.buttons.MarketFloatingActionButtonMultiActions
import br.com.market.core.ui.components.buttons.fab.SubActionFabItem
import br.com.market.core.ui.components.buttons.rememberFabMultiActionsState


@Composable
fun TestesBanner() {
    ConstraintLayout {
        val visible = remember { mutableStateOf(false) }
        val (buttonRef) = createRefs()

        Button(
            onClick = { visible.value = true },
            modifier = Modifier.constrainAs(buttonRef) {
                linkTo(start = parent.start, end = parent.end, top = parent.top, bottom = parent.bottom)
            }
        ) {
            Text(text = "Abrir")
        }

        Banner(
            isVisible = visible,
            message = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
        )
    }

}

@Composable
fun TestesGallery() {
    HorizontalGallery(
        images = listOf(
            "https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            "https://images.pexels.com/photos/2294477/pexels-photo-2294477.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            "https://images.pexels.com/photos/3584910/pexels-photo-3584910.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        onLoadClick = { },
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun TestesBottomSheet(
    onItemClickListener: (IEnumOptionsBottomSheet) -> Unit
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { openBottomSheet = !openBottomSheet }) {
            Text(text = "Abrir Bottom Sheet")
        }
    }

    if (openBottomSheet) {
        BottomSheetLoadImage(
            onDismissRequest = {
                openBottomSheet = false
            },
            onItemClickListener = {
                onItemClickListener(it)
            }
        )
    }
}

@Composable
fun TestesBottomAppBar() {
    val state = rememberFabMultiActionsState()

    Scaffold(
        bottomBar = {
            MultiActionsFabBottomAppBar(
                floatingActionButton = {
                    MarketFloatingActionButtonMultiActions(
                        state = state,
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.label_adicionar),
                            tint = Color.White
                        )
                    }
                },
                subActionsFab = listOf(
                    SubActionFabItem(icon = painterResource(id = R.drawable.ic_calendar_24dp), label = "Agendar Entrada"),
                    SubActionFabItem(icon = painterResource(id = R.drawable.ic_input_storage_24dp), label = "Entrada"),
                    SubActionFabItem(icon = painterResource(id = R.drawable.ic_warning_24dp), label = "Baixa")
                ),
                state = state
            )
        }
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {

        }
    }
}