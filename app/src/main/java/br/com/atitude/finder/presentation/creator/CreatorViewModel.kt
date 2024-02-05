package br.com.atitude.finder.presentation.creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.PointContact
import br.com.atitude.finder.domain.PointTime
import br.com.atitude.finder.domain.PostalCodeAddressInfo
import br.com.atitude.finder.domain.Sector
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import com.google.android.gms.maps.model.LatLng

class CreatorViewModel(private val apiRepository: ApiRepository, appRemoteConfig: AppRemoteConfig) :
    BaseViewModel(appRemoteConfig) {

    private val _sectors = MutableLiveData<List<Sector>>(emptyList())
    val sectors: LiveData<List<Sector>> = _sectors

    private val _weekDays = MutableLiveData<List<WeekDay>>(emptyList())
    val weekDays: LiveData<List<WeekDay>> = _weekDays

    private val _addressCoordinates = MutableLiveData<LatLng?>()
    val addressCoordinates: LiveData<LatLng?> = _addressCoordinates

    private val _weekDay = MutableLiveData<WeekDay?>()
    val weekDay: LiveData<WeekDay?> = _weekDay

    private val _postalCodeData = MutableLiveData<PostalCodeAddressInfo?>()
    val postalCodeData: LiveData<PostalCodeAddressInfo?> = _postalCodeData

    private val _pointContacts = MutableLiveData(emptyList<PointContact>())
    val pointContacts: LiveData<List<PointContact>> = _pointContacts

    fun addPointContact(pointContact: PointContact) {
        _pointContacts.postValue((pointContacts.value ?: emptyList()) + listOf(pointContact))
    }

    fun getSimplePointContacts(): List<PointContact> {
        return (pointContacts.value ?: emptyList()).map { it.toSimplePhone() }
    }

    var selectedSector: Sector? = null

    fun setAddressCoordinates(coordinates: LatLng) {
        _addressCoordinates.postValue(coordinates)
    }

    fun setWeekDay(weekDay: WeekDay) {
        _weekDay.postValue(weekDay)
    }

    fun fetchSectors() {
        launch {
            _sectors.postValue(apiRepository.getAllSectors())
        }
    }

    fun fetchWeekDays() {
        launch {
            val weekDays = apiRepository.getWeekDays()
            _weekDays.postValue(weekDays)
        }
    }

    fun fetchPostalCodeData(postalCode: String) {
        launch(
            loadingReason = "Buscando informações do CEP...",
            showAlertOnError = false,
            errorBlock = {
                _postalCodeData.postValue(null)
            }) {
            _postalCodeData.postValue(apiRepository.getPostalCodeAddress(postalCode))
        }
    }

    fun createPoint(
        name: String,
        street: String,
        leaderName: String,
        coordinates: LatLng,
        postalCode: String,
        number: Int?,
        tag: String,
        pointTime: PointTime,
        weekDay: WeekDay,
        neighborhood: String,
        state: String,
        city: String,
        complement: String?,
        sectorId: String,
        pointContacts: List<PointContact>,
        onFail: (() -> Unit)? = null,
        onSuccess: () -> Unit,
    ) {
        launch(errorBlock = {
            onFail?.invoke()
        }) {
            apiRepository.createPoint(
                name = name,
                street = street,
                leaderName = leaderName,
                latitude = coordinates.latitude,
                longitude = coordinates.longitude,
                number = number,
                postalCode = postalCode,
                tag = tag,
                hour = pointTime.hour,
                minutes = pointTime.minutes,
                weekDay = weekDay.response,
                neighborhood = neighborhood,
                complement = complement,
                state = state,
                city = city,
                sectorId = sectorId,
                phoneContacts = pointContacts
            )
            onSuccess.invoke()
        }
    }
}