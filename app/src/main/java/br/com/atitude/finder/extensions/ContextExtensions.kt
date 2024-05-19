package br.com.atitude.finder.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(InputMethodManager::class.java)
    val view = currentFocus ?: View(this)
    view.clearFocus()
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}