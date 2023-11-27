package br.com.atitude.finder.extensions

import android.content.Context
import android.view.View
import android.widget.Toast

fun View.visibleOrGone(visible: Boolean) {
    this.visibility = if(visible) View.VISIBLE else View.GONE
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}