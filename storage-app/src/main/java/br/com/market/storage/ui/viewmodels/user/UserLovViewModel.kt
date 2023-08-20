package br.com.market.storage.ui.viewmodels.user

import androidx.lifecycle.ViewModel
import br.com.market.storage.repository.UserRepository
import br.com.market.storage.ui.states.user.UserLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserLovViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserLovUIState> = MutableStateFlow(UserLovUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        findUsers()
    }

    fun findUsers(name: String?  = null) {
        _uiState.update {
            it.copy(
                users = userRepository.findUsers(name = name)
            )
        }
    }
}
