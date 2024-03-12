package br.com.atitude.finder.di

import br.com.atitude.finder.presentation.creator.CreatorViewModel
import br.com.atitude.finder.presentation.search.SearchViewModel
import br.com.atitude.finder.presentation.searchlist.SearchListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModules = module {
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { SearchListViewModel(get(), get()) }
    viewModel { CreatorViewModel(get(), get()) }
}