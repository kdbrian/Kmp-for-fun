package com.farmconnect.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.FarmConnectTheme

@Composable
fun OrdersScreenLoading() {


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        repeat(5) {
            OrderLoadingItem(it)
            if (it!=4)
                HorizontalDivider(Modifier.Companion.padding(horizontal = 12.dp))

        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrdersScreenLoadingPrev() {
    FarmConnectTheme {
        OrdersScreenLoading()
    }
}
