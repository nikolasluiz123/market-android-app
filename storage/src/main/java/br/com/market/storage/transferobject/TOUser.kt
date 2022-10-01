package br.com.market.storage.transferobject

import androidx.lifecycle.MutableLiveData
import br.com.market.storage.model.User

class TOUser (private var user: User = User(),
              val email: MutableLiveData<String> = MutableLiveData<String>().also { it.value = user.email},
              val password: MutableLiveData<String> = MutableLiveData<String>().also { it.value = user.password}
) {

    fun getUser(): User? {
        return this.user.copy(
            email = email.value ?: return null,
            password = password.value ?: return null
        )
    }
}