package br.com.atitude.finder.presentation.searchlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.atitude.finder.R
import br.com.atitude.finder.domain.SimplePoint

class SearchListAdapter(private val context: Context, private val callback: SearchListAdapterCallback) :
    RecyclerView.Adapter<SearchListViewHolder>() {

    var points: List<SimplePoint> = emptyList()
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        return SearchListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.point_list_item, parent, false),
            callback
        )
    }

    override fun getItemCount() = points.size

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        points.getOrNull(position)?.let { simplePoint ->
            holder.bind(simplePoint)
        }
    }
}