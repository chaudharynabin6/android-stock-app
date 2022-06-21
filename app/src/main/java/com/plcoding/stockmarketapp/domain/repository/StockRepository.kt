package com.plcoding.stockmarketapp.domain.repository

import com.plcoding.stockmarketapp.domain.model.CompanyInfo
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.domain.model.IntradayInfo
import com.plcoding.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListing(
        fetchFromRemote : Boolean,
        query : String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getCompanyInfo(
        symbol : String
    ) : Resource<CompanyInfo>

    suspend fun getIntradayInfo(
        symbol: String
    ) : Resource<List<IntradayInfo>>

}