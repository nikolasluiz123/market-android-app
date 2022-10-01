package br.com.market.storage.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.market.storage.R
import br.com.market.storage.databinding.FragmentLoginBinding
import br.com.market.storage.extensions.showSnackBar
import br.com.market.storage.model.User
import br.com.market.storage.transferobject.TOUser
import br.com.market.storage.ui.fragment.base.AbstractAuthenticableFragment

class LoginFragment : AbstractAuthenticableFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val toUser by lazy { TOUser() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.toUser = toUser

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureLoginButton()
        configureRegisterUserButton()
    }

    private fun configureLoginButton() {
        binding.loginButton.setOnClickListener {
            toUser.getUser()?.let { user ->
                if (validate(user)) {
                    authenticationViewModel.login(user).observe(viewLifecycleOwner) { resource ->
                        if (resource.data) {
                            navController.navigate(LoginFragmentDirections.actionLoginFragmentToListaProdutosFragment())
                        } else {
                            val errorMessage =
                                resource.error ?: getString(R.string.generic_error_message_login)
                            view?.showSnackBar(errorMessage)
                        }
                    }
                }
            }
        }
    }

    private fun validate(user: User): Boolean {
        var valid = true

        clearErrors()

        if (user.email.isBlank()) {
            binding.loginInputLayoutEmail.error = getString(R.string.email_required)
            valid = false
        }

        if (user.password.isBlank()) {
            binding.loginInputLayoutPassword.error = getString(R.string.password_required)
            valid = false
        }

        return valid
    }

    private fun clearErrors() {
        binding.loginInputLayoutEmail.error = null
        binding.loginInputLayoutPassword.error = null
    }

    private fun configureRegisterUserButton() {
        binding.userRegisterButton.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterUserFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}