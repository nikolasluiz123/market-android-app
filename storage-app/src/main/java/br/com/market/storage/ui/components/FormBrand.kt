package br.com.market.storage.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.domains.ProductBrandDomain
import br.com.market.storage.ui.states.FormProductUiState
import br.com.market.storage.ui.theme.CINZA_500
import br.com.market.storage.ui.theme.StorageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormBrand(
    state: FormProductUiState = FormProductUiState(),
    onDialogConfirmClick: (Long?, BrandDomain) -> Unit = { p, b -> }
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(100),
            onClick = state.onToggleBrandDialog
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val (brandsListRef, emptyText) = createRefs()

            if (state.openBrandDialog) {
                BrandDialog(
                    state = state,
                    onDissmissDialog = state.onToggleBrandDialog,
                    onConfirmClick = onDialogConfirmClick
                )
            }

            if (state.brands.isEmpty()) {
                Text(
                    modifier = Modifier.constrainAs(emptyText) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                    text = "Não há Marcas para esse Produto",
                    style = MaterialTheme.typography.titleMedium,
                    color = CINZA_500
                )
            } else {
                LazyColumn(
                    modifier = Modifier.constrainAs(brandsListRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        linkTo(top = parent.top, bottom = parent.bottom, bias = 0F)
                    },
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        items(state.brands) { productBrandDomain ->
                            CardBrandItem(productBrandDomain = productBrandDomain)
                        }
                    }
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun FormBrandPreview() {
    StorageTheme {
        Surface {
            FormBrand(
                state = FormProductUiState(
                    brands = listOf(
                        ProductBrandDomain(productName = "Arroz", brandName = "Dalfovo", count = 4),
                        ProductBrandDomain(productName = "Arroz", brandName = "Urbano", count = 15),
                        ProductBrandDomain(productName = "Arroz", brandName = "do Zé", count = 10)
                    )
                )
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FormBrandEmptyListPreview() {
    StorageTheme {
        Surface {
            FormBrand()
        }
    }
}