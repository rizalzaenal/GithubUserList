package com.example.githubapp.ui.search_user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.githubapp.data.repository.UserRepository
import com.example.githubapp.data.util.convertException
import com.example.githubapp.data.util.formatDate
import com.example.githubapp.data.util.replaceNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userDetailFlow: MutableStateFlow<SnackBarUIState> = MutableStateFlow(
        SnackBarUIState()
    )
    val userDetailFlow: StateFlow<SnackBarUIState> = _userDetailFlow

    val searchPagingFlow =
        Pager(PagingConfig(pageSize = LOAD_PER_PAGE)) { userRepository.getPagingSource() }
            .flow
            .cachedIn(viewModelScope)

    fun getUserDetailData(userName: String) {
        viewModelScope.launch {
            try {
                val userDetail = userRepository.getUserDetails(userName)
                val data = SnackBarUIState(
                    userDetail.name.replaceNull(),
                    userDetail.email.replaceNull(),
                    formatDate(userDetail.createdAt.replaceNull()),
                    true
                )
                _userDetailFlow.update { data }
            } catch (e: Exception) {
                _userDetailFlow.update { SnackBarUIState(errorMessage = convertException(e).message ?: "Unknown Error", shouldShown = true) }
            }
            //Clear data after data emitted
            _userDetailFlow.update { SnackBarUIState() }
        }
    }

    companion object {
        const val LOAD_PER_PAGE = 10
    }
}