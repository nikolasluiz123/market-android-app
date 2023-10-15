package br.com.market.market.compose.components.lov.viewmodel

import androidx.lifecycle.ViewModel
import br.com.market.market.common.repository.lov.UserLovRepository
import br.com.market.market.compose.components.lov.state.UserLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserLovViewModel @Inject constructor(
    private val userRepository: UserLovRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserLovUIState> = MutableStateFlow(UserLovUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        findUsers()
    }

    fun findUsers(name: String?  = null) {
        _uiState.update {
            it.copy(
                users = emptyFlow()
            )
        }
    }
}
