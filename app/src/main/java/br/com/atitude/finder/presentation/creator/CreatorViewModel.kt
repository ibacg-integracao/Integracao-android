package br.com.atitude.finder.presentation.creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.domain.PointTime
import br.com.atitude.finder.domain.PostalCodeAddressInfo
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import com.google.android.gms.maps.model.LatLng

class CreatorViewModel(private val apiRepository: ApiRepository) : BaseViewModel() {

    private val _addressCoordinates = MutableLiveData<LatLng?>()
    val addressCoordinates: LiveData<LatLng?> = _addressCoordinates

    private val _weekDay = MutableLiveData<WeekDay?>()
    val weekDay: LiveData<WeekDay?> = _weekDay

    private val _postalCodeData = MutableLiveData<PostalCodeAddressInfo?>()
    val postalCodeData: LiveData<PostalCodeAddressInfo?> = _postalCodeData

    fun setAddressCoordinates(coordinates: LatLng) {
        _addressCoordinates.postValue(coordinates)
    }

    fun setWeekDay(weekDay: WeekDay) {
        _weekDay.postValue(weekDay)
    }

    fun fetchPostalCodeData(postalCode: String) {
        launch(showAlertOnError = false, errorBlock = {
            _postalCodeData.postValue(null)
        }) {
            _postalCodeData.postValue(apiRepository.getPostalCodeAddress(postalCode))
        }
    }

    fun createPoint(
        name: String,
        street: String,
        leaderName: String,
        leaderPhone: String,
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
                leaderPhone = leaderPhone,
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
                city = city
            )
            onSuccess.invoke()
        }
    }
}