package br.com.atitude.finder.di

import br.com.atitude.finder.presentation.authentication.AuthenticatorViewModel
import br.com.atitude.finder.presentation.authentication.RegisterAccountViewModel
import br.com.atitude.finder.presentation.creator.CreatorViewModel
import br.com.atitude.finder.presentation.detail.DetailViewModel
import br.com.atitude.finder.presentation.profile.ProfileViewModel
import br.com.atitude.finder.presentation.search.SearchViewModel
import br.com.atitude.finder.presentation.searchlist.SearchListViewModel
import br.com.atitude.finder.presentation.users.UsersManagerOptionsViewModel
import br.com.atitude.finder.presentation.users.UsersManagerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModules = module {
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { SearchListViewModel(get(), get(), get(), get()) }
    viewModel { CreatorViewModel(get(), get(), get()) }
    viewModel { DetailViewModel(get(), get(), get()) }
    viewModel { AuthenticatorViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { RegisterAccountViewModel(get(), get(), get()) }
    viewModel { UsersManagerViewModel(get(), get(), get()) }
    viewModel { UsersManagerOptionsViewModel(get(), get(), get()) }
}