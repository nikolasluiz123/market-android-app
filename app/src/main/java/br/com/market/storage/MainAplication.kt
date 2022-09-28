package br.com.market.storage

import android.app.Application
import br.com.market.storage.injection.firebaseModule
import br.com.market.storage.injection.repositoryModule
import br.com.market.storage.injection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainAplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainAplication)
            modules(listOf(viewModelModule, repositoryModule, firebaseModule))
        }
    }
}