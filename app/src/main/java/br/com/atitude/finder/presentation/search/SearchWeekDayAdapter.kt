package br.com.atitude.finder.presentation.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.WeekDayListItemBinding
import br.com.atitude.finder.domain.WeekDay

class SearchWeekDayAdapter(private val context: Context) :
    RecyclerView.Adapter<SearchWeekDayAdapter.ViewHolder>() {

    private val weekDays = WeekDay.values()

    private var selectedWeekDays = emptyList<WeekDay>()

    fun toggleWeekDay(weekDay: WeekDay) {
        selectedWeekDays = if (selectedWeekDays.contains(weekDay).not()) {
            selectedWeekDays + listOf(weekDay)
        } else {
            selectedWeekDays.filter { it != weekDay }
        }
    }

    fun isWeekDaySelected(weekDay: WeekDay): Boolean = selectedWeekDays.contains(weekDay)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewWeekDay: AppCompatTextView =
            WeekDayListItemBinding.bind(itemView).textViewWeekDay

        fun bind(weekDay: WeekDay) {
            when (weekDay) {
                WeekDay.MONDAY -> textViewWeekDay.setText(R.string.monday)
                WeekDay.TUESDAY -> textViewWeekDay.setText(R.string.tuesday)
                WeekDay.WEDNESDAY -> textViewWeekDay.setText(R.string.wednesday)
                WeekDay.THURSDAY -> textViewWeekDay.setText(R.string.thursday)
                WeekDay.FRIDAY -> textViewWeekDay.setText(R.string.friday)
            }

            textViewWeekDay.setOnClickListener {
                toggleWeekDay(weekDay)
            }

            val uncheckedCheckBoxDrawable =
                AppCompatResources.getDrawable(context, R.drawable.round_check_box_outline_blank_24)
            val checkedCheckBoxDrawable =
                AppCompatResources.getDrawable(context, R.drawable.round_check_box_24)

            val checkBoxDrawable =
                if (isWeekDaySelected(weekDay)) checkedCheckBoxDrawable else uncheckedCheckBoxDrawable

            textViewWeekDay.setCompoundDrawablesWithIntrinsicBounds(
                checkBoxDrawable,
                null,
                null,
                null
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.week_day_list_item, parent, false)
        )
    }

    override fun getItemCount() = weekDays.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weekDays[position])
    }
}