package br.com.market.storage.ui.fragment.base

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

abstract class AbstractBaseFragment : Fragment() {

    protected val navController by lazy { findNavController() }
}