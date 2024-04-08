package br.com.atitude.finder.presentation.users

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.UserManagerListItemBinding
import br.com.atitude.finder.domain.UserManagerItem
import br.com.atitude.finder.extensions.visibleOrGone

class UsersManagerViewHolder(itemView: View, private val callback: UsersManagerListCallback) :
    ViewHolder(itemView) {

    private val textViewName = UserManagerListItemBinding.bind(itemView).textViewName
    private val textViewAcceptStatus =
        UserManagerListItemBinding.bind(itemView).textViewAcceptedStatus
    private val buttonAccept = UserManagerListItemBinding.bind(itemView).buttonAccept

    fun bind(userManagerItem: UserManagerItem) {
        textViewName.text = userManagerItem.name

        if (userManagerItem.accepted) {
            configWhenAcceptedState()
        } else {
            configWhenNotAcceptedState(userManagerItem)
        }

        itemView.setOnClickListener {
            callback.onClick(userManagerItem)
        }
    }

    private fun configWhenAcceptedState() {
        textViewAcceptStatus.setText(R.string.users_manager_accepted)
        textViewAcceptStatus.setTextColor(itemView.resources.getColor(R.color.green, null))
        buttonAccept.visibleOrGone(false)
    }

    private fun configWhenNotAcceptedState(userManagerItem: UserManagerItem) {
        textViewAcceptStatus.setText(R.string.users_manager_not_accepted)
        textViewAcceptStatus.setTextColor(itemView.resources.getColor(R.color.red, null))
        buttonAccept.visibleOrGone(true)
        buttonAccept.setOnClickListener {
            callback.onClickAccept(userManagerItem)
        }
    }
}