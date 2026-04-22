package com.farmconnect.core.ui.components.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.components.SettingsRowItem
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.ui.util.toCurrency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFilters(
    modifier: Modifier = Modifier,
    categoriesList: List<String> = (1..12).map { LoremIpsum(it).values.joinToString() }.shuffled(),
    tagsList: List<String>? = (1..12).map { LoremIpsum(it).values.joinToString() }.shuffled(),
    selectedCategory: String? = null,
    selectedTags: List<String>? = null,
    priceValueRange: ClosedFloatingPointRange<Float>? = 100f..1000f,
    onSelectTag: (String) -> Unit = { },
    onSelectCategory: (String) -> Unit = { },
    onPriceRangeUpdate: (Float) -> Unit = { },
    onApplyFilters: () -> Unit = { }
) {

    val sliderState = remember {
        SliderState(valueRange = priceValueRange ?: 0f..0f, steps = 5)
    }
    LaunchedEffect(sliderState.value) {
        onPriceRangeUpdate(sliderState.value)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        //price slider
        AnimatedVisibility(
            visible = priceValueRange != null
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Price",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = LocalFontFamily.current,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )

                    Text(
                        text = sliderState.value.toDouble().toCurrency(Locale.getDefault()),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = LocalFontFamily.current,
                            color = LocalPrimaryColor.current,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                Slider(
                    state = sliderState,
                    colors = SliderDefaults.colors(
                        inactiveTrackColor = LocalPrimaryColor.current,
                        activeTrackColor = Color.DarkGray,
                        inactiveTickColor = Color.White,
                        activeTickColor = Color.White,
                    ),
                    modifier = Modifier.padding(8.dp),
                )
            }
        }

        // Category
        AnimatedVisibility(
            visible = categoriesList.isNotEmpty()
        ) {
            Column {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = LocalFontFamily.current,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                LazyHorizontalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    rows = StaggeredGridCells.Fixed(3)
                ) {
                    items(categoriesList) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = LocalFontFamily.current,
                                fontWeight = FontWeight.Light,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .padding(6.dp)
                                .background(
                                    color = if (selectedCategory.equals(it)) Color(0xFFA42CD6) else Color(
                                        0xFFA42CD6
                                    ).copy(.55f),
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(8.dp)
                                .clickable { onSelectCategory(it) }
                        )
                    }
                }


            }
        }


        //Tags
        AnimatedVisibility(
            visible = tagsList?.isNotEmpty() == true
        ) {
            Column {
                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = LocalFontFamily.current,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                LazyHorizontalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    rows = StaggeredGridCells.Fixed(3)
                ) {
                    tagsList?.let { tags ->
                        items(tags) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = LocalFontFamily.current,
                                    fontWeight = FontWeight.Light,
                                    color = Color.White
                                ),
                                modifier = Modifier
                                    .padding(6.dp)
                                    .background(
                                        color = if (selectedTags?.isNotEmpty() == true && selectedTags.contains(
                                                it
                                            )
                                        ) Color(0xFFA42CD6) else Color(0xFFA42CD6).copy(.55f),
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(8.dp)
                                    .clickable { onSelectTag(it) }
                            )
                        }
                    }
                }


            }
        }

        Spacer(Modifier.padding(8.dp))
        SettingsRowItem(
            title = buildAnnotatedString {
                append("Apply")
            },
            onClick = onApplyFilters,
            centerTitle = true,
            color = LocalPrimaryColor.current,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )

    }


}

@Preview(showBackground = true)
@Composable
private fun ProductFiltersPrev() {
    ProductFilters(
        selectedCategory = "Lorem"
    )
}