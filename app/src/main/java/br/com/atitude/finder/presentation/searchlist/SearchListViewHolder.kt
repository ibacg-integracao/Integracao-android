package br.com.atitude.finder.presentation.searchlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.PointListItemBinding
import br.com.atitude.finder.domain.SimplePoint

class SearchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textViewPointName = PointListItemBinding.bind(itemView).textViewPointName
    private val textViewPointDistance = PointListItemBinding.bind(itemView).textViewDistance
    private val textViewPointTag = PointListItemBinding.bind(itemView).textViewTag
    private val textViewPointAddress = PointListItemBinding.bind(itemView).textViewAddress
    private val textViewPointWeekDayAndTime = PointListItemBinding.bind(itemView).textViewTime

    fun bind(point: SimplePoint) {
        textViewPointName.text = point.name
        val distancePrecision = point.getPreciseDistance()

        textViewPointDistance.text = itemView.context.getString(
            R.string.point_distance,
            distancePrecision.distance,
            distancePrecision.unit.symbol
        )
        textViewPointAddress.text = point.address
        textViewPointTag.text = point.tag
        textViewPointWeekDayAndTime.text = itemView.context.getString(
            R.string.point_weekday_time,
            itemView.context.getString(point.weekDay.localization),
            point.hour,
            point.minute
        )
    }
}