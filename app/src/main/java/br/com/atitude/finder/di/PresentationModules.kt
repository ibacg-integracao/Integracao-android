package br.com.atitude.finder.di

import br.com.atitude.finder.presentation.creator.CreatorViewModel
import br.com.atitude.finder.presentation.map.PointMapViewModel
import br.com.atitude.finder.presentation.search.SearchViewModel
import br.com.atitude.finder.presentation.searchlist.SearchListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModules = module {
    viewModel { SearchViewModel(get()) }
    viewModel { SearchListViewModel(get()) }
    viewModel { CreatorViewModel(get()) }
    viewModel { PointMapViewModel(get()) }
}