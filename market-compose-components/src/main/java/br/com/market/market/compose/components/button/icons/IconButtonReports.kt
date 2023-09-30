package br.com.market.market.compose.components.button.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import br.com.market.core.R.drawable

@Composable
fun IconButtonReports(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = drawable.ic_reports_in_folder_32dp),
            contentDescription = "Relatório"
        )
    }
}