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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.market.storage.ui.screens.FormProductScreen
import br.com.market.storage.ui.screens.LoginScreen
import br.com.market.storage.ui.screens.StorageProductsScreen
import br.com.market.storage.ui.screens.navigation.formProductNavRoute
import br.com.market.storage.ui.screens.navigation.loginNavRoute
import br.com.market.storage.ui.screens.navigation.storageProductsNavRoute
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
                                navController = navController, startDestination = loginNavRoute
                            ) {
                                composable(route = loginNavRoute) {
                                    val loginViewModel by viewModels<LoginViewModel>()

                                    LoginScreen(
                                        viewModel = loginViewModel,
                                        onLoginClick = {
                                            navController.navigate(storageProductsNavRoute)
                                        }
                                    )
                                }

                                composable(route = storageProductsNavRoute) {
                                    val storageProductsViewModel by viewModels<StorageProductsViewModel>()
                                    StorageProductsScreen(
                                        viewModel = storageProductsViewModel,
                                        onItemClick = { navController.navigate(formProductNavRoute) },
                                        onLogoutClick = { navController.popBackStack(route = loginNavRoute, inclusive = false) },
                                        onFABNewProductClick = { navController.navigate(formProductNavRoute) }
                                    )
                                }

                                composable(route = formProductNavRoute) {
                                    val formProductViewModel by viewModels<FormProductViewModel>()
                                    FormProductScreen(
                                        viewModel = formProductViewModel,
                                        onLogoutClick = {
                                            navController.popBackStack(route = loginNavRoute, inclusive = false)
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

