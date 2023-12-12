package br.com.atitude.finder.presentation.searchlist

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.PointListItemBinding
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.extensions.visibleOrGone

class SearchListViewHolder(private val itemView: View, private val callback: SearchListAdapterCallback) : RecyclerView.ViewHolder(itemView) {
    private val textViewPointName = PointListItemBinding.bind(itemView).textViewPointName
    private val textViewPointDistance = PointListItemBinding.bind(itemView).textViewDistance
    private val textViewPointTag = PointListItemBinding.bind(itemView).textViewTag
    private val textViewPointAddress = PointListItemBinding.bind(itemView).textViewAddress
    private val textViewPointWeekDayAndTime = PointListItemBinding.bind(itemView).textViewTime
    private val imageButtonOptions = PointListItemBinding.bind(itemView).imageButtonOptions

    fun bind(point: SimplePoint) {
        textViewPointName.text = point.name
        val distancePrecision = point.getPreciseDistance()

        if(distancePrecision != null) {
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

        imageButtonOptions.setOnClickListener {
            showOptionsDialog(point.id)
        }
    }

    private fun showOptionsDialog(id: String) {
        AlertDialog.Builder(itemView.context)
            .setItems(arrayOf("Editar", "Excluir")) { _, which ->
                when(which) {
                    0 -> callback.onSelectEdit()
                    1 -> callback.onSelectDelete(id)
                }
            }
            .show()
    }
}