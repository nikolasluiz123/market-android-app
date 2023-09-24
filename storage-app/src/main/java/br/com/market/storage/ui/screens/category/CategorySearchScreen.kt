package br.com.market.storage.ui.screens.category

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.market.core.R
import br.com.market.core.extensions.verifyCameraPermissionGranted
import br.com.market.core.extensions.verifyGalleryPermissionGranted
import br.com.market.core.extensions.verifyWriteExternalStoragePermissionGranted
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketBottomAppBar
import br.com.market.core.ui.components.MarketCircularBlockUIProgressIndicator
import br.com.market.core.ui.components.PagedVerticalListComponent
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.IconButtonInactivate
import br.com.market.core.ui.components.buttons.IconButtonLogout
import br.com.market.core.ui.components.buttons.IconButtonSync
import br.com.market.core.ui.components.buttons.fab.FloatingActionButtonAdd
import br.com.market.core.ui.components.filter.SimpleFilter
import br.com.market.core.utils.PermissionUtils
import br.com.market.storage.ui.states.category.CategorySearchUIState
import br.com.market.storage.ui.viewmodels.category.CategorySearchViewModel
import java.util.*

@Composable
fun CategorySearchScreen(
    viewModel: CategorySearchViewModel,
    onAddCategoryClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onAfterLogout: () -> Unit,
    onAboutClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    CategorySearchScreen(
        state = state,
        onAddCategoryClick = onAddCategoryClick,
        onCategoryClick = onCategoryClick,
        onSyncClick = { onFinish ->
            viewModel.sync(onFinish)
        },
        onLogoutClick = {
            viewModel.logout()
            onAfterLogout()
        },
        onAboutClick = onAboutClick,
        onSimpleFilterChange = viewModel::updateList
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySearchScreen(
    state: CategorySearchUIState,
    onDeleteCategoryClick: () -> Unit = { },
    onAddCategoryClick: () -> Unit = { },
    onCategoryClick: (String) -> Unit = { },
    onSyncClick: (onFinish: () -> Unit) -> Unit = { },
    onLogoutClick: () -> Unit = { },
    onAboutClick: () -> Unit = { },
    onSimpleFilterChange: (String) -> Unit = { }
) {
    val pagingData = state.categories.collectAsLazyPagingItems()
    var showLoadingBlockUI by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val requestPermissionLauncher = PermissionUtils.requestMultiplePermissionsLauncher()

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(br.com.market.storage.R.string.category_search_title),
                showNavigationIcon = false,
                showMenuWithLogout = false,
                showMenu = true,
                actions = {
                    IconButtonSync {
                        showLoadingBlockUI = true
                        onSyncClick {
                            showLoadingBlockUI = false
                        }
                    }
                    IconButtonLogout(onLogoutClick)
                },
                menuItems = {
                    DropdownMenuItem(text = { Text(stringResource(R.string.label_about)) }, onClick = onAboutClick)
                }
            )
        },
        bottomBar = {
            MarketBottomAppBar(
                actions = {
                    IconButtonInactivate(
                        onClick = onDeleteCategoryClick,
                        enabled = pagingData.itemCount > 0
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonAdd(onClick = onAddCategoryClick)
                }
            )
        }
    ) { padding ->

        LaunchedEffect(Unit) {
            val permissions = mutableListOf<String>()

            if (!context.verifyWriteExternalStoragePermissionGranted()) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            if (!context.verifyCameraPermissionGranted()) {
                permissions.add(Manifest.permission.CAMERA)
            }

            if (!context.verifyGalleryPermissionGranted()) {
                permissions.add(PermissionUtils.getMediaImagesPermission())
            }

            requestPermissionLauncher.launch(permissions.toTypedArray())
        }

        ConstraintLayout(modifier = Modifier.padding(padding)) {
            val (listRef, searchBarRef, searchDividerRef) = createRefs()
            var searchActive by remember { mutableStateOf(false) }

            MarketCircularBlockUIProgressIndicator(
                show = showLoadingBlockUI,
                label = stringResource(R.string.label_sincronizing)
            )

            SimpleFilter(
                modifier = Modifier
                    .constrainAs(searchBarRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth(),
                onSimpleFilterChange = onSimpleFilterChange,
                active = searchActive,
                onActiveChange = { searchActive = it },
                placeholderResId = br.com.market.storage.R.string.category_screen_tab_brand_buscar_por
            ) {
                PagedVerticalListComponent(pagingItems = pagingData) {
                    CategoryListItem(
                        categoryName = it.name,
                        active = it.active,
                        onItemClick = {
                            onCategoryClick(it.id!!)
                        }
                    )
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }

            if (!searchActive) {
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(searchDividerRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            top.linkTo(searchBarRef.bottom)
                        }
                        .padding(top = 8.dp)
                )

                PagedVerticalListComponent(
                    pagingItems = pagingData,
                    modifier = Modifier
                        .constrainAs(listRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            linkTo(top = searchDividerRef.bottom, bottom = parent.bottom, bias = 0F)
                        }
                        .padding(bottom = 74.dp)
                ) {
                    CategoryListItem(
                        categoryName = it.name,
                        active = it.active,
                        onItemClick = {
                            onCategoryClick(it.id!!)
                        }
                    )
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Preview
@Composable
fun CategorySearchScreenPreview() {
    MarketTheme {
        Surface {
            CategorySearchScreen(state = CategorySearchUIState())
        }
    }
}