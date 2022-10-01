package br.com.market.storage.injection

import br.com.market.storage.repository.FirebaseAuthenticationRepository
import br.com.market.storage.ui.viewmodel.AuthenticationViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthenticationViewModel(get()) }
}

val repositoryModule = module {
    single { FirebaseAuthenticationRepository(get()) }
}

val firebaseModule = module {
    single { Firebase.auth }
}