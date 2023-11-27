package br.com.atitude.finder.presentation.creator

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivityCreatorBinding
import br.com.atitude.finder.domain.PointTime
import br.com.atitude.finder.domain.PostalCodeAddressInfo
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.presentation._base.ToolbarActivity
import br.com.atitude.finder.presentation.map.PointMapResultContract
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class CreatorActivity : ToolbarActivity() {

    private lateinit var binding: ActivityCreatorBinding

    private val creatorViewModel: CreatorViewModel by viewModel()

    override fun getViewModel() = creatorViewModel

    private val pointMapResult =
        registerForActivityResult(PointMapResultContract()) { pointAddress ->
            if (pointAddress != null)
                creatorViewModel.setAddressCoordinates(pointAddress)
        }

    private fun initWeekDayInput() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            WeekDay.values().map { getString(it.localization) }
        )

        binding.autocompleteWeekDay.setAdapter(adapter)
        binding.autocompleteWeekDay.setOnItemClickListener { _, _, index, _ ->
            val weekDay = WeekDay.values()[index]
            creatorViewModel.setWeekDay(weekDay)
            binding.autocompleteWeekDay.clearFocus()
        }
    }

    private fun initToolbar() {
        configToolbar(binding.toolbar)
        setFinishOnBack()
    }

    private fun initTimePicker() {
        setPointTimeTextToCurrentTime()
        binding.textInputPointTime.setOnFocusChangeListener { _, b ->
            if (b) {
                val calendar = Calendar.getInstance()
                val hour = calendar[Calendar.HOUR_OF_DAY]
                val minute = calendar[Calendar.MINUTE]
                TimePickerDialog(
                    this,
                    { _, pickedHour, pickedMinute ->
                        binding.textInputPointTime.clearFocus()
                        val hourFormat = getString(R.string.hour_format, pickedHour, pickedMinute)
                        binding.textInputPointTime.setText(hourFormat)
                        binding.autocompleteWeekDay.requestFocus()
                    }, hour, minute, true
                ).show()
            }
        }
    }

    private fun configButtonCreateClickListener() {
        binding.buttonCreate.setOnClickListener {
            handleCreate()
        }
    }

    private fun getInputtedFullAddress(): String {
        val builder = StringBuilder()

        builder.append(binding.textInputStreet.editableText.toString())

        if (!binding.checkboxNumber.isChecked) {
            builder.append(", ")
            builder.append(binding.textInputNumber.editableText.toString())
        }

        if (!binding.checkboxComplement.isChecked) {
            builder.append(" - ")
            builder.append(binding.textInputComplement.editableText.toString())
        }

        builder.append(" - ")
        builder.append(binding.textInputCity.editableText.toString())

        return builder.toString()
    }

    private fun validateField(
        textInputLayout: TextInputLayout,
        validation: (String) -> Boolean
    ): String? {
        textInputLayout.error = null

        val editText = textInputLayout.editText ?: return null

        val editTextString = editText.text.toString()
        val message = "Campo inválido"

        if (validation.invoke(editTextString)) {
            textInputLayout.error = message
            return null
        }

        return editTextString
    }

    private fun validateAddressFields(): Boolean {
        val street = validateField(binding.textInputLayoutStreet) {
            return@validateField it.isBlank()
        }

        val neighborhood = validateField(binding.textInputLayoutNeighborhood) {
            return@validateField it.isBlank()
        }

        val city = validateField(binding.textInputLayoutCity) {
            return@validateField it.isBlank()
        }

        val state = validateField(binding.textInputLayoutState) {
            return@validateField it.isBlank()
        }

        val number = validateField(binding.textInputLayoutNumber) {
            return@validateField !binding.checkboxNumber.isChecked && it.isBlank()
        }

        val complement = validateField(binding.textInputLayoutComplement) {
            return@validateField !binding.checkboxComplement.isChecked && it.isBlank()
        }

        return listOf(street, neighborhood, city, state, number, complement).all { it != null }
    }

    private fun configConfirmLocationClickListener() {
        binding.textViewPointLocation.setOnClickListener {
            if (validateAddressFields()) {
                pointMapResult.launch(getInputtedFullAddress())
            }
        }
    }

    private fun initTextInputPostalCode() {
        binding.textInputPostalCode.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                fetchPostalCodeAddressData()
            }
        }

        binding.textInputPostalCode.addTextChangedListener { editableText ->
            editableText?.let {
                if (editableText.length >= 8) {
                    binding.textInputPostalCode.clearFocus()
                    fetchPostalCodeAddressData()
                }
            }
        }
    }

    private fun focusTextInputPointName() {
        binding.textInputPointName.requestFocus()
    }

    private fun configCheckboxNumberChangeListener() {
        binding.checkboxNumber.setOnCheckedChangeListener { _, checked ->
            binding.textInputLayoutNumber.isEnabled = !checked
        }
    }

    private fun configCheckboxComplementChangeListener() {
        binding.checkboxComplement.setOnCheckedChangeListener { _, checked ->
            binding.textInputLayoutComplement.isEnabled = !checked
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initWeekDayInput()
        initTimePicker()
        configButtonCreateClickListener()
        configConfirmLocationClickListener()
        configLeaderNameInputFocusListener()
        configLeaderPhoneFocusListener()
        configPointTagFocusListener()
        focusTextInputPointName()
        initTextInputPostalCode()
        configCheckboxNumberChangeListener()
        configCheckboxComplementChangeListener()
    }

    private fun fetchPostalCodeAddressData() {
        val textInputText = binding.textInputPostalCode.editableText.toString()

        clearPostalCodeInputError()

        if (textInputText.length != 8) {
            setPostalCodeInputWithInvalidPostalCodeError()
            return
        }

        creatorViewModel.fetchPostalCodeData(textInputText)
    }

    private fun fillFieldWithPostalCodeData(postalCodeAddressInfo: PostalCodeAddressInfo) {
        binding.textInputStreet.editableText.clear()
        binding.textInputStreet.isEnabled = true

        binding.textInputNeighborhood.editableText.clear()
        binding.textInputNeighborhood.isEnabled = true

        binding.textInputCity.editableText.clear()
        binding.textInputCity.isEnabled = true

        binding.textInputState.editableText.clear()
        binding.textInputState.isEnabled = true

        postalCodeAddressInfo.street?.let { street ->
            with(binding.textInputStreet) {
                editableText.insert(0, street)
                isEnabled = false
            }
        }

        postalCodeAddressInfo.neighborhood?.let { neighborhood ->
            with(binding.textInputNeighborhood) {
                editableText.insert(0, neighborhood)
                isEnabled = false
            }
        }

        postalCodeAddressInfo.city.let { city ->
            with(binding.textInputCity) {
                editableText.insert(0, city)
                isEnabled = false
            }
        }

        postalCodeAddressInfo.state.let { state ->
            with(binding.textInputState) {
                editableText.insert(0, state)
                isEnabled = false
            }
        }
    }

    private fun enableAllAddressFields() {
        with(binding) {
            listOf(
                textInputStreet,
                textInputNeighborhood,
                textInputCity,
                textInputState
            ).forEach {
                it.isEnabled = true
            }
        }
    }

    private fun initPostalCodeDataObserver() {
        creatorViewModel.postalCodeData.observe(this) { postalCodeAddressInfo ->
            if (postalCodeAddressInfo != null) {
                fillFieldWithPostalCodeData(postalCodeAddressInfo)
            } else {
                setPostalCodeInputWithInvalidPostalCodeError()
                enableAllAddressFields()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        configApiErrorHandler()
        initPostalCodeDataObserver()
    }

    private fun setPostalCodeInputWithInvalidPostalCodeError() {
        binding.textInputLayoutPostalCode.error = getString(R.string.invalid_postal_code)
    }

    private fun clearPostalCodeInputError() {
        binding.textInputLayoutPostalCode.error = null
    }

    private fun scrollToFirstInputWithError(): Boolean {
        val inputs = listOf(binding.textInputPointName)

        val view = inputs.firstOrNull { it.error != null }
        view?.let {
            binding.scrollView.smoothScrollTo(0, it.top)
        }
        return view != null
    }

    private fun getPointTime(): Pair<Int, Int> {
        val pointTime = binding.textInputPointTime.text.toString()
        val pointTimeTokens = pointTime.split(":")
        val pointHour = pointTimeTokens[0].toInt()
        val pointMinutes = pointTimeTokens[1].toInt()
        return pointHour to pointMinutes
    }

    private fun handleCreate() {
        val pointName = binding.textInputPointName.text.toString()
        val pointTag = binding.textInputPointTag.text.toString()
        val pointTimeTokens = getPointTime()
        val pointHour = pointTimeTokens.first
        val pointMinutes = pointTimeTokens.second
        val pointLeaderName = binding.textInputLeaderName.text.toString()
        val pointLeaderPhone = binding.textInputLeaderPhone.text.toString()
        val pointCoordinates = creatorViewModel.addressCoordinates.value ?: return
        val weekDay = creatorViewModel.weekDay.value ?: return
        val pointPostalCode = binding.textInputPostalCode.text.toString()
        val pointPostalStreet = binding.textInputStreet.text.toString()
        val pointPostalNeighborhood = binding.textInputNeighborhood.text.toString()
        val pointPostalState = binding.textInputState.text.toString()
        val pointPostalCity = binding.textInputCity.text.toString()

        if (scrollToFirstInputWithError().not()) {
            creatorViewModel.createPoint(
                name = pointName,
                street = pointPostalStreet,
                neighborhood = pointPostalNeighborhood,
                state = pointPostalState,
                city = pointPostalCity,
                complement = null,
                leaderName = pointLeaderName,
                leaderPhone = pointLeaderPhone,
                coordinates = pointCoordinates,
                postalCode = pointPostalCode,
                number = null,
                tag = pointTag,
                pointTime = PointTime(
                    hour = pointHour,
                    minutes = pointMinutes
                ),
                weekDay = weekDay,
            ) {
                Toast.makeText(this, "Célula criada com sucesso", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun configPointTagFocusListener() {
        binding.textInputPointTag.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.textInputLayoutPointTag.error = null
            }
        }
    }

    private fun configLeaderPhoneFocusListener() {
        binding.textInputLeaderPhone.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.textInputLayoutLeaderPhone.error = null

                var text =
                    binding.textInputLeaderPhone.editableText.toString().filter { it.isDigit() }



                if (text.length >= 2) {
                    binding.textInputLeaderPhone.editableText.clear()
                    binding.textInputLeaderPhone.editableText.append("(")
                    binding.textInputLeaderPhone.editableText.insert(1, text.substring(0..1))
                    binding.textInputLeaderPhone.editableText.append(")")
                    binding.textInputLeaderPhone.editableText.append(text.substring(2..<text.length))
                }

                text = binding.textInputLeaderPhone.editableText.toString()

                val regex = Regex("\\(\\d{2}\\)\\d{8,9}").matches(text)

                if (!regex) {
                    binding.textInputLayoutLeaderPhone.error = "Formato de telefone inválido"
                }
            }
        }
    }

    private fun configLeaderNameInputFocusListener() {
        binding.textInputLeaderName.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.textInputLayoutLeaderName.error = null

                if (binding.textInputLeaderName.editableText.isBlank()) {
                    binding.textInputLayoutLeaderName.error = "O nome não pode estar em branco"
                    return@setOnFocusChangeListener
                }

                if (binding.textInputLeaderName.editableText.firstOrNull()?.isUpperCase() == false) {
                    binding.textInputLayoutLeaderName.error =
                        "A primeira letra do nome deve ser maiuscula"
                    return@setOnFocusChangeListener
                }

                if (binding.textInputLeaderName.editableText.split(" ").size == 1) {
                    binding.textInputLayoutLeaderName.error = "O nome deve ser composto"
                    return@setOnFocusChangeListener
                }
            }
        }
    }

    private fun setPointTimeTextToCurrentTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val hourFormat = getString(R.string.hour_format, hour, minute)
        binding.textInputPointTime.setText(hourFormat)
    }
}