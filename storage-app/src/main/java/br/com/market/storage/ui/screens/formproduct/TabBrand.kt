package br.com.market.storage.ui.screens.formproduct

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.storage.R
import br.com.market.storage.ui.components.CardBrandItem
import br.com.market.storage.ui.components.LazyVerticalListComponent
import br.com.market.storage.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.storage.ui.components.buttons.FloatingActionButtonSave
import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.domains.ProductBrandDomain
import br.com.market.storage.ui.states.FormProductUiState
import br.com.market.storage.ui.theme.StorageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBrand(
    state: FormProductUiState = FormProductUiState(),
    onDialogConfirmClick: (BrandDomain) -> Unit = { },
    onMenuItemDeleteBrandClick: (Long) -> Unit = { }
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
                    onDissmissDialog = state.onHideBrandDialog,
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
    StorageTheme {
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
    StorageTheme {
        Surface {
            TabBrand()
        }
    }
}