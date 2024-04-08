package br.com.atitude.finder.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.Users
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.SharedPrefs

class UsersManagerViewModel(
    appRemoteConfig: AppRemoteConfig,
    sharedPreferences: SharedPrefs,
    private val apiRepository: ApiRepository
) : BaseViewModel(appRemoteConfig, sharedPreferences) {

    private val _users = MutableLiveData<Users>(emptyList())
    val users: LiveData<Users> = _users

    fun fetchUsers(loadingReason: String) {
        launch(loadingReason = loadingReason) {
            _users.postValue(apiRepository.getAllUsers())
        }
    }

    fun enableUser(loadingReason: String, userId: String) {
        launch(loadingReason = loadingReason) {
            apiRepository.enableUser(userId)
            _users.postValue(apiRepository.getAllUsers())
        }
    }

}