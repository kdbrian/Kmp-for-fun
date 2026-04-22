package com.farmconnect.core.ui.util

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitFetchPlace
import com.google.android.libraries.places.widget.PlaceAutocomplete
import com.google.android.libraries.places.widget.PlaceAutocompleteActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Isolated function to handle Place Autocomplete results
 */
private fun handlePlaceAutocompleteResult(
    result: ActivityResult,
    context: Context,
    coroutineScope: CoroutineScope,
    onPlaceSelected: (Place) -> Unit
) {
    result.data?.let { intent ->
        if (result.resultCode == PlaceAutocompleteActivity.Companion.RESULT_OK) {
            val prediction: AutocompletePrediction? =
                PlaceAutocomplete.getPredictionFromIntent(intent)

            prediction?.placeId?.let { placeId ->
                fetchPlaceDetails(
                    context = context,
                    placeId = placeId,
                    coroutineScope = coroutineScope,
                    onPlaceSelected = onPlaceSelected
                )
            }
        }
    }
}

/**
 * Isolated function to fetch place details using PlacesClient
 */
private fun fetchPlaceDetails(
    context: Context,
    placeId: String,
    coroutineScope: CoroutineScope,
    onPlaceSelected: (Place) -> Unit
) {
    val placesClient: PlacesClient = Places.createClient(context)

    coroutineScope.launch {
        try {
            val response = placesClient.awaitFetchPlace(
                placeId,
                listOf(
                    Place.Field.DISPLAY_NAME,
                    Place.Field.LOCATION
                )
            )
            onPlaceSelected(response.place)
        } catch (e: Exception) {
            // Handle error appropriately
            e.printStackTrace()
        }
    }
}

/**
 * Alternative: More reusable version with explicit launch function
 */
@Composable
fun PlacesAutocomplete(
    onPlaceSelected: (Place) -> Unit,
    content: @Composable (launchPicker: () -> Unit) -> Unit
) {
    val sessionToken =   AutocompleteSessionToken.newInstance()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handlePlaceAutocompleteResult(
            result = result,
            context = context,
            coroutineScope = coroutineScope,
            onPlaceSelected = onPlaceSelected
        )
    }

    val launchPicker = /*remember {*/
        {
            val intent = PlaceAutocomplete.createIntent(context) {
                setAutocompleteSessionToken(sessionToken)
            }
            launcher.launch(intent)
        }
//    }

    content(launchPicker)
}

