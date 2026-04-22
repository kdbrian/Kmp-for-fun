package com.farmconnect.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.util.SavedPlace

class SavedPlaceProvider : PreviewParameterProvider<SavedPlace> {
    override val values: Sequence<SavedPlace> = sequenceOf(
        SavedPlace(
            name = "Home",
            address = "13 Cornelia Street",
        ),
        SavedPlace(
            name = "14 Main Street",
            address = "14 Main Street, London",
        ),
        SavedPlace(
            name = "2972 Westheimer Rd.",
            address = "2972 Westheimer Rd.",
        ),
        SavedPlace(
            name = "2715 Ash Dr. San Jose",
            address = "2715 Ash Dr. San Jose",
        )
    )
}

@Composable
fun SavedPlaceItem(
    icon: @Composable () -> Unit,
    title: String,
    address: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFF3E8FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = LocalFontFamily.current
                ),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = "$address",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = LocalFontFamily.current
                ),
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSavedPlaceItemHome(
    @PreviewParameter(SavedPlaceProvider::class) savedPlace: SavedPlace
) {
    FarmConnectTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SavedPlaceItem(
                icon = {
                    Icon(
                        imageVector = if (savedPlace.name == "Home")
                            Icons.Default.Home
                        else
                            Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF7C3AED)
                    )
                },
                title = savedPlace.name,
                address = savedPlace.address,
                onClick = {}
            )
        }
    }
}


@Composable
private fun PreviewSavedPlacesList() {
    val locations = listOf(
        SavedPlace(
            name = "14 Main Street",
            address = "14 Main Street, London",
        ),
        SavedPlace(
            name = "2972 Westheimer Rd.",
            address = "2972 Westheimer Rd.",
        )
    )

    FarmConnectTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Selected locations",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            locations.forEach { location ->
                SavedPlaceItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF7C3AED)
                        )
                    },
                    title = location.name,
                    address = location.address,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
