package br.com.market.storage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.market.storage.ui.navigation.StorageAppNavHost
import br.com.market.storage.ui.screens.FormProductScreen
import br.com.market.storage.ui.screens.LoginScreen
import br.com.market.storage.ui.screens.SplashScreen
import br.com.market.storage.ui.screens.StorageProductsScreen
import br.com.market.storage.ui.navigation.StorrageAppDestinations
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
                            StorageAppNavHost(navController = rememberNavController())
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

