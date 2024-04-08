package br.com.atitude.finder.presentation.users

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.UserManagerListItemBinding
import br.com.atitude.finder.domain.Users

class UsersManagerAdapter(
    private val context: Context,
    private val callback: UsersManagerListCallback
) : Adapter<UsersManagerViewHolder>() {

    var items: Users = emptyList()
        set(value) {
            notifyItemRangeChanged(0, value.size)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersManagerViewHolder {
        val view = UserManagerListItemBinding.inflate(LayoutInflater.from(context), parent, false).root
        return UsersManagerViewHolder(view, callback)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: UsersManagerViewHolder, position: Int) {
        items.getOrNull(position)?.let { holder.bind(it) }
    }
}