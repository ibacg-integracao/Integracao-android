package br.com.atitude.finder.presentation.searchlist

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivitySearchListBinding
import br.com.atitude.finder.domain.PointState
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.extensions.gone
import br.com.atitude.finder.extensions.hideKeyboard
import br.com.atitude.finder.extensions.visible
import br.com.atitude.finder.extensions.visibleOrGone
import br.com.atitude.finder.presentation._base.EXTRA_INPUT
import br.com.atitude.finder.presentation._base.EXTRA_TAGS
import br.com.atitude.finder.presentation._base.EXTRA_TIMES
import br.com.atitude.finder.presentation._base.EXTRA_WEEK_DAYS
import br.com.atitude.finder.presentation._base.ToolbarActivity
import br.com.atitude.finder.presentation._base.openPointDetail
import br.com.atitude.finder.presentation.searchlist.PointOptionsBottomSheet.Companion.openPointOptionsBottomSheet
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SearchListActivity : ToolbarActivity() {
    private val input: String by lazy { intent.getStringExtra(EXTRA_INPUT).orEmpty() }

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
        configPointNameSearchTextInput()
        setFinishOnBack()
        initObservers()
    }

    private fun configPointNameSearchTextInput() {
        binding.textInputPointName.setOnEditorActionListener { textView, i, keyEvent ->
            performSearchWithPointName()
            return@setOnEditorActionListener true
        }
        binding.textInputLayoutPointName.setEndIconOnClickListener {
            performSearchWithPointName()
        }
    }

    override fun onStart() {
        super.onStart()

        initSearchList()
        initObservers()
        initViewModel()
    }

    private fun initObservers() {

        getViewModel().flow.observe(this) {
            when (it) {
                is SearchListViewModel.Flow.Success -> handleSuccessState(it)
                is SearchListViewModel.Flow.NoPoints -> handleNoPoints()
                is SearchListViewModel.Flow.DeletedPoint -> handleDeletedPoint()
                is SearchListViewModel.Flow.UpdatedPoint -> handleUpdatedPoint()
                is SearchListViewModel.Flow.Error -> handleErrorState()
                is SearchListViewModel.Flow.AddressOrPostalCodeNotFound -> handleAddressOrPostalCodeNotFoundState()
                else -> Unit
            }

            if (it is SearchListViewModel.Flow.Loading) {
                hidePointNameInputAction()
                showLoader()
            } else {
                showPointNameInputAction()
                hideLoader()
            }
        }
    }

    private fun handleAddressOrPostalCodeNotFoundState() {
        showErrorState(
            drawableRes = R.drawable.no_results,
            title = R.string.search_no_address_or_postal_code_title,
            description = R.string.search_no_address_or_postal_code_description,
            action = R.string.search_no_address_or_postal_code_action
        ) {
            fetchPoints()
        }
    }

    private fun hideErrorState() {
        binding.viewErrorState.gone()
    }

    private fun showErrorState(
        @DrawableRes drawableRes: Int,
        @StringRes title: Int,
        @StringRes description: Int,
        @StringRes action: Int,
        onAction: () -> Unit
    ) {
        binding.viewErrorState.visible()
        binding.imageViewErrorState.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                drawableRes
            )
        )
        binding.textViewErrorTitle.setText(title)
        binding.textViewErrorDescription.setText(description)
        binding.buttonErrorAction.setText(action)
        binding.buttonErrorAction.setOnClickListener {
            onAction()
        }
    }

    private fun handleErrorState() {
        hideLoader()
        binding.recyclerViewPoints.visibleOrGone(false)
        showErrorState(
            drawableRes = R.drawable.error,
            title = R.string.search_error_title,
            description = R.string.search_error_description,
            action = R.string.search_no_results_action
        ) {
            fetchPoints()
        }
    }

    private fun handleUpdatedPoint() {
        fetchPoints()
    }

    private fun handleDeletedPoint() {
        fetchPoints()
    }

    private fun hideLoader() {
        binding.viewLoading.visibleOrGone(false)
    }

    private fun showLoader() {
        binding.viewLoading.visibleOrGone(true)
    }

    private fun handleNoPoints() {
        hideLoader()
        binding.recyclerViewPoints.visibleOrGone(false)
        showErrorState(
            drawableRes = R.drawable.no_results,
            title = R.string.search_no_results_title,
            description = R.string.search_no_results_description,
            action = R.string.search_no_results_action
        ) {
            finish()
        }
    }

    private fun hidePointNameInputAction() {
        binding.textInputLayoutPointName.isEndIconVisible = false
    }

    private fun showPointNameInputAction() {
        binding.textInputLayoutPointName.isEndIconVisible = true
    }

    private fun handleSuccessState(flow: SearchListViewModel.Flow.Success) {
        hideErrorState()
        hideLoader()
        binding.recyclerViewPoints.visibleOrGone(true)
        adapter.points = flow.points
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

    private fun getPointNameFromInput() = binding.textInputPointName.text?.toString().orEmpty()

    private fun performSearchWithPointName() {
        val pointName = getPointNameFromInput()
        hideKeyboard()
        getViewModel().trackSearchPointName(pointName)
        getViewModel().searchByAddressOrPostalCode(
            input = input,
            pointName = pointName,
            weekDays = weekDays.map { it.response },
            tags = tags,
            times = times
        )
    }

    private fun fetchPoints() {
        getViewModel().searchByAddressOrPostalCode(
            input = input,
            weekDays = weekDays.map { it.response },
            tags = tags,
            times = times,
            pointName = null
        )
    }

    fun openPointOptionsModal(simplePoint: SimplePoint) {
        pointOptionsBottomSheet = openPointOptionsBottomSheet(
            simplePoint,
            Configuration(canDelete = getViewModel().canDeletePoint()),
            PointOptionsCallbackImpl()
        )
    }

    fun closePointOptionsModal() {
        pointOptionsBottomSheet?.let {
            it.dismiss()
            pointOptionsBottomSheet = null
        }
    }

    inner class SearchListAdapterCallbackImpl : SearchListAdapterCallback {
        override fun onClick(simplePoint: SimplePoint) {
            openPointOptionsModal(simplePoint)
        }
    }

    inner class PointOptionsCallbackImpl : PointOptionsBottomSheet.Callback {
        override fun onClickSeeDetails(point: SimplePoint) {
            openPointDetail(point.id)
            getViewModel().trackSeeDetails(point)
        }

        override fun onSave(newState: SimplePoint) {
            val text = getString(newState.state.message)
            when (newState.state) {
                PointState.INACTIVE -> getViewModel().setPointInactive(
                    getString(R.string.point_change_state, newState.name, text),
                    newState
                )

                PointState.SUSPENDED -> getViewModel().setPointSuspended(
                    getString(R.string.point_change_state, newState.name, text),
                    newState
                )

                PointState.ACTIVE -> getViewModel().setPointActive(
                    getString(R.string.point_change_state, newState.name, text),
                    newState
                )

                else -> throw IllegalStateException("Unknown point state.")
            }
            closePointOptionsModal()
            getViewModel().trackSaveState(newState)
        }

        override fun onDelete(point: SimplePoint) {
            getViewModel().deletePoint(
                loadingReason = getString(R.string.deleting_point),
                id = point.id
            )
            getViewModel().trackSuccessDeletePoint(point)
        }

        override fun onClickDeleteButton() {
            getViewModel().trackClickDeletePointButton()
        }
    }
}