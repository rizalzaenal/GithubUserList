package com.example.githubapp.ui.search_user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.githubapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val searchPagingFlow = savedStateHandle.getLiveData<String>(KEY_QUERY)
        .asFlow()
        .flatMapLatest {
            Pager(PagingConfig(pageSize = LOAD_PER_PAGE)) {
                userRepository.getPagingSource(it)
            }.flow
        }
        .cachedIn(viewModelScope)

    fun searchNewQuery(query: String) {
        savedStateHandle.set(KEY_QUERY, query)
    }


    companion object {
        const val KEY_QUERY = "QUERY"
        const val LOAD_PER_PAGE = 10
    }
}