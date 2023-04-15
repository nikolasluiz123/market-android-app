package br.com.market.storage.ui.screens.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.BLUE_600
import br.com.market.core.theme.GREY_800
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.LazyVerticalListComponent
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.MarketCircularBlockUIProgressIndicator
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.FloatingActionButtonAdd
import br.com.market.core.ui.components.buttons.IconButtonDelete
import br.com.market.core.ui.components.buttons.IconButtonLogout
import br.com.market.storage.R
import br.com.market.storage.sampledata.sampleCategories
import br.com.market.storage.ui.states.category.CategorySearchUIState
import br.com.market.storage.ui.viewmodels.category.CategorySearchViewModel
import java.util.*

@Composable
fun CategorySearchScreen(
    viewModel: CategorySearchViewModel,
    onButtonBackClickFailureScreen: () -> Unit,
    onAddCategoryClick: () -> Unit,
    onCategoryClick: (UUID) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    CategorySearchScreen(
        state = state,
        onButtonBackClickFailureScreen = onButtonBackClickFailureScreen,
        onButtonRetryClick = {
            viewModel.findCategories()
        },
        onAddCategoryClick = onAddCategoryClick,
        onCategoryClick = onCategoryClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySearchScreen(
    state: CategorySearchUIState,
    onButtonBackClickFailureScreen: () -> Unit = { },
    onButtonRetryClick: () -> Unit = { },
    onDeleteCategoryClick: () -> Unit = { },
    onAddCategoryClick: () -> Unit = { },
    onCategoryClick: (UUID) -> Unit = { }
) {
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Categorias",
                showNavigationIcon = false,
                showMenuWithLogout = false,
                actions = {
                    IconButtonLogout()
                }
            )
        },
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    IconButtonDelete(
                        onClick = onDeleteCategoryClick,
                        enabled =  state is CategorySearchUIState.Success && state.categories.isNotEmpty()
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonAdd(onClick = onAddCategoryClick)
                }
            )
        }
    ) {
        when (state) {
            CategorySearchUIState.Failure -> {
                FailureScreen(
                    modifier = Modifier.padding(it),
                    onButtonBackClick = onButtonBackClickFailureScreen,
                    onButtonRetryClick = onButtonRetryClick
                )
            }
            CategorySearchUIState.Loading -> {
                MarketCircularBlockUIProgressIndicator(
                    show = true,
                    label = "Carregando"
                )
            }
            is CategorySearchUIState.Success -> {
                ConstraintLayout(modifier = Modifier.padding(it)) {
                    val (categoriesListRef) = createRefs()

                    LazyVerticalListComponent(
                        modifier = Modifier
                            .fillMaxSize()
                            .constrainAs(categoriesListRef) {
                                linkTo(start = parent.start, end = parent.end, bias = 0F)
                                linkTo(top = parent.top, bottom = parent.bottom, bias = 0F)
                            },
                        items = state.categories,
                        emptyStateText = stringResource(R.string.category_search_screen_empty_state_text)
                    ) { categoryDomain ->
                        CategoryListCard(
                            categoryName = categoryDomain.name,
                            active = categoryDomain.active,
                            onItemClick = {
                                onCategoryClick(categoryDomain.id!!)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FailureScreen(
    onButtonRetryClick: () -> Unit,
    onButtonBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    message: String = "Ocorreu um erro inesperado"
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (messageRef, retryButtonRef, backButtonRef) = createRefs()

            Text(
                text = message,
                color = GREY_800,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.constrainAs(messageRef) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(parent.top)
                }
            )

            Button(
                onClick = onButtonRetryClick,
                colors = ButtonDefaults.buttonColors(containerColor = BLUE_600),
                modifier = Modifier.constrainAs(retryButtonRef) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(messageRef.bottom, margin = 8.dp)
                }
            ) {
                Text(
                    text = "Tentar Novamente",
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            OutlinedButton(
                onClick = onButtonBackClick,
                border = BorderStroke(width = ButtonDefaults.outlinedButtonBorder.width, color = BLUE_600),
                modifier = Modifier.constrainAs(backButtonRef) {
                    linkTo(start = retryButtonRef.start, end = retryButtonRef.end)
                    top.linkTo(retryButtonRef.bottom, margin = 8.dp)

                    width = Dimension.fillToConstraints
                }

            ) {
                Text(
                    text = "Voltar",
                    color = BLUE_600,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}


@Preview
@Composable
fun CategorySearchScreenFailurePreview() {
    MarketTheme {
        Surface {
            CategorySearchScreen(state = CategorySearchUIState.Failure)
        }
    }
}

@Preview
@Composable
fun CategorySearchScreenLoadingPreview() {
    MarketTheme {
        Surface {
            CategorySearchScreen(state = CategorySearchUIState.Loading)
        }
    }
}

@Preview
@Composable
fun CategorySearchScreenEmptyListPreview() {
    MarketTheme {
        Surface {
            CategorySearchScreen(state = CategorySearchUIState.Success(emptyList()))
        }
    }
}

@Preview
@Composable
fun CategorySearchScreenPreview() {
    MarketTheme {
        Surface {
            CategorySearchScreen(state = CategorySearchUIState.Success(sampleCategories))
        }
    }
}