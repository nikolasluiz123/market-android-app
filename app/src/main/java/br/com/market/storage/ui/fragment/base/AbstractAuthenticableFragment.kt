package br.com.market.storage.ui.fragment.base

import br.com.market.storage.ui.viewmodel.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class AbstractAuthenticableFragment : AbstractBaseFragment() {

    protected val authenticationViewModel: AuthenticationViewModel by sharedViewModel()
}