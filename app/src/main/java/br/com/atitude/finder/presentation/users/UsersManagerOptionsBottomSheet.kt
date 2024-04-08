package br.com.atitude.finder.presentation.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.FragmentUsersManagerOptionsBottomSheetBinding
import br.com.atitude.finder.domain.UserManagerItem
import br.com.atitude.finder.extensions.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class UsersManagerOptionsBottomSheet(
    private val userManagerItem: UserManagerItem,
    private val callback: Callback
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentUsersManagerOptionsBottomSheetBinding
    private val usersManagerOptionsViewModel: UsersManagerOptionsViewModel by activityViewModel()

    override fun onPause() {
        super.onPause()
        usersManagerOptionsViewModel.reset()
    }

    override fun onDestroy() {
        super.onDestroy()
        usersManagerOptionsViewModel.reset()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersManagerOptionsBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configState()
        observeEvent()
    }

    private fun observeEvent() {
        usersManagerOptionsViewModel.event.observe(this) {
            when (it) {
                UsersManagerOptionsViewModel.Event.DeletedUser -> handleDeletedUserEvent()
                UsersManagerOptionsViewModel.Event.EnabledOrDisabledUser -> handleEnabledOrDisableUserEvent()
                UsersManagerOptionsViewModel.Event.LoadingDeleteUser -> handleLoadingDeleteUserEvent()
                UsersManagerOptionsViewModel.Event.LoadingEnableOrDisableEvent -> handleLoadingEnableOrDisableEvent()
                null -> handleDefaultState()
            }
        }
    }

    private fun handleDefaultState() {
        binding.buttonEnableDisableUser.isEnabled = true
        binding.buttonDeleteUser.isEnabled = true
    }

    private fun handleDeletedUserEvent() {
        dismiss()
        callback.onDeletedUser()
    }

    private fun handleEnabledOrDisableUserEvent() {
        dismiss()
        callback.onEnabledOrDisabledUser()
    }

    private fun handleLoadingDeleteUserEvent() {
        binding.buttonEnableDisableUser.isEnabled = false
        binding.buttonDeleteUser.isEnabled = false
    }

    private fun handleLoadingEnableOrDisableEvent() {
        binding.buttonEnableDisableUser.isEnabled = false
        binding.buttonDeleteUser.isEnabled = false
    }

    private fun configState() {
        if (userManagerItem.accepted) {
            configStateWhenUserIsAccepted()
        } else {
            configStateWhenUserIsNotAccepted()
        }
    }

    private fun configStateWhenUserIsNotAccepted() {
        binding.textViewCannotDeleteWarning.visibleOrGone(false)
        binding.buttonDeleteUser.isEnabled = true
        with(binding.buttonEnableDisableUser) {
            setText(R.string.users_manager_enable_user)
            setOnClickListener {
                usersManagerOptionsViewModel.enableOrDisableUser(userManagerItem.id, true)
            }
        }

        with(binding.buttonDeleteUser) {
            setOnClickListener {
                usersManagerOptionsViewModel.deleteUser(userManagerItem.id)
            }
        }
    }

    private fun configStateWhenUserIsAccepted() {
        binding.textViewCannotDeleteWarning.visibleOrGone(true)
        binding.buttonDeleteUser.isEnabled = false
        with(binding.buttonEnableDisableUser) {
            setText(R.string.users_manager_disable_user)
            setOnClickListener {
                usersManagerOptionsViewModel.enableOrDisableUser(userManagerItem.id, false)
            }
        }
    }

    interface Callback {
        fun onDeletedUser()
        fun onEnabledOrDisabledUser()
    }

    companion object {
        fun FragmentActivity.openUsersManagerBottomSheet(
            userManagerItem: UserManagerItem,
            callback: Callback
        ): UsersManagerOptionsBottomSheet {
            return UsersManagerOptionsBottomSheet(
                userManagerItem,
                callback
            ).also {
                it.show(this.supportFragmentManager, TAG)
            }
        }

        private const val TAG = "UsersManagerBottomSheet"
    }
}