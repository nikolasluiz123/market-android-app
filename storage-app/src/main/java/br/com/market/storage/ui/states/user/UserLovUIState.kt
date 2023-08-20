package br.com.market.storage.ui.states.user

import androidx.paging.PagingData
import br.com.market.domain.UserDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class UserLovUIState(
    val users: Flow<PagingData<UserDomain>> = emptyFlow()
)
