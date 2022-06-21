package com.plcoding.stockmarketapp.data.csv

import android.util.Log
import com.opencsv.CSVReader
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class CompanyListingCsvParser @Inject constructor() : CsvParser<CompanyListing> {

    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        val csvReader = CSVReader(InputStreamReader(stream))

        return withContext(Dispatchers.IO) {
            val companyListings = csvReader.readAll()
                .drop(1)
                .mapNotNull {
                    val symbol = it.getOrNull(0)
                    val name = it.getOrNull(1)
                    val exchange = it.getOrNull(2)
                     CompanyListing(
                        symbol = symbol ?: return@mapNotNull null,
                        name = name ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )

                }.also {
                    csvReader.close()
                }

            companyListings
        }
    }


}