package br.com.market.storage.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.market.storage.model.User
import br.com.market.storage.repository.FirebaseAuthenticationRepository
import br.com.market.storage.repository.Resource

class AuthenticationViewModel(private val firebaseAuthRepository: FirebaseAuthenticationRepository) :
    ViewModel() {

    fun save(user: User): LiveData<Resource<Boolean>> = firebaseAuthRepository.save(user)

    fun login(user: User): LiveData<Resource<Boolean>> = firebaseAuthRepository.login(user)

    fun logout() = firebaseAuthRepository.logout()

    fun isNotLogged() = !firebaseAuthRepository.isLogged()
}