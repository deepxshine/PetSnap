package com.example.petsnap.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petsnap.domain.model.UserSearchResponse
import com.example.petsnap.domain.usecase.SearchUsersUseCase
import com.example.petsnap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<Resource<List<UserSearchResponse>>>()
    val searchResults: LiveData<Resource<List<UserSearchResponse>>> = _searchResults

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery.debounce(300) //Задержка для снижения частоты запросов
                .collect { query ->
                    if (query.isEmpty()) {
                        _searchResults.value = Resource.success(emptyList()) //Очищаем список результатов
                    } else {
                        performSearch(query)
                    }
                }
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    private fun performSearch(query: String) {
        _searchResults.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val users = searchUsersUseCase(query)
                if (users.isEmpty()) {
                    _searchResults.value = Resource.success(emptyList())
                } else {
                    _searchResults.value = Resource.success(users)
                }
            } catch (e: Exception) {
                _searchResults.value = Resource.error(e.localizedMessage ?: "An error occurred", null)
            }
        }
    }
}



