package br.com.atitude.finder.presentation.searchlist

import br.com.atitude.finder.domain.SimplePoint

interface SearchListAdapterCallback {
    fun onClick(simplePoint: SimplePoint)
}