package com.plcoding.stockmarketapp.presentation.company_listing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.stockmarketapp.domain.model.CompanyListing

@Composable
fun CompanyItem(
    company: CompanyListing,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            ) {
//      company name
                Text(
                    text = company.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
//                    weight is very necessary to work for overflow
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth(.5f)

                )

//        exchange
                Box(
                    modifier = Modifier.fillMaxWidth(.4f),
                    contentAlignment = Alignment.CenterEnd
                ){
                    Text(
                        text = company.symbol,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colors.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }


            }
//            space between the company name and exchange
            Spacer(modifier = Modifier.height(8.dp))

//            exchange
            Text(
                text = "(${company.exchange})",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(8.dp)
                )


        }
    }

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DisplayCompanyItems() {
    val companies = listOf(
        CompanyListing(
            "Google",
            "GO",
            "NY"
        ),
        CompanyListing(
            "Apple",
            "AP",
            "WS"
        ),
        CompanyListing(
            "Apple fdjfdljflkdjaf;lkdja;ldkfj;aldfkjhfkdjfljfldkjfl;sjdf;lkjs",
            "AP",
            "WS"
        ),
        CompanyListing(
            "Apple",
            "AP  djf;ldksjf;lsdkajf;lksdj;flkjs;ldkjf;slkjf;l",
            "WS"
        ),
        CompanyListing(
            "Apple fdjfdljflkdjaf;lkdja;ldkfj;aldfkjhfkdjflj",
            "AP  djf;ldksjf;lsdkajf;lksdj;flkjs;ldkjf;slkjf;lklfjdl;kfjldskjf;ldskja;slkfj;slkfj",
            "WS"
        ),

        )

    MaterialTheme {

        androidx.compose.material.Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(companies.size) { index: Int ->
                    CompanyItem(
                        company = companies[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }

}
