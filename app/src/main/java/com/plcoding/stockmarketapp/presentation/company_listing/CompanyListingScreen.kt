package com.plcoding.stockmarketapp.presentation.company_listing

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.plcoding.stockmarketapp.presentation.company_info.CompanyInfoScreen
import com.plcoding.stockmarketapp.presentation.destinations.CompanyInfoScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun CompanyListingScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListingViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
//        search box
        OutlinedTextField(
            value = state.query,
            onValueChange = { query ->
                viewModel.onEvent(
                    event = CompanyListingEvent.OnSearchQueryChange(
                        query = query
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            maxLines = 1,
            placeholder = {
                Text(text = "Search ...")
            }
        )
// Company listing
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
            viewModel.onEvent(
                CompanyListingEvent.Refresh
            )
        }
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(state.companies.size) { index: Int ->
                    CompanyItem(
                        company = state.companies[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                           navigator.navigate(
                               CompanyInfoScreenDestination(symbol = state.companies[index].symbol)
                           )
                            }
                    )

                    if (index < state.companies.size - 1) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }

        }
    }

}