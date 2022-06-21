package com.plcoding.stockmarketapp.data.remote

import com.plcoding.stockmarketapp.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {
    //    https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo
    @GET("query?function=LISTING_STATUS")
    suspend fun getListing(
        @Query(value = "apikey") apiKey: String = API_KEY,
    ): ResponseBody

    //    https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo
    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query(value = "symbol") symbol: String,
        @Query(value = "apikey") apiKey: String = API_KEY,
    ): CompanyInfoDto

    //https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=60min&apikey=demo&datatype=csv
    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query(value = "symbol") symbol: String,
        @Query(value = "apikey") apiKey: String = API_KEY,
    ) : ResponseBody

    companion object {
        const val API_KEY = "IV2QBPRWNXAAB0V0"
        const val BASE_URL = "https://www.alphavantage.co/"
    }
}