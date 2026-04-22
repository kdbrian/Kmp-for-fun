package com.farmconnect.core.ui.components.products

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.farmconnect.core.domain.dto.ProductOrServiceItemDto
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.components.StarRatingBar
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.util.toCurrency
import java.util.Locale


@Composable
fun ProductCardStackedHorizontally(
    modifier: Modifier = Modifier,
//    isAvailableInCart: Boolean = false,
//    onAddToCart: () -> Unit = {},
    productOrServiceItemDto: ProductOrServiceItemDto = ProductOrServiceItemDto.demoProductDto,
    shape: Shape = RoundedCornerShape(8.dp),
    imageSize: Dp = 80.dp,
    imageShape: Shape = RoundedCornerShape(8.dp),
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {},
//    imageUrl: String? = "",
    @DrawableRes placeholder: Int = R.drawable.placeholder,
    extras: @Composable RowScope.() -> Unit = {
    },
//    title: String = LoremIpsum(3).values.joinToString(),
//    price: Double = Random.nextDouble(99.99, 999.99),
//    category: String = LoremIpsum(2).values.joinToString()
) {

    productOrServiceItemDto.run {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    onClick()
                }
                .background(color, shape),
        ) {
            AsyncImage(
                model = if(images.isNotEmpty()) images.random() else placeholder,
                modifier = Modifier
                    .size(imageSize)
                    .clip(imageShape),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(placeholder),
                error = painterResource(placeholder),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                BasicText(
                    text = title ?: "",
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = MaterialTheme.typography.titleMedium.fontSize,
                        maxFontSize = MaterialTheme.typography.titleLarge.fontSize,
                        stepSize = 2.2.sp
                    ),
                    style = TextStyle(
                        fontFamily = LocalFontFamily.current,
                        color = contentColor,
                    ),
                )

                if (rating != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        BasicText(
                            text = String.format(Locale.getDefault(), "(🌟%.2f)", rating),
                            autoSize = TextAutoSize.StepBased(
                                minFontSize = MaterialTheme.typography.labelMedium.fontSize,
                                maxFontSize = MaterialTheme.typography.labelLarge.fontSize,
                                stepSize = .2.sp
                            ),
                            style = TextStyle(
                                fontFamily = LocalFontFamily.current,
                                color = contentColor,
                            ),
                        )

                        StarRatingBar(
                            rating = rating!!,
                            starSize = 4f,
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }
                }

                BasicText(
                    text ="${price?.toCurrency()} /=" ,
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        maxFontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        stepSize = .2.sp
                    ),
                    style = TextStyle(
                        fontFamily = LocalFontFamily.current,
                        color = contentColor,
                    ),
                )

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    categories?.forEach { category ->
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = contentColor,
                                fontFamily = LocalFontFamily.current
                            ),
                            fontFamily = LocalFontFamily.current,
                            modifier = Modifier
                        )

                        if (categories!!.indexOf(category) != categories!!.lastIndex)
                            Text(
                                text = "·",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = LocalFontFamily.current,
                                    color = Color.DarkGray
                                ),
                                fontFamily = LocalFontFamily.current,
                                modifier = Modifier
                            )


                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    extras()
                }
            }
        }
    }

}

@Preview(
    showBackground = true
)
@Composable
private fun ProductCardStackedHorizontallyPrev() {
    FarmConnectTheme {
        ProductCardStackedHorizontally(
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = R.drawable.grocery,
            productOrServiceItemDto = ProductOrServiceItemDto.demoProductDto.copy(
                images = listOf("https://picsum.photos/200/300",)
            ),
        )
    }
}