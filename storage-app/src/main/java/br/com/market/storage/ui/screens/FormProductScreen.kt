package br.com.market.storage.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.storage.ui.components.AppBarTextField
import br.com.market.storage.ui.components.FormBrand
import br.com.market.storage.ui.components.FormProduct
import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.ui.states.FormProductUiState
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.viewmodels.FormProductViewModel
import kotlinx.coroutines.launch

@Composable
fun FormProductScreen(
    viewModel: FormProductViewModel,
    onBackClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onAfterDeletePoduct: () -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    FormProductScreen(
        state = state,
        onBackClick = onBackClick,
        onLogoutClick = onLogoutClick,
        onFABSaveProductClick = {
            viewModel.saveProduct(it)
            Toast.makeText(
                context,
                "Produto Salvo com Sucesso.",
                Toast.LENGTH_LONG
            ).show()
        },
        onDeletePoduct = {
            viewModel.deleteProduct(it)
            onAfterDeletePoduct()
        },
        onDialogConfirmClick = { brand ->
            viewModel.saveBrand(brand)
            Toast.makeText(
                context,
                "Marca Salva com Sucesso.",
                Toast.LENGTH_LONG
            ).show()
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FormProductScreen(
    state: FormProductUiState = FormProductUiState(),
    onBackClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onFABSaveProductClick: (ProductDomain) -> Unit = { },
    onDeletePoduct: (Long?) -> Unit = { },
    onDialogConfirmClick: (BrandDomain) -> Unit = { b -> }
) {
    var tabIndex by remember { mutableStateOf(0) }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                if (state.openSearch) {
                    AppBarTextField(
                        value = state.searchText,
                        onValueChange = state.onSearchChange,
                        hint = "O que você procura?"
                    )
                } else {
                    Text(text = "Cadastrando um Produto", style = MaterialTheme.typography.titleMedium)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),
            navigationIcon = {
                IconButton(onClick = {
                    if (state.openSearch) {
                        state.onToggleSearch()
                    } else {
                        onBackClick()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            },
            actions = {
                if (!state.openSearch) {
                    if (tabIndex == 0) {

                        if (state.productId != null) {
                            IconButton(onClick = { onDeletePoduct(state.productId) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            }
                        }

                        var showMenu by remember { mutableStateOf(false) }

                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }

                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(text = { Text("Logout") }, onClick = onLogoutClick)
                        }

                    } else {
                        IconButton(onClick = state.onToggleSearch) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Pesquisar"
                            )
                        }

                        var showMenu by remember { mutableStateOf(false) }

                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }

                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(text = { Text("Logout") }, onClick = onLogoutClick)
                        }

                    }
                } else {
                    IconButton(onClick = { state.onSearchChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpar Pesquisa"
                        )
                    }
                }
            }
        )
    }) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val (tabRowRef, horizontalPagerRef) = createRefs()
            val tabTitles = listOf("Produto", "Marca")
            val pagerState = rememberPagerState()
            val coroutineScope = rememberCoroutineScope()

            TabRow(
                modifier = Modifier.constrainAs(tabRowRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                selectedTabIndex = tabIndex
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = {
                            coroutineScope.launch {
                                tabIndex = index
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title) }
                    )
                }
            }

            HorizontalPager(
                pageCount = tabTitles.size,
                state = pagerState,
                modifier = Modifier
                    .constrainAs(horizontalPagerRef) {
                        start.linkTo(parent.start)
                        top.linkTo(tabRowRef.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        height = Dimension.fillToConstraints
                    }
            ) { index ->
                tabIndex = index

                when (tabIndex) {
                    0 -> {
                        FormProduct(
                            state = state,
                            onFABSaveProductClick = onFABSaveProductClick
                        )
                    }
                    1 -> {
                        FormBrand(
                            state = state,
                            onDialogConfirmClick = onDialogConfirmClick
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FormProductScreenPreview() {
    StorageTheme {
        Surface {
            FormProductScreen()
        }
    }
}