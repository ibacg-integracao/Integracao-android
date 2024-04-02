package br.com.atitude.finder.presentation.creator

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivityCreatorBinding
import br.com.atitude.finder.domain.PointTime
import br.com.atitude.finder.domain.PostalCodeAddressInfo
import br.com.atitude.finder.presentation._base.ToolbarActivity
import br.com.atitude.finder.presentation.map.PointMapResultContract
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class CreatorActivity : ToolbarActivity() {

    private lateinit var binding: ActivityCreatorBinding

    private val creatorViewModel: CreatorViewModel by viewModel()

    override fun getViewModel() = creatorViewModel

    private lateinit var adapter: CreatorPointContactAdapter

    private val pointMapResult =
        registerForActivityResult(PointMapResultContract()) { pointAddress ->
            if (pointAddress != null)
                getViewModel().setAddressCoordinates(pointAddress)
        }

    private fun initPointContactRecyclerView() {
        adapter = CreatorPointContactAdapter(this)
        binding.rvPointContacts.adapter = adapter
        binding.rvPointContacts.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun initSectorInput() {
        getViewModel().sectors.observe(this) { sectors ->
            binding.autocompleteSector.isEnabled = sectors.isNotEmpty()
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                sectors.map { sector -> sector.name }
            )

            binding.autocompleteSector.setAdapter(adapter)
            binding.autocompleteSector.setOnItemClickListener { _, _, index, _ ->
                val sector = sectors[index]
                getViewModel().selectedSector = sector
                binding.autocompleteSector.clearFocus()
            }
        }
    }

    private fun initWeekDayInput() {

        getViewModel().weekDays.observe(this) { weekDays ->
            binding.autocompleteWeekDay.isEnabled = weekDays.isNotEmpty()
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                weekDays.map { weekDay -> getString(weekDay.localization) }
            )

            binding.autocompleteWeekDay.setAdapter(adapter)
            binding.autocompleteWeekDay.setOnItemClickListener { _, _, index, _ ->
                val weekDay = weekDays[index]
                getViewModel().setWeekDay(weekDay)
                binding.autocompleteWeekDay.clearFocus()
            }
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

    private fun validateReference(): Boolean {
        return validateField(binding.textInputLayoutReference) {
            return@validateField !binding.checkboxReference.isChecked && it.isBlank()
        } != null
    }

    private fun configConfirmLocationClickListener() {
        binding.textViewPointLocation.setOnClickListener {
            if (validateAddressFields()) {
                pointMapResult.launch(getInputtedFullAddress())
            }
        }
    }

    private fun initTextInputPostalCode() {
        binding.textInputPostalCode.addTextChangedListener { editableText ->
            editableText?.let {
                if (editableText.length == 8) {
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

    private fun configCheckboxReferenceChangeListener() {
        binding.checkboxReference.setOnCheckedChangeListener { _, checked ->
            binding.textInputLayoutReference.isEnabled = !checked
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initWeekDayInput()
        initSectorInput()
        initTimePicker()
        configButtonCreateClickListener()
        configConfirmLocationClickListener()
        configLeaderNameInputFocusListener()
        configPointTagFocusListener()
        focusTextInputPointName()
        initTextInputPostalCode()
        configCheckboxNumberChangeListener()
        configCheckboxComplementChangeListener()
        configCheckboxReferenceChangeListener()
        initPointContactRecyclerView()
        configAddContact()
        initObservers()
    }

    private fun initObservers() {
        with(getViewModel()) {
            pointContacts.observe(this@CreatorActivity) {
                adapter.items = it
            }
        }
    }

    private fun configAddContact() {
        binding.tvAddContact.setOnClickListener {
            openAddPointContactBottomSheet()
        }
    }

    private fun openAddPointContactBottomSheet() {
        ContactBottomSheet { pointContact ->
            getViewModel().addPointContact(pointContact)
        }.run {
            show(supportFragmentManager, ContactBottomSheet.TAG)
        }
    }

    private fun fetchPostalCodeAddressData() {
        val textInputText = binding.textInputPostalCode.editableText.toString()

        clearPostalCodeInputError()

        if (textInputText.length != 8) {
            setPostalCodeInputWithInvalidPostalCodeError()
            return
        }

        getViewModel().fetchPostalCodeData(
            loadingReason = getString(R.string.searching_postal_code_data),
            postalCode = textInputText
        )
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
        getViewModel().postalCodeData.observe(this) { postalCodeAddressInfo ->
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
        initPostalCodeDataObserver()
        initViewModel()
    }

    private fun initViewModel() {
        with(getViewModel()) {
            fetchWeekDays()
            fetchSectors()
        }
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
        val pointCoordinates = getViewModel().addressCoordinates.value

        if (pointCoordinates == null) {
            pointMapResult.launch(getInputtedFullAddress())
            return
        }

        val weekDay = getViewModel().weekDay.value ?: return
        val sector = getViewModel().selectedSector ?: return
        val pointPostalCode = binding.textInputPostalCode.text.toString()
        val pointPostalStreet = binding.textInputStreet.text.toString()
        val pointPostalNeighborhood = binding.textInputNeighborhood.text.toString()
        val pointPostalState = binding.textInputState.text.toString()
        val pointPostalCity = binding.textInputCity.text.toString()
        val phoneContacts = getViewModel().getSimplePointContacts()
        val pointNumber = binding.textInputNumber.text.toString().toIntOrNull()?.takeIf {
            binding.checkboxNumber.isChecked.not()
        }
        val pointComplement = binding.textInputComplement.text.toString().takeIf {
            binding.checkboxComplement.isChecked.not()
        }
        val pointReference = binding.textInputReference.text.toString().takeIf {
            binding.checkboxReference.isChecked.not()
        }

        if (phoneContacts.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Nenhum contato adicionado")
                .setMessage("Deseja adicionar um contato?")
                .setPositiveButton("Sim") { _, _ ->
                    openAddPointContactBottomSheet()
                }
                .setNegativeButton("Não") { _, _ ->
                    binding.scrollView.smoothScrollTo(0, binding.tvAddContact.top)
                }
                .show()
            return
        }


        if (scrollToFirstInputWithError().not() && validateReference()) {
            getViewModel().createPoint(
                name = pointName,
                street = pointPostalStreet,
                neighborhood = pointPostalNeighborhood,
                state = pointPostalState,
                city = pointPostalCity,
                complement = pointComplement,
                leaderName = pointLeaderName,
                coordinates = pointCoordinates,
                postalCode = pointPostalCode,
                number = pointNumber,
                tag = pointTag,
                pointTime = PointTime(
                    hour = pointHour,
                    minutes = pointMinutes
                ),
                weekDay = weekDay,
                sectorId = sector.id,
                pointContacts = getViewModel().getSimplePointContacts(),
                reference = pointReference
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

    private fun configLeaderNameInputFocusListener() {
        binding.textInputLeaderName.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.textInputLayoutLeaderName.error = null

                if (binding.textInputLeaderName.editableText.isBlank()) {
                    binding.textInputLayoutLeaderName.error = "O nome não pode estar em branco"
                    return@setOnFocusChangeListener
                }

                if (binding.textInputLeaderName.editableText.firstOrNull()
                        ?.isUpperCase() == false
                ) {
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