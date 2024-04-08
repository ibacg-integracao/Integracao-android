package br.com.atitude.finder.presentation.users

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.atitude.finder.databinding.ActivityUsersManagerBinding
import br.com.atitude.finder.domain.UserManagerItem
import br.com.atitude.finder.presentation._base.BaseActivity
import br.com.atitude.finder.presentation.users.UsersManagerOptionsBottomSheet.Companion.openUsersManagerBottomSheet
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersManagerActivity : BaseActivity() {

    private lateinit var binding: ActivityUsersManagerBinding

    private val usersManagerViewModel: UsersManagerViewModel by viewModel()
    override fun getViewModel() = usersManagerViewModel

    private val adapter = UsersManagerAdapter(this, UsersManagerListCallbackImpl())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configAdapter()
        observeUsers()
        updateUsers()
    }

    private fun observeUsers() {
        getViewModel().users.observe(this) {
            adapter.items = it
        }
    }

    private fun configAdapter() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@UsersManagerActivity, LinearLayoutManager.VERTICAL, false)
    }

    private fun openOptionsDialog(userManagerItem: UserManagerItem) {
        openUsersManagerBottomSheet(userManagerItem, UsersManagerOptionsCallbackImpl())
    }

    private fun updateUsers() {
        getViewModel().fetchUsers("Buscando usuários...")
    }

    private fun enableUser(userManagerItem: UserManagerItem) {
        getViewModel().enableUser("Autorizando usuário...", userManagerItem.id)
    }

    inner class UsersManagerOptionsCallbackImpl : UsersManagerOptionsBottomSheet.Callback {
        override fun onDeletedUser() {
            updateUsers()
        }

        override fun onEnabledOrDisabledUser() {
            updateUsers()
        }

    }

    inner class UsersManagerListCallbackImpl : UsersManagerListCallback {
        override fun onClick(userManagerItem: UserManagerItem) {
            openOptionsDialog(userManagerItem)
        }

        override fun onClickAccept(userManagerItem: UserManagerItem) {
            enableUser(userManagerItem)
        }
    }
}