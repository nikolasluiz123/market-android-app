package br.com.market.storage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.market.storage.ui.screens.FormProductScreen
import br.com.market.storage.ui.screens.LoginScreen
import br.com.market.storage.ui.screens.StorageProductsScreen
import br.com.market.storage.ui.screens.navigation.AppDestinations
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.viewmodels.FormProductViewModel
import br.com.market.storage.ui.viewmodels.LoginViewModel
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StorageTheme {
                Surface {
                    App(
                        content = {
                            val navController: NavHostController = rememberNavController()

                            NavHost(
                                navController = navController, startDestination = AppDestinations.Login.route
                            ) {
                                composable(route = AppDestinations.Login.route) {
                                    val loginViewModel by viewModels<LoginViewModel>()

                                    LoginScreen(
                                        viewModel = loginViewModel,
                                        onLoginClick = {
                                            navController.navigate(AppDestinations.StorageProducts.route) {
                                                popUpTo(AppDestinations.Login.route) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    )
                                }

                                composable(route = AppDestinations.StorageProducts.route) {
                                    val storageProductsViewModel by viewModels<StorageProductsViewModel>()
                                    StorageProductsScreen(
                                        viewModel = storageProductsViewModel,
                                        onItemClick = { productId ->
                                            navController.navigate("${AppDestinations.FormProduct.route}?productId={$productId}")
                                        },
                                        onLogoutClick = {
                                            navController.navigate(AppDestinations.Login.route) {
                                                popUpTo(AppDestinations.StorageProducts.route) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        onFABNewProductClick = { navController.navigate(AppDestinations.FormProduct.route) }
                                    )
                                }

                                composable(
                                    route = "${AppDestinations.FormProduct.route}?productId={productId}",
                                    arguments = listOf(
                                        navArgument("productId") {
                                            type = NavType.StringType
                                            nullable = true
                                        }
                                    )
                                ) { backstackEntry ->
                                    val formProductViewModel by viewModels<FormProductViewModel>()
                                    val productId = backstackEntry.arguments?.getString("productId")
                                    formProductViewModel.updateFormInfos(productId)

                                    FormProductScreen(
                                        viewModel = formProductViewModel,
                                        onLogoutClick = {
                                            navController.navigate(AppDestinations.Login.route) {
                                                popUpTo(AppDestinations.StorageProducts.route) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        onBackClick = {
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(content: @Composable () -> Unit = { LoginScreen() }) {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AppPreview() {
    StorageTheme {
        Surface {
            App()
        }
    }
}

