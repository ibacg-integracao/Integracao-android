package br.com.atitude.finder.presentation.detail

import br.com.atitude.finder.domain.pointdetail.PointDetailContact

interface AdapterPointContactEvent {
    fun onCopy(pointDetailContact: PointDetailContact)
    fun onCall(pointDetailContact: PointDetailContact)
    fun onOpenWhatsApp(pointDetailContact: PointDetailContact)
}