package com.farmconnect.core.ui.components.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.farmconnect.core.domain.dto.ProductOrServiceItemDto
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.ui.util.DateUtils
import com.farmconnect.core.ui.util.DateUtils.toFormattedDate
import kotlin.random.Random

object ProductListItems {

    @Composable
    fun DefaultProductListItem(
        modifier: Modifier = Modifier,
        shape: Shape = RoundedCornerShape(12.dp),
        onCategorySelect: (String) -> Unit = {},
        onClick: (String) -> Unit = {},
        productOrServiceItem: ProductOrServiceItemDto = ProductOrServiceItemDto(
            title = LoremIpsum(3).values.joinToString(),
            price = Random.nextDouble(100.0, 999.9),
            stock = Random.nextInt(10, 300).toDouble(),
            categories = listOf("Demo"),
            tags = (1..5).map { LoremIpsum(it).values.joinToString() },
            metadata = mapOf(
                "updatedOn" to System.currentTimeMillis().toString()
            )
        )
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            onClick = { onClick(productOrServiceItem.id.toString()) }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                AsyncImage(
                    model = if (productOrServiceItem.images.isNotEmpty()) productOrServiceItem.images.random() else null,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    error = painterResource(R.drawable.grocery),
                    contentScale = ContentScale.Fit,
                )

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        BasicText(
                            text = productOrServiceItem.title ?: "",
                            modifier = Modifier.weight(1f),
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = LocalFontFamily.current,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Text(
                            text = productOrServiceItem.stock?.toString() ?: "0",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = LocalFontFamily.current,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            modifier = Modifier
                                .padding(4.dp)
                                .background(
                                    color = Color.Yellow,
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(6.dp)
                        )

                    }


                    AnimatedVisibility(
                        productOrServiceItem.categories?.isNotEmpty() == true
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                        ) {

                            productOrServiceItem.categories?.take(5)?.forEach { categoryName ->
                                Text(
                                    text = categoryName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = LocalFontFamily.current,
                                        fontWeight = FontWeight.Light,
                                        color = Color.White
                                    ),
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .background(
                                            color = LocalPrimaryColor.current,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                        .clickable(true) {
                                            onCategorySelect(categoryName)
                                        }
                                )
                            }

                        }
                    }

                    Spacer(Modifier.padding(4.dp))
                    AnimatedVisibility(
                        productOrServiceItem.metadata.contains("updatedOn")
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                ) {
                                    append("Last update")
                                }
                                append(' ')

                                withStyle(
                                    SpanStyle(
                                        fontStyle = FontStyle.Italic,
                                    )
                                ) {
                                    append(
                                        productOrServiceItem.metadata["updatedOn"]?.toLong()
                                            ?.toFormattedDate(
                                                DateUtils.FORMAT_READABLE
                                            )
                                    )
                                }


                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = LocalFontFamily.current,
                                fontWeight = FontWeight.Light
                            ),
                            modifier = Modifier
                        )
                    }

                }
            }


        }

    }


}

@Preview
@Composable
private fun ProductListItemsPrev() {
    FarmConnectTheme {
        ProductListItems.DefaultProductListItem()
    }
}