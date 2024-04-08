package br.com.atitude.finder.presentation.detail

import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.atitude.finder.databinding.ViewItemDetailContactBinding
import br.com.atitude.finder.domain.pointdetail.PointDetailContact
import br.com.atitude.finder.presentation._base.toPhoneFormat

class PointDetailContactViewHolder(view: View, private val event: AdapterPointContactEvent) :
    ViewHolder(view) {
    private val textViewName: AppCompatTextView =
        ViewItemDetailContactBinding.bind(view).tvContactName
    private val textViewPhone: AppCompatTextView =
        ViewItemDetailContactBinding.bind(view).tvContactPhone
    private val imageButtonCopy: AppCompatImageButton =
        ViewItemDetailContactBinding.bind(view).ibCopyNumber
    private val imageButtonCall: AppCompatImageButton =
        ViewItemDetailContactBinding.bind(view).ibCallNumber
    private val imageButtonWhatsApp: AppCompatImageButton =
        ViewItemDetailContactBinding.bind(view).ibWhatsapp

    fun bind(pointDetailContact: PointDetailContact) {
        textViewName.text = pointDetailContact.name
        textViewPhone.text = pointDetailContact.contactPhone.toPhoneFormat()
        imageButtonCopy.setOnClickListener {
            event.onCopy(pointDetailContact)
        }

        imageButtonCall.setOnClickListener {
            event.onCall(pointDetailContact)
        }

        imageButtonWhatsApp.setOnClickListener {
            event.onOpenWhatsApp(pointDetailContact)
        }
    }
}