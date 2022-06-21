package com.plcoding.stockmarketapp.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository,
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {

            state = state.copy(isLoading = true)
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
            val intradayInfoList = async { repository.getIntradayInfo(symbol) }


            when(val result = companyInfoResult.await()){

                is Resource.Success -> {
                   state =  state.copy(
                       company = result.data,
                       isLoading = false
                   )
                }
                is Resource.Error -> {
                    state =  state.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {

                }
            }

            when(val result = intradayInfoList.await()){

                is Resource.Success -> {
                    state = state.copy(
                        stockInfos = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    state =  state.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {}
            }

        }
    }


}