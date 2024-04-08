package br.com.atitude.finder.presentation.searchlist

import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivitySearchListBinding
import br.com.atitude.finder.domain.PointState
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.extensions.visibleOrGone
import br.com.atitude.finder.presentation._base.EXTRA_INPUT
import br.com.atitude.finder.presentation._base.EXTRA_INPUT_TYPE
import br.com.atitude.finder.presentation._base.EXTRA_TAGS
import br.com.atitude.finder.presentation._base.EXTRA_TIMES
import br.com.atitude.finder.presentation._base.EXTRA_WEEK_DAYS
import br.com.atitude.finder.presentation._base.SearchType
import br.com.atitude.finder.presentation._base.ToolbarActivity
import br.com.atitude.finder.presentation._base.openPointDetail
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SearchListActivity : ToolbarActivity() {
    private val input: String? by lazy { intent.getStringExtra(EXTRA_INPUT) }
    private val inputType: SearchType? by lazy {
        intent.getStringExtra(EXTRA_INPUT_TYPE)?.let { SearchType.findByType(it) }
    }
    private val weekDays: List<WeekDay> by lazy {
        intent.getStringArrayExtra(EXTRA_WEEK_DAYS)?.mapNotNull { WeekDay.getByResponse(it) }
            ?: emptyList()
    }

    private val tags: List<String> by lazy {
        intent.getStringArrayExtra(EXTRA_TAGS)?.toList() ?: emptyList()
    }

    private val times: List<String> by lazy {
        intent.getStringArrayExtra(EXTRA_TIMES)?.toList() ?: emptyList()
    }

    private lateinit var binding: ActivitySearchListBinding

    private val adapter = SearchListAdapter(this, SearchListAdapterCallbackImpl())

    private var pointOptionsBottomSheet: PointOptionsBottomSheet? = null

    override fun getViewModel() = getViewModel<SearchListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configToolbar(binding.toolbar)
        setFinishOnBack()
        initObservers()
        initSearchParamsView()
    }

    private fun initSearchParamsView() {

        binding.viewSearchParams.visibleOrGone(getViewModel().isSearchParamsViewEnabled())

        binding.viewSearchParams.setOnClickListener {
            getViewModel().toggleExpandSearchParams()
        }
    }

    private fun initSearchParamsObservers() {
        getViewModel().expandedSearchParams.observe(this) { expanded ->
            val arrowDown =
                AppCompatResources.getDrawable(this, R.drawable.round_arrow_drop_down_24)
            val arrowUp = AppCompatResources.getDrawable(this, R.drawable.round_arrow_drop_up_24)

            binding.ibArrow.setImageDrawable(
                if (expanded) arrowUp else arrowDown
            )
            val stringBuilder = StringBuilder()
            val inputType = inputType

            if (inputType != null) {
                val inputTypeFormatted = when (inputType) {
                    SearchType.POSTAL_CODE -> "CEP"
                    SearchType.ADDRESS -> "Endereço"
                }

                stringBuilder.append("$inputTypeFormatted: $input")
            }

            if (expanded) {
                if (weekDays.isNotEmpty()) {
                    stringBuilder.appendLine()
                    val weekDaysFormatted =
                        weekDays.joinToString(", ") { getString(it.localization) }
                    stringBuilder.append("Dias: $weekDaysFormatted")
                }
            }

            if(stringBuilder.isBlank()) {
                binding.textViewInput.visibleOrGone(false)
            } else {
                binding.textViewInput.text = stringBuilder.toString()
            }

        }
    }

    override fun onStart() {
        super.onStart()

        when (inputType) {
            SearchType.POSTAL_CODE -> binding.textViewInput.text =
                getString(R.string.search_input, getString(R.string.postal_code), input)

            else -> Unit
        }

        initSearchList()
        initObservers()
        initViewModel()
    }

    private fun initObservers() {
        initSearchParamsObservers()

        getViewModel().flow.observe(this) {
            when (it) {
                is SearchListViewModel.Flow.Success -> handleSuccessState(it)
                is SearchListViewModel.Flow.SearchingPoints -> handleSearchingPoints()
                is SearchListViewModel.Flow.NoPoints -> handleNoPoints()
                is SearchListViewModel.Flow.DeletedPoint -> handleDeletedPoint()
                is SearchListViewModel.Flow.UpdatedPoint -> handleUpdatedPoint()
            }
        }
    }

    private fun handleUpdatedPoint() {
        fetchPoints()
    }

    private fun handleDeletedPoint() {
        fetchPoints()
    }

    private fun handleNoPoints() {
        with(binding.textViewPlaceholder) {
            visibleOrGone(true)
            text = getString(R.string.no_points)
        }
        
        with(binding.recyclerViewPoints) {
            visibleOrGone(false)
        }
    }

    private fun handleSearchingPoints() {
        with(binding.textViewPlaceholder) {
            visibleOrGone(true)
            text = getString(R.string.searching)
        }
    }

    private fun handleSuccessState(flow: SearchListViewModel.Flow.Success) {
        adapter.points = flow.points
        binding.textViewPlaceholder.visibleOrGone(false)
    }

    private fun initSearchList() {
        binding.recyclerViewPoints.adapter = adapter
        binding.recyclerViewPoints.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initViewModel() {
        fetchPoints()
    }

    private fun fetchPoints() {
        input?.let { input ->
            getViewModel().search(
                postalCode = input,
                weekDays = weekDays.map { it.response },
                tags = tags,
                times = times
            )
        } ?: run {
            getViewModel().fetchAllPoints()
        }
    }

    fun openPointOptionsModal(simplePoint: SimplePoint) {
        pointOptionsBottomSheet = PointOptionsBottomSheet(
            simplePoint,
            PointOptionsCallbackImpl()
        ).also {
            it.show(supportFragmentManager, PointOptionsBottomSheet.TAG)
        }
    }

    fun closePointOptionsModal() {
        pointOptionsBottomSheet?.let {
            it.dismiss()
            pointOptionsBottomSheet = null
        }
    }

    inner class SearchListAdapterCallbackImpl: SearchListAdapterCallback {
        override fun onClick(simplePoint: SimplePoint) {
            openPointOptionsModal(simplePoint)
        }
    }

    inner class PointOptionsCallbackImpl : PointOptionsBottomSheet.Callback {
        override fun onClickSeeDetails(point: SimplePoint) {
            openPointDetail(point.id)
        }

        override fun onSave(newState: SimplePoint) {
            val text = getString(newState.state.message)
            when (newState.state) {
                PointState.INACTIVE -> getViewModel().setPointInactive(
                    "Tornando ${newState.name} como '${text}'",
                    newState
                )

                PointState.SUSPENDED -> getViewModel().setPointSuspended(
                    "Tornando ${newState.name} como '${text}'",
                    newState
                )

                PointState.ACTIVE -> getViewModel().setPointActive(
                    "Tornando ${newState.name} como '${text}'",
                    newState
                )

                else -> throw IllegalStateException("Unknown point state.")
            }
            closePointOptionsModal()
        }

        override fun onDelete(pointId: String) {
            getViewModel().deletePoint(pointId)
        }

    }
}