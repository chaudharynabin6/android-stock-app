package com.plcoding.stockmarketapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>
    )

    @Query(""" DELETE FROM CompanyListingEntity""")
    suspend fun clearCompanyListingEntities()

    @Query("""
        SELECT * FROM
        CompanyListingEntity
        WHERE
        LOWER(name) LIKE "%" || LOWER(:query) || "%"
        OR
        LOWER(symbol) LIKE "%" || LOWER(:query) || "%"
        OR
        LOWER(exchange) LIKE "%" || LOWER(:query) || "%"
    """)
    suspend fun searchCompanyListing(query : String): List<CompanyListingEntity>

    @Query(
        """
            SELECT * FROM CompanyListingEntity
        """
    )
    suspend fun getAllCompanyListing() : List<CompanyListingEntity>
}