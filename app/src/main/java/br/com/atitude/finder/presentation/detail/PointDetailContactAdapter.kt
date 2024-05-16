package br.com.atitude.finder.presentation.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.atitude.finder.R
import br.com.atitude.finder.domain.pointdetail.PointDetailContact

class PointDetailContactAdapter(private val context: Context, private val event: AdapterPointContactEvent) :
    RecyclerView.Adapter<PointDetailContactViewHolder>() {
    var pointDetailContact: List<PointDetailContact> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PointDetailContactViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.view_item_detail_contact, parent, false)
        return PointDetailContactViewHolder(view, event)
    }

    override fun getItemCount() = pointDetailContact.size

    override fun onBindViewHolder(holder: PointDetailContactViewHolder, position: Int) {
        pointDetailContact.getOrNull(position)?.let { item ->
            holder.bind(item)
        }
    }

}