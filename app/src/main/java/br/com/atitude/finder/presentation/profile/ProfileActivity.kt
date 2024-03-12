package br.com.atitude.finder.presentation.profile

import android.os.Bundle
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivityProfileBinding
import br.com.atitude.finder.domain.User
import br.com.atitude.finder.extensions.toDMYString
import br.com.atitude.finder.extensions.toHMString
import br.com.atitude.finder.extensions.visibleOrGone
import br.com.atitude.finder.presentation._base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun getViewModel() = profileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configLogoutButton()
        observeUser()
        getViewModel().fetchUser()
    }

    private fun observeUser() {
        getViewModel().user.observe(this) {
            if (it == null) handleUserNotPresent()
            else handleUser(it)
        }
    }

    private fun handleUser(user: User) {
        binding.textViewName.text = user.name
        binding.textViewLastUpdateAt.text = getString(
            R.string.last_update_at,
            user.updatedAt.toDMYString(this),
            user.updatedAt.toHMString(this)
        )
        binding.buttonLogout.visibleOrGone(true)
    }

    private fun handleUserNotPresent() {
        binding.buttonLogout.visibleOrGone(false)
    }

    private fun configLogoutButton() {
        binding.buttonLogout.setOnClickListener {
            finish()
            logout()
        }
    }
}