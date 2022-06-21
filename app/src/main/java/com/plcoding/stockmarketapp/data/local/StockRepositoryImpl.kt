package com.plcoding.stockmarketapp.data.local

import com.plcoding.stockmarketapp.data.csv.CsvParser
import com.plcoding.stockmarketapp.data.mapper.toCompanyInfo
import com.plcoding.stockmarketapp.data.mapper.toCompanyListing
import com.plcoding.stockmarketapp.data.mapper.toCompanyListingEntity
import com.plcoding.stockmarketapp.data.remote.StockApi
import com.plcoding.stockmarketapp.domain.model.CompanyInfo
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.domain.model.IntradayInfo
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val db: StockDatabase,
    private val stockApi: StockApi,
    private val companyListingCsvParser: CsvParser<CompanyListing>,
    private val intradayInfoParser: CsvParser<IntradayInfo>,
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<Resource<List<CompanyListing>>> {

        return flow {
//          initially  loading resource is emitted
            emit(
                Resource.Loading(isLoading = true)
            )

//            getting local resource from cache
            val localCompanyListingEntity = dao.searchCompanyListing(query)

//            converting to entity to companyListing
            val companyListing = localCompanyListingEntity.map {
                it.toCompanyListing()
            }
//            emitting local cache
            emit(
                Resource.Success(
                    companyListing
                )
            )
//            checking is db empty
            val isDbEmpty = localCompanyListingEntity.isEmpty() && query.isBlank()

//            checking should load from cache
            val isLoadFromCache = !isDbEmpty && !fetchFromRemote

//            load from cache
            if (isLoadFromCache) {

                emit(
                    Resource.Loading(isLoading = false)
                )
                return@flow
            }
//            load from cache is not true


            val remoteListing = try {
//   fetching data from remote
                val response = stockApi.getListing()
//                parsing data to List of companyListing
                val byteStream = response.byteStream()

                companyListingCsvParser.parse(byteStream)

            } catch (e: IOException) {
                e.printStackTrace()
                emit(
                    Resource.Error(message = "Cannot parse the data")
                )
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(
                    Resource.Error(message = "Cannot get data ")
                )
                return@flow
            }

            remoteListing.let { listings ->
//            this ensures the data is only coming from the db

//                first clearing the previous db
                dao.clearCompanyListingEntities()
//              inserting new entities to db
                dao.insertCompanyListings(
                    listings.map {
                        it.toCompanyListingEntity()
                    }
                )
//              getting data from the db and showing
                emit(
                    Resource.Success(
                        data = dao.searchCompanyListing("").map {
                            it.toCompanyListing()
                        }
                    )
                )

//               finally making loading false

                emit(
                    Resource.Loading(false)
                )

            }
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val response = stockApi.getCompanyInfo(symbol)
            val companyInfo = response.toCompanyInfo()
            Resource.Success(companyInfo)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "cannot load CompanyInfo"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "cannot load CompanyInfo"
            )
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = stockApi.getIntradayInfo(symbol)
            val byteStream = response.byteStream()
            val intradayInfoList = intradayInfoParser.parse(byteStream)
            Resource.Success(intradayInfoList)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "cannot load Intraday info"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "cannot load Intraday info"
            )
        }
    }
}