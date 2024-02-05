package br.com.atitude.finder.presentation.searchlist

interface SearchListAdapterCallback {
    fun onSelect(id: String)
    fun onSelectDelete(id: String)
    fun onSelectEdit()
}