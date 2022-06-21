package com.plcoding.stockmarketapp.presentation.company_listing

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val repository: StockRepository,
) : ViewModel() {


    var state by mutableStateOf(
        CompanyListingState()
    )
    init {
        getCompanyListings()
    }
    var job: Job? = null
//    onEvent function is called from compose  or activity or fragment  for any event to execute on viewModel
    fun onEvent(event: CompanyListingEvent) {
        when (event) {
            is CompanyListingEvent.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingEvent.OnSearchQueryChange -> {
//                since this state is single source of truth
//                first we copy the event instant query to state
                state = state.copy(
                    query = event.query
                )
//                if previous job is running then it is cancelled
                job?.cancel()

//                launch job to fetch company listing
                job = viewModelScope.launch {
                    delay(500)
//                    only access the data from the state
                    getCompanyListings(query = state.query)
                }

            }
        }
    }

    private fun getCompanyListings(
        fetchFromRemote: Boolean = false,
        query: String = state.query.lowercase(),
    ) {


        viewModelScope.launch {
            repository.getCompanyListing(
                fetchFromRemote, query
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { companies ->
                            state = state.copy(
                                companies = companies
                            )
                        }
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        state = state.copy(
                            isRefreshing = result.isLoading
                        )
                    }
                }

            }
        }

    }

}