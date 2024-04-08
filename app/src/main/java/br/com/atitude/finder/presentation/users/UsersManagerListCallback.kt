package br.com.atitude.finder.presentation.users

import br.com.atitude.finder.domain.UserManagerItem

interface UsersManagerListCallback {
    fun onClick(userManagerItem: UserManagerItem)
    fun onClickAccept(userManagerItem: UserManagerItem)
}