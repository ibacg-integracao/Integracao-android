package br.com.atitude.finder.presentation.searchlist

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivitySearchListBinding
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.extensions.visibleOrGone
import br.com.atitude.finder.presentation._base.EXTRA_INPUT
import br.com.atitude.finder.presentation._base.EXTRA_INPUT_TYPE
import br.com.atitude.finder.presentation._base.EXTRA_TAGS
import br.com.atitude.finder.presentation._base.EXTRA_TIMES
import br.com.atitude.finder.presentation._base.EXTRA_WEEK_DAYS
import br.com.atitude.finder.presentation._base.SearchType
import br.com.atitude.finder.presentation._base.ToolbarActivity
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
            }
        }
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

    inner class SearchListAdapterCallbackImpl: SearchListAdapterCallback {
        override fun onSelectDelete(id: String) {
            AlertDialog.Builder(this@SearchListActivity)
                .setTitle("Exclusão de Célula")
                .setMessage("Tem certeza que deseja excluir esta Célula?")
                .setPositiveButton("Excluir") { dialog, which ->
                    getViewModel().deletePoint(id) {
                        fetchPoints()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Não Excluir") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

        override fun onSelectEdit() {

        }

    }
}