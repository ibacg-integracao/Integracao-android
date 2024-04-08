package br.com.atitude.finder.presentation.searchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.FragmentPointOptionsBottomSheetBinding
import br.com.atitude.finder.domain.PointState
import br.com.atitude.finder.domain.SimplePoint
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PointOptionsBottomSheet(private val point: SimplePoint, private val callback: Callback) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPointOptionsBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPointOptionsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configStateRadioGroup()
        configSaveButton()
        configDeleteButton()
    }

    private fun configStateRadioGroup() {
        binding.radioGroupStates.check(getRadioButtonIdByState(point.state))
        binding.radioGroupStates.setOnCheckedChangeListener { radioGroup, id ->
            val oldState: PointState = point.state
            val selectedState: PointState = getStateByRadioButtonId(id)
            binding.btnSave.isEnabled = oldState != selectedState

        }
    }

    private fun configDeleteButton() {
        binding.btnDelete.setOnClickListener {
            callback.onDelete(point.id)
            dismiss()
        }
    }

    private fun configSaveButton() {
        binding.btnSave.setOnClickListener {
            val checkedOption = binding.radioGroupStates.checkedRadioButtonId
            callback.onSave(point.copy(state = getStateByRadioButtonId(checkedOption)))
        }
    }

    private fun stateRadioButtonIdMap() = mapOf(
        R.id.radio_button_active to PointState.ACTIVE,
        R.id.radio_button_inactive to PointState.INACTIVE,
        R.id.radio_button_suspended to PointState.SUSPENDED,
    )

    private fun getStateByRadioButtonId(@IdRes id: Int) =
        stateRadioButtonIdMap()[id] ?: throw IllegalStateException("unknown state radio button id")

    private fun radioButtonIdToStateMap() = mapOf(
        PointState.ACTIVE to R.id.radio_button_active,
        PointState.INACTIVE to R.id.radio_button_inactive,
        PointState.SUSPENDED to R.id.radio_button_suspended,
    )

    private fun getRadioButtonIdByState(state: PointState) =
        radioButtonIdToStateMap()[state] ?: throw IllegalStateException("unknown state")

    interface Callback {
        fun onSave(newState: SimplePoint)
        fun onDelete(pointId: String)
    }

    companion object {
        fun FragmentActivity.openPointOptionsBottomSheet(simplePoint: SimplePoint, callback: Callback): PointOptionsBottomSheet {
            return PointOptionsBottomSheet(
                simplePoint,
                callback
            ).also {
                it.show(this.supportFragmentManager, TAG)
            }
        }
        const val TAG = "PointOptionsBottomSheet"
    }
}