package com.example.githubapp.ui.search_user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.githubapp.data.model.UserDetail
import com.example.githubapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _userDetailFlow: MutableStateFlow<UIState<UserDetail>?> = MutableStateFlow(null)
    val userDetailFlow: StateFlow<UIState<UserDetail>?> = _userDetailFlow

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

    fun getUserDetailData(userName: String) {
        viewModelScope.launch {
            try {
                val userDetail = userRepository.getUserDetails(userName)
                _userDetailFlow.value = UIState.Success(userDetail)
            } catch (e: Exception) {
                _userDetailFlow.value = UIState.Error(e)
            }
        }
    }

    fun setDataShown() {
        _userDetailFlow.value = null
    }

    companion object {
        const val KEY_QUERY = "QUERY"
        const val LOAD_PER_PAGE = 10
    }
}