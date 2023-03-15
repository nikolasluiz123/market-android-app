package br.com.market.storage.ui.screens.formproduct

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.LazyVerticalListComponent
import br.com.market.core.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.domain.BrandDomain
import br.com.market.domain.ProductBrandDomain
import br.com.market.storage.R
import br.com.market.storage.ui.states.FormProductUiState
import java.util.*

/**
 * Tab de Marcas
 *
 * @param state Estado da tela onde a tab é usada.
 * @param onDialogConfirmClick Listener executado ao clicar em confirmar na dialog de marcas.
 * @param onMenuItemDeleteBrandClick Listener executado ao clicar no item Deletar do menu das marcas.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun TabBrand(
    state: FormProductUiState = FormProductUiState(),
    onDialogConfirmClick: (BrandDomain) -> Unit = { },
    onMenuItemDeleteBrandClick: (UUID) -> Unit = { }
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButtonAdd { state.onShowBrandDialog(null) }
        }
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val (brandsListRef) = createRefs()

            if (state.openBrandDialog) {
                BrandDialog(
                    state = state,
                    onDismissDialog = state.onHideBrandDialog,
                    onConfirmClick = onDialogConfirmClick
                )
            }

            LazyVerticalListComponent(
                modifier = Modifier.constrainAs(brandsListRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    linkTo(top = parent.top, bottom = parent.bottom, bias = 0F)
                }.fillMaxSize(),
                items = state.brands,
                emptyStateText = stringResource(R.string.form_product_screen_tab_brand_empty_state_text)
            ) { productBrandDomain ->
                CardBrandItem(
                    productBrandDomain = productBrandDomain,
                    onItemClick = { itemClicked ->
                        state.onShowBrandDialog(itemClicked)
                    },
                    onMenuItemDeleteBrandClick = onMenuItemDeleteBrandClick
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun FormBrandPreview() {
    MarketTheme {
        Surface {
            TabBrand(
                state = FormProductUiState(
                    brands = listOf(
                        ProductBrandDomain(productName = "Arroz", brandName = "Dalfovo", count = 4),
                        ProductBrandDomain(productName = "Arroz", brandName = "Urbano", count = 15),
                        ProductBrandDomain(productName = "Arroz", brandName = "do Zé", count = 10)
                    )
                ),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FormBrandEmptyListPreview() {
    MarketTheme {
        Surface {
            TabBrand()
        }
    }
}