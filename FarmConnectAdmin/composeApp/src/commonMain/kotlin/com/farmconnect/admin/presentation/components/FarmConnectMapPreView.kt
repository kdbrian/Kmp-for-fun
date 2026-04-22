package com.farmconnect.core.ui.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuOpen
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

// Define the two main states for the bottom sheet
sealed class LocationSheetState {
    data object SelectLocation : LocationSheetState()
    data class ConfirmLocation(val address: String) : LocationSheetState()
}

// Main Composable that replicates the map and bottom sheets
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmConnectMapPreView(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    // A location in Nigeria (e.g., Abuja) to better match the context (Kado, Abuja)
    val abuja = LatLng(9.0765, 7.3986)
    val mapCenter = remember { mutableStateOf(abuja) }

    // State for the draggable marker in the center of the screen
    val markerState = rememberUpdatedMarkerState(position = mapCenter.value)

    // Camera state centered around the initial location
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(abuja, 14f)
    }

    // State to control which bottom sheet content is shown
    var currentSheetState by remember {
        mutableStateOf<LocationSheetState>(LocationSheetState.SelectLocation)
    }

    // State for the Modal Bottom Sheet
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            FarmConnectElevatedAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.MenuOpen,
                            contentDescription = "Menu"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = "Schedule"
                        )
                    }
                },
                parameters = FarmConnectElevatedAppBarPreviewParameters(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
            )
        },
        // The bottomBar from your initial code is replaced by the bottom sheet
        // to match the screenshot structure.
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Google Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false, // Disable default zoom controls
                    compassEnabled = false,
                    myLocationButtonEnabled = false
                ),
                properties = MapProperties(isMyLocationEnabled = false),
                onMapLoaded = {
                    // Optional: Code to run after the map has loaded
                }
            ) {
                // We use a MarkerState, but visually, the pin is centered on the map
                // In a real app, you'd listen to cameraPositionState.position.target
                // to get the center coordinate.
            }

            // The central pin icon, independent of the Marker composable
            Icon(
                // This is a simplified representation of the pin in the screenshot
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = "Location Pin",
                tint = Color.Black,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 30.dp) // Adjust for pin's height
            )

            // 2. Bottom Sheet Content
            // We'll use a standard ModalBottomSheet for simplicity in this example
            if (sheetState.isVisible) {
                androidx.compose.material3.ModalBottomSheet(
                    onDismissRequest = { /* Don't dismiss to match screen 2/3 */ },
                    sheetState = sheetState,
                    containerColor = Color.White
                ) {
                    when (currentSheetState) {
                        is LocationSheetState.SelectLocation -> SelectLocationContent(
                            onUseCurrentLocation = {
                                // In a real app, this would get and set the current GPS location
                                currentSheetState = LocationSheetState.ConfirmLocation(
                                    "Current GPS Location (Simulated)"
                                )
                            },
                            onSetLocationManually = {
                                // Simulate picking a location on the map
                                val currentLatLon = cameraPositionState.position.target
                                currentSheetState = LocationSheetState.ConfirmLocation(
                                    "Ota Highway (Simulated, Lat: ${
                                        String.format(
                                            "%.2f",
                                            currentLatLon.latitude
                                        )
                                    })"
                                )
                            }
                        )

                        is LocationSheetState.ConfirmLocation -> ConfirmLocationContent(
                            address = (currentSheetState as LocationSheetState.ConfirmLocation).address,
                            onSetLocation = {
                                // Final action to confirm the location
                            },
                            onEdit = {
                                // Go back to the selection screen, but keep the map position
                                currentSheetState = LocationSheetState.SelectLocation
                                // In a real app, you might show the search bar from image 1 here.
                            }
                        )
                    }
                }
            }
        }
    }
}


// Content for the "Set Your Location" sheet (Image 2)
@Composable
fun SelectLocationContent(
    onUseCurrentLocation: () -> Unit,
    onSetLocationManually: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Map Icon Placeholder
        Icon(
            imageVector = Icons.Rounded.Map,
            contentDescription = "Map Icon",
            modifier = Modifier.height(48.dp)
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Set Your Location",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        Text(
            text = "To connect with professionals in your area, set your location",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(32.dp))

        FarmConnectButton(onClick = onUseCurrentLocation) {
            Text("Use Current Location", color = Color.White)
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onSetLocationManually,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            )
        ) {
            Text("Set Location Manually")
        }
        Spacer(Modifier.height(32.dp))
    }
}


// Content for the "Confirm your Location" sheet (Image 3)
@Composable
fun ConfirmLocationContent(
    address: String,
    onSetLocation: () -> Unit,
    onEdit: () -> Unit
) {
    Column(
        modifier = Modifier.padding(
            start = 24.dp,
            end = 24.dp,
            top = 16.dp,
            bottom = 32.dp
        )
    ) {
        Text(
            text = "Confirm your Location",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        // Location Display Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = address,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            IconButton(
                onClick = onEdit,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Edit Location"
                )
            }
        }
        Spacer(Modifier.height(24.dp))

        FarmConnectButton(onClick = onSetLocation) {
            Text("Set Location", color = Color.White)
        }
    }
}


@Preview
@Composable
private fun FarmConnectMapPreViewPrev() {
    FarmConnectTheme {
        FarmConnectMapPreView()
    }
}