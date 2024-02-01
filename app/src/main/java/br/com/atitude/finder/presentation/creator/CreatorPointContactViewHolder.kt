package br.com.atitude.finder.presentation.creator

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.atitude.finder.databinding.PointContactListItemBinding
import br.com.atitude.finder.domain.PointContact

class CreatorPointContactViewHolder(itemView: View): ViewHolder(itemView) {

    private val name = PointContactListItemBinding.bind(itemView).tvContactName
    private val phone = PointContactListItemBinding.bind(itemView).tvContactPhone
    fun bind(it: PointContact) {
        name.text = "${it.name} (${it.gender.name})"
        phone.text = it.contact
    }
}