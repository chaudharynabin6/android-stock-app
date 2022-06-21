package com.plcoding.stockmarketapp.presentation.company_listing

import com.plcoding.stockmarketapp.domain.model.CompanyListing

// val is used for immutability
data class CompanyListingState(
    val companies: List<CompanyListing> = emptyList(),
    val isRefreshing : Boolean = false,
    val isSwapping : Boolean = false,
    val query : String = ""
    )