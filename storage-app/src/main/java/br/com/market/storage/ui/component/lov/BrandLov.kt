package br.com.market.storage.ui.component.lov

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.storage.ui.screens.brand.BrandListCard
import br.com.market.storage.ui.states.brand.BrandLovUIState
import br.com.market.storage.ui.viewmodels.brand.BrandLovViewModel
import java.util.*

@Composable
fun BrandLov(
    viewModel: BrandLovViewModel,
    onItemClick: (UUID) -> Unit = { },
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    BrandLov(
        state = state,
        onItemClick = onItemClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandLov(
    state: BrandLovUIState = BrandLovUIState(),
    onItemClick: (UUID) -> Unit = { },
    onBackClick: () -> Unit = { }
) {
    val pagingData = state.brands.collectAsLazyPagingItems()
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Marcas",
                showMenuWithLogout = false,
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        ConstraintLayout(modifier = Modifier.padding(padding)) {
            PagedVerticalListComponent(pagingItems = pagingData) { brandDomain ->
                BrandListCard(
                    brandName = brandDomain.name,
                    active = brandDomain.active,
                    onItemClick = {
                        onItemClick(brandDomain.id!!)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun BrandLovPreview() {
    MarketTheme {
        Surface {
            BrandLov()
        }
    }
}