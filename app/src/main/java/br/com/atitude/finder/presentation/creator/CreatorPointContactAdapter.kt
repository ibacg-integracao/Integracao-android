package br.com.atitude.finder.presentation.creator

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.atitude.finder.R
import br.com.atitude.finder.domain.PointContact

class CreatorPointContactAdapter(private val context: Context) :
    RecyclerView.Adapter<CreatorPointContactViewHolder>() {

    var items: List<PointContact> = listOf()
        set(value) {
            notifyItemRangeChanged(0, value.size)
            field = value
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreatorPointContactViewHolder {
        return CreatorPointContactViewHolder(
            LayoutInflater.from(context).inflate(R.layout.point_contact_list_item, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CreatorPointContactViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            holder.bind(it)
        }
    }
}