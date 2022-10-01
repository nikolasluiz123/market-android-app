package br.com.market.storage.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.market.storage.R
import br.com.market.storage.databinding.FragmentRegisterUserBinding
import br.com.market.storage.extensions.showSnackBar
import br.com.market.storage.model.User
import br.com.market.storage.transferobject.TOUser
import br.com.market.storage.ui.fragment.base.AbstractAuthenticableFragment

class RegisterUserFragment : AbstractAuthenticableFragment() {

    private var _binding: FragmentRegisterUserBinding? = null
    private val binding get() = _binding!!

    private val toUser by lazy { TOUser() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        binding.toUser = toUser

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRegisterUserButton()
    }

    private fun configureRegisterUserButton() {
        binding.registerUserRegisterButton.setOnClickListener {
            toUser.getUser()?.let { user ->
                if (validate(user)) {
                    authenticationViewModel.save(user).observe(viewLifecycleOwner) { resource ->
                        if (resource.data) {
                            view?.showSnackBar(getString(R.string.message_success_register_user))
                            navController.popBackStack()
                        } else {
                            val errorMessage = resource.error
                                ?: getString(R.string.generic_error_message_register_user)
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
            binding.registerUserInputLayoutEmail.error = getString(R.string.email_required)
            valid = false
        }

        if (user.password.isBlank()) {
            binding.registerUserInputLayoutPassword.error = getString(R.string.password_required)
            valid = false
        }

        return valid
    }

    private fun clearErrors() {
        binding.registerUserInputLayoutEmail.error = null
        binding.registerUserInputLayoutPassword.error = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}