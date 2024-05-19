package br.com.atitude.finder.presentation.searchlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.PointListItemBinding
import br.com.atitude.finder.domain.PointState
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.extensions.visibleOrGone


class SearchListViewHolder(
    itemView: View,
    private val callback: SearchListAdapterCallback
) : RecyclerView.ViewHolder(itemView) {
    private val textViewPointName = PointListItemBinding.bind(itemView).textViewPointName
    private val textViewPointDistance = PointListItemBinding.bind(itemView).textViewDistance
    private val textViewPointTag = PointListItemBinding.bind(itemView).textViewTag
    private val textViewPointAddress = PointListItemBinding.bind(itemView).textViewAddress
    private val textViewPointWeekDayAndTime = PointListItemBinding.bind(itemView).textViewTime
    private val textViewPointAddressReference =
        PointListItemBinding.bind(itemView).textViewReference
    private val groupPointInfo = PointListItemBinding.bind(itemView).groupInfo
    private val textViewPointState = PointListItemBinding.bind(itemView).textViewPointState

    fun bind(point: SimplePoint) {

        itemView.setOnClickListener {
            callback.onClick(point)
        }

        textViewPointName.text = point.name

        val distancePrecision = point.getPreciseDistance()

        if (distancePrecision != null) {
            textViewPointDistance.visibleOrGone(true)
            textViewPointDistance.text = itemView.context.getString(
                R.string.point_distance,
                distancePrecision.distance,
                distancePrecision.unit.symbol
            )
        } else {
            textViewPointDistance.visibleOrGone(false)
        }

        textViewPointAddress.text = point.address
        textViewPointTag.text = point.tag
        textViewPointWeekDayAndTime.text = itemView.context.getString(
            R.string.point_weekday_time,
            itemView.context.getString(point.weekDay.localization),
            point.hour,
            point.minute
        )

        textViewPointAddressReference.visibleOrGone(point.reference != null)
        point.reference?.let { reference ->
            textViewPointAddressReference.text = reference
        }

        itemView.setOnClickListener {
            showOptionsDialog(point)
        }

        point.state.takeIf { it != PointState.ACTIVE }.let { showPointInfoOrState(it) }
    }

    private fun showOptionsDialog(simplePoint: SimplePoint) {
        callback.onClick(simplePoint)
    }

    private fun showPointInfoOrState(state: PointState?) {
        if (state == null) {
            unFadePointName()
            groupPointInfo.visibleOrGone(true)
            textViewPointState.visibleOrGone(false)
        } else {
            fadePointName()
            groupPointInfo.visibleOrGone(false)
            with(textViewPointState) {
                visibleOrGone(true)
                text = context.getText(state.message)
            }
        }
    }

    private fun fadePointName() {
        textViewPointName.alpha = 0.5F
    }

    private fun unFadePointName() {
        textViewPointName.alpha = 1F
    }
}