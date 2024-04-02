package br.com.atitude.finder.presentation._base

import android.view.Menu
import androidx.appcompat.widget.Toolbar
import br.com.atitude.finder.R

abstract class ToolbarActivity: BaseActivity() {

    private var toolbar: Toolbar? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    fun configToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.person -> {
                    startActivity(intentProfile())
                    return@setOnMenuItemClickListener true
                }

                else -> false
            }
        }
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