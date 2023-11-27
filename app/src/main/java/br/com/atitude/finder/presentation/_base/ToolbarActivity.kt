package br.com.atitude.finder.presentation._base

import androidx.appcompat.widget.Toolbar

abstract class ToolbarActivity: BaseActivity() {

    private var toolbar: Toolbar? = null

    fun configToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        this.toolbar = toolbar
    }

    fun setFinishOnBack() {
        setOnBackListener { finish() }
    }

    private fun setOnBackListener(callback: () -> Unit) {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(false)
        }

        toolbar?.setNavigationOnClickListener {
            callback()
        }
    }
}