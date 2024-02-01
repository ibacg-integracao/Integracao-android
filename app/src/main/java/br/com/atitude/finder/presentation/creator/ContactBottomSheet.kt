package br.com.atitude.finder.presentation.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivityCreatorBinding
import br.com.atitude.finder.databinding.FragmentContactBottomSheetBinding
import br.com.atitude.finder.domain.Gender
import br.com.atitude.finder.domain.PointContact
import br.com.atitude.finder.presentation._base.Validators.validateName
import br.com.atitude.finder.presentation._base.Validators.validatePhone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ContactBottomSheet(private val callback: (PointContact) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentContactBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            if(submit()) dismiss()
        }
    }

    private fun getCheckedGender(): Gender? = when (binding.radioGender.checkedRadioButtonId) {
        R.id.rb_male -> Gender.MALE
        R.id.rb_female -> Gender.FEMALE
        else -> null
    }

    private fun submit(): Boolean {
        val name = binding.textInputLayoutName.validateName()
        val phone = binding.textInputLayoutPhone.validatePhone()
        val gender = getCheckedGender()

        if(name == null) return false
        if(phone == null) return false
        if(gender == null) return false

        callback.invoke(
            PointContact(
                name = name,
                contact = phone,
                gender = gender
            )
        )
        return true
    }

    companion object {
        const val TAG = "ContactBottomSheet"
    }
}