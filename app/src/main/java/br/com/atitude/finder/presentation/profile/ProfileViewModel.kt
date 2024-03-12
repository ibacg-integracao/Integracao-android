package br.com.atitude.finder.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.User
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.SharedPrefs

class ProfileViewModel(
    appRemoteConfig: AppRemoteConfig,
    sharedPreferences: SharedPrefs,
    private val apiRepository: ApiRepository
) : BaseViewModel(appRemoteConfig, sharedPreferences) {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun fetchUser() {
        launch(loadingReason = "Buscando informações do usuário...") {
            val user = apiRepository.getAuthenticatedUser()
            _user.postValue(user)
        }
    }

}