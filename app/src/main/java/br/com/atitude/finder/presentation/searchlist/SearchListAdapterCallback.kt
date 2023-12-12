package br.com.atitude.finder.presentation.searchlist

interface SearchListAdapterCallback {
    fun onSelectDelete(id: String)
    fun onSelectEdit()
}