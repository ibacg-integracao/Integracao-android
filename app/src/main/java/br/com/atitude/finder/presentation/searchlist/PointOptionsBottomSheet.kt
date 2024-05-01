package br.com.atitude.finder.presentation.searchlist

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.FragmentPointOptionsBottomSheetBinding
import br.com.atitude.finder.domain.PointState
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.extensions.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

data class Configuration(val canDelete: Boolean)

class PointOptionsBottomSheet(
    private val point: SimplePoint,
    private val configuration: Configuration,
    private val callback: Callback
) : BottomSheetDialogFragment() {

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
        configSeeDetailButton()
    }

    private fun configSeeDetailButton() {
        binding.btnSeePointDetail.setOnClickListener {
            callback.onClickSeeDetails(point)
        }
    }

    private fun configStateRadioGroup() {
        binding.radioGroupStates.check(getRadioButtonIdByState(point.state))
        binding.radioGroupStates.setOnCheckedChangeListener { _, id ->
            val oldState: PointState = point.state
            val selectedState: PointState = getStateByRadioButtonId(id)
            binding.btnSave.isEnabled = oldState != selectedState

        }
    }

    private fun configDeleteButton() {
        binding.btnDelete.visibleOrGone(configuration.canDelete)
        binding.btnDelete.setOnClickListener {
            callback.onClickDeleteButton()
            context?.let { context ->
                val alertDialogBuilder = AlertDialog.Builder(context).create()
                alertDialogBuilder.setTitle(getString(R.string.point_exclusion_confirmation))
                alertDialogBuilder.setMessage(
                    getString(
                        R.string.point_exclusion_confirmation_message,
                        point.name
                    )
                )
                alertDialogBuilder.setButton(
                    DialogInterface.BUTTON_POSITIVE, getString(R.string.point_exclusion_action)
                ) { _, _ ->
                    callback.onDelete(point)
                    dismiss()
                }
                alertDialogBuilder.show()
            }
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
        fun onClickSeeDetails(point: SimplePoint)
        fun onSave(newState: SimplePoint)
        fun onDelete(point: SimplePoint)
        fun onClickDeleteButton()
    }

    companion object {
        const val TAG = "PointOptionsBottomSheet"
    }
}