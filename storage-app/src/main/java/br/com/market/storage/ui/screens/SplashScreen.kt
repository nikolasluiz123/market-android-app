package br.com.market.storage.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.market.storage.R
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.theme.colorSecondary
import kotlinx.coroutines.delay

/**
 * SplashScreen do APP
 *
 * @param onAfterDelay Listener executado após um delay de 1 segundo.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun SplashScreen(onAfterDelay: () -> Unit = { }) {
    LaunchedEffect(key1 = true) {
        delay(1000)
        onAfterDelay()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorSecondary)
    ) {
        Card(
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.size(150.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.box_svg),
                    contentDescription = null,
                    Modifier.size(100.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    StorageTheme {
        Surface {
            SplashScreen()
        }
    }
}