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
import br.com.market.storage.ui.screens.LoginScreen
import br.com.market.storage.ui.screens.StorageProductsScreen
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.viewmodels.LoginViewModel
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel

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
                                navController = navController, startDestination = "login_screen"
                            ) {
                                composable(route = "login_screen") {
                                    val loginViewModel by viewModels<LoginViewModel>()

                                    LoginScreen(
                                        viewModel = loginViewModel,
                                        onLoginClick = {
                                            navController.navigate("storage_products_screen")
                                        }
                                    )
                                }

                                composable(route = "storage_products_screen") {
                                    val storageProductsViewModel by viewModels<StorageProductsViewModel>()
                                    StorageProductsScreen(storageProductsViewModel)
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

