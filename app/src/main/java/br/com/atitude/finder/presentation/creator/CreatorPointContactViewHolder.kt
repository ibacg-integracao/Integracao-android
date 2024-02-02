package br.com.atitude.finder.presentation.creator

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.PointContactListItemBinding
import br.com.atitude.finder.domain.PointContact
import br.com.atitude.finder.presentation._base.toPhoneFormat

class CreatorPointContactViewHolder(itemView: View) : ViewHolder(itemView) {

    private val name = PointContactListItemBinding.bind(itemView).tvContactName
    private val phone = PointContactListItemBinding.bind(itemView).tvContactPhone
    fun bind(it: PointContact) {
        val genderDisplayText = itemView.context.getString(it.gender.display)
        val nameGenderText = itemView.context.getString(
            R.string.contact_name_gender_display,
            it.name,
            genderDisplayText
        )
        name.text = nameGenderText
        phone.text = it.contact.toPhoneFormat()
    }
}