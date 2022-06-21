package com.plcoding.stockmarketapp.data.csv

import com.opencsv.CSVReader
import com.plcoding.stockmarketapp.data.mapper.toIntradayInfo
import com.plcoding.stockmarketapp.data.remote.dto.IntradayInfoDto
import com.plcoding.stockmarketapp.domain.model.IntradayInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject

class IntradayInfoParser  @Inject constructor() : CsvParser<IntradayInfo> {
    override suspend fun parse(stream: InputStream): List<IntradayInfo> {

        val csvReader = CSVReader(InputStreamReader(stream))

        return withContext(Dispatchers.IO) {
            csvReader.readAll().drop(1).mapNotNull { line ->
                val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                val close = line.getOrNull(4) ?: return@mapNotNull null

                val intradayInfoDto = IntradayInfoDto(
                    timestamp = timestamp,
                    close = close
                )
                intradayInfoDto.toIntradayInfo()

            }.filter { intradayInfo ->
//                filtering yesterday only
                intradayInfo.date.dayOfMonth == LocalDateTime.now().minusDays(2).dayOfMonth
            }.sortedBy {
                it.date.hour
            }.also {
                csvReader.close()
            }
        }
    }
}