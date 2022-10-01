package br.com.market.storage.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}