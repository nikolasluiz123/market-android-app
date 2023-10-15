package br.com.market.market.compose.components.lov.state

import androidx.paging.PagingData
import br.com.market.domain.UserDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class UserLovUIState(
    val users: Flow<PagingData<UserDomain>> = emptyFlow()
)
