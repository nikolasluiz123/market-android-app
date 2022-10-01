package br.com.market.storage.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.market.storage.model.User
import com.google.firebase.auth.*
import java.lang.Exception


class FirebaseAuthenticationRepository(private val firebaseAuthentication: FirebaseAuth) {

    fun save(user: User): LiveData<Resource<Boolean>> = MutableLiveData<Resource<Boolean>>().apply {
        firebaseAuthentication.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                value = Resource(task.isSuccessful, task.exception?.let(::getErrorMessageSave))
            }
    }

    private fun getErrorMessageSave(exception: Exception): String = when (exception) {
        is FirebaseAuthWeakPasswordException -> "A Senha precisa conter ao menos 6 dígitos"
        is FirebaseAuthInvalidCredentialsException -> "E-mail inválido"
        is FirebaseAuthUserCollisionException -> "E-mail já cadastrado"
        else -> "Erro desconhecido"
    }

    fun login(user: User): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            firebaseAuthentication.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    value = Resource(task.isSuccessful, task.exception?.let(::getErrorMessageLogin))
                }
        }

    private fun getErrorMessageLogin(exception: Exception?): String = when (exception) {
        is FirebaseAuthInvalidUserException,
        is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos"
        else -> "Erro desconhecido"
    }

    fun logout() = firebaseAuthentication.signOut()

    fun isLogged() = firebaseAuthentication.currentUser != null

}