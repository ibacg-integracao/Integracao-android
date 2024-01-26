package br.com.atitude.finder.presentation.search

import android.os.Bundle
import androidx.core.os.bundleOf
import br.com.atitude.finder.databinding.ActivitySearchBinding
import br.com.atitude.finder.domain.SearchParams
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.presentation._base.CustomChip
import br.com.atitude.finder.presentation._base.SearchType
import br.com.atitude.finder.presentation._base.StringChip
import br.com.atitude.finder.presentation._base.ToolbarActivity
import br.com.atitude.finder.presentation._base.WeekDayChip
import br.com.atitude.finder.presentation._base.openCreator
import br.com.atitude.finder.presentation._base.openSearchList
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import org.koin.androidx.viewmodel.ext.android.getViewModel

const val VIEW_FLIPPER_NO_PARAMS = 0
const val VIEW_FLIPPER_WITH_PARAMS = 1

class SearchActivity : ToolbarActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun getViewModel() = getViewModel<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configToolbar(binding.toolbar)
        initObservers()
        initSearchButton()
        initCreateButton()
    }

    override fun onResume() {
        super.onResume()
        getViewModel().fetchSearchParams()
        binding.includeWithParams.buttonSearch.isEnabled = true
    }

    private fun clearChipGroup(chipGroup: ChipGroup) {
        chipGroup.removeAllViews()
    }

    private fun clearWeekDayChips() {
        clearChipGroup(binding.includeWithParams.chipGroupWeekDays)
    }

    private fun clearTagsChips() {
        clearChipGroup(binding.includeWithParams.chipGroupTags)
    }

    private fun clearTimesChips() {
        clearChipGroup(binding.includeWithParams.chipGroupTime)
    }

    private fun initObservers() {
        with(getViewModel()) {
            searchParams.observe(this@SearchActivity) { searchParams ->
                if (searchParams != null) {
                    handleWithSearchParams(searchParams)
                } else {
                    handleNoSearchParams()
                }
            }
        }
    }

    private fun handleWithSearchParams(searchParams: SearchParams) {
        binding.viewFlipperContent.displayedChild = VIEW_FLIPPER_WITH_PARAMS
        handleWeekDaysChange(searchParams)
        handleTagsChange(searchParams)
        handleTimeChanges(searchParams)
    }

    private fun handleNoSearchParams() {
        binding.viewFlipperContent.displayedChild = VIEW_FLIPPER_NO_PARAMS
        binding.includeNoParams.buttonRegister.setOnClickListener {
            openCreator()
        }
    }

    private fun handleTimeChanges(searchParams: SearchParams) {
        clearTimesChips()
        val times = searchParams.times.map {
            val chip = StringChip(this)
            chip.data = it
            return@map chip
        }

        times.forEach {
            binding.includeWithParams.chipGroupTime.addView(it)
        }
    }

    private fun handleTagsChange(searchParams: SearchParams) {
        clearTagsChips()
        val tags = searchParams.tags.map {
            val chip = StringChip(this)
            chip.data = it
            return@map chip
        }

        tags.forEach {
            binding.includeWithParams.chipGroupTags.addView(it)
        }
    }

    private fun <T : Chip> getSelectedChipGroupChips(chipGroup: ChipGroup): List<T> {
        return chipGroup.checkedChipIds.mapNotNull {
            return@mapNotNull chipGroup.findViewById<T>(it)
        }
    }

    private fun getSelectedTagsChips(): List<StringChip> =
        getSelectedChipGroupChips(binding.includeWithParams.chipGroupTags)

    private fun getSelectedWeekDayChips(): List<WeekDayChip> =
        getSelectedChipGroupChips(binding.includeWithParams.chipGroupWeekDays)

    private fun getSelectedTimesChips(): List<StringChip> =
        getSelectedChipGroupChips(binding.includeWithParams.chipGroupTime)

    private fun handleWeekDaysChange(searchParams: SearchParams) {
        clearWeekDayChips()
        val weekDayChips = searchParams.toWeekDays().map { weekDay ->
            val chip = WeekDayChip(this)
            chip.data = weekDay
            return@map chip
        }

        weekDayChips.forEach {
            binding.includeWithParams.chipGroupWeekDays.addView(it)
        }
    }

    private fun <T> getSelectedChipData(list: List<CustomChip<T>>): Set<T> =
        list.mapNotNull { it.data }.toSet()

    private fun getSelectedWeekDays(): Set<WeekDay> = getSelectedChipData(getSelectedWeekDayChips())

    private fun getSelectedTags(): Set<String> = getSelectedChipData(getSelectedTagsChips())

    private fun getSelectedTimes(): Set<String> = getSelectedChipData(getSelectedTimesChips())

    private fun initCreateButton() {
        binding.includeWithParams.btnCreate.setOnClickListener {
            this.openCreator()
        }
    }

    private fun getPostalCode(): String =
        binding.includeWithParams.textInputPostalCode.text?.toString().orEmpty()

    private fun handleSearchButton() {
        val postalCode = getPostalCode()
        getViewModel().trackSearch(
            postalCode = postalCode,
            weekDays = getSelectedWeekDays().toList(),
            categories = getSelectedTags().toList(),
            times = getSelectedTimes().toList()
        )

        binding.includeWithParams.textInputLayoutPostalCode.error = null

        if (postalCode.isNotEmpty() && postalCode.length != 8) {
            binding.includeWithParams.textInputLayoutPostalCode.error = "CEP inválido"
            return
        }

        binding.includeWithParams.buttonSearch.isEnabled = false
        this.openSearchList(
            input = postalCode.takeIf { it.isNotBlank() },
            type = postalCode.takeIf { it.isNotBlank() }?.let { SearchType.POSTAL_CODE },
            weekDays = getSelectedWeekDays(),
            tags = getSelectedTags(),
            times = getSelectedTimes()
        )
    }

    private fun initSearchButton() {
        binding.includeWithParams.buttonSearch.setOnClickListener {
            handleSearchButton()
        }
    }
}