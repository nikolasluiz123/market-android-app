package br.com.market.market.compose.components.button.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.market.core.R
import br.com.market.core.R.drawable

@Composable
fun IconButtonCalendar(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = drawable.ic_calendar_24dp), contentDescription = stringResource(R.string.label_calendar)
        )
    }
}