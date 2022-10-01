package br.com.market.storage.ui.fragment.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import br.com.market.storage.NavGraphDirections
import br.com.market.storage.R

abstract class AbstractSessionedFragment : AbstractAuthenticableFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyLoggedUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureMenu()
    }

    private fun configureMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_logout, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_item_logout) {
                    onLogout()
                }

                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun onLogout() {
        authenticationViewModel.logout()
        navigateToLogin()
    }

    private fun verifyLoggedUser() {
        if (authenticationViewModel.isNotLogged()) {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        navController.navigate(NavGraphDirections.actionGlobalLoginFragment())
    }
}