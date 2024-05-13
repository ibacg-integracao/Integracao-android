package br.com.atitude.finder.presentation.map

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.presentation._base.BaseViewModel
import com.google.android.gms.maps.model.LatLng

class PointMapViewModel(
    private val geocoder: Geocoder,
    remoteConfig: AppRemoteConfig
) : BaseViewModel(remoteConfig) {
    private val _lastSearchedAddress = MutableLiveData<Address?>()
    val lastSearchedAddress: LiveData<Address?> = _lastSearchedAddress

    private val _cameraTarget = MutableLiveData<LatLng?>(null)
    val cameraTarget: LiveData<LatLng?> = _cameraTarget

    private val _searching = MutableLiveData(false)
    val searching: LiveData<Boolean> = _searching

    fun searchAddress(address: String) {
        _searching.postValue(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(
                address, 1
            ) { addressList ->
                addressList.firstOrNull()?.let {
                    _lastSearchedAddress.postValue(it)
                    _cameraTarget.postValue(LatLng(it.latitude, it.longitude))
                    _searching.postValue(false)
                }
            }
        } else {
            _searching.postValue(false)
        }
    }
}