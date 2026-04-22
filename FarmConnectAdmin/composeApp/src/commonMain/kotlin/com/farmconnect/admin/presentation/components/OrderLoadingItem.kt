package com.farmconnect.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.util.shimmer

@Composable
fun OrderLoadingItem(index: Int)  {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.Companion.Top
    ) {
        Box(
            modifier = Modifier.Companion
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmer(durationMillis = 2000)
        )

        Column(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                    .shimmer(durationMillis = 2000)
            )
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth(.65f)
                    .height(16.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                    .shimmer(durationMillis = 2000)
            )

            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxWidth(.45f)
                        .height(16.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                        .shimmer(durationMillis = 2000)
                )
                Box(
                    modifier = Modifier.Companion
                        .fillMaxWidth(.10f)
                        .height(16.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                        .shimmer(durationMillis = 2000)
                )
                Box(
                    modifier = Modifier.Companion
                        .fillMaxWidth(.35f)
                        .height(16.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                        .shimmer(durationMillis = 2000)
                )
            }

        }
    }

}