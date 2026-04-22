package com.farmconnect.core.ui.components.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.farmconnect.core.domain.dto.ProductOrServiceItemDto
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.components.StarRatingBar
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.ui.util.toCurrency
import java.util.Locale

class ProductCardPreviewParameterProvider : PreviewParameterProvider<ProductOrServiceItemDto> {
    override val values: Sequence<ProductOrServiceItemDto>
        get() = sequenceOf(
            ProductOrServiceItemDto.demoProductDto,
            ProductOrServiceItemDto()
        )
}


@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    productOrServiceItemDto: ProductOrServiceItemDto = ProductOrServiceItemDto.demoProductDto,
    isAvailableInCart: Boolean = false,
    onRemoveFromCart: () -> Unit = {},
    onAddToCart: () -> Unit = {},
    onClick: () -> Unit = {},
    borderStroke: BorderStroke = BorderStroke(0.dp, Color.Unspecified),
    shape: Shape = RoundedCornerShape(8.dp),
) {

    productOrServiceItemDto.run {
        Surface(
            modifier = modifier,
            border = borderStroke,
            onClick = onClick,
            shape = shape
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                AsyncImage(
                    model = if(images.isNotEmpty()) images.random() else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder),
                )

                Column(
                    modifier = Modifier
                        .padding(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    BasicText(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = title ?: "",
                        style = TextStyle(
                            fontFamily = LocalFontFamily.current,
                            fontSize = 24.sp,
                            color = LocalPrimaryColor.current,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        rating?.let {
                            if (it > 0)
                                StarRatingBar(
                                    rating = it,
                                    modifier = Modifier
                                        .padding(6.dp),
                                    starSize = 6f
                                )
                        }

                        AnimatedVisibility(
                            visible = discount != null,
                            modifier = Modifier,
                        ) {
                            if (discount != null)
                                Text(
                                    text = buildAnnotatedString {
                                        append(
                                            String.format(
                                                Locale.getDefault(),
                                                "%.2f",
                                                discount
                                            )
                                        )
                                        append(" % off")
                                    },
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFD17A22).copy(.65f),
                                            shape = RectangleShape
                                        )
                                        .padding(4.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White,
                                        fontFamily = LocalFontFamily.current,
                                        fontWeight = FontWeight.W400
                                    )
                                )

                        }
                    }


                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )
                            ) {
                                append(price?.toCurrency().toString())
                            }
                        },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.End
                        ),
                        fontFamily = LocalFontFamily.current,
                        modifier = Modifier
                    )


                }


//                FarmConnectButton(
//                    onClick = if (isAvailableInCart) onRemoveFromCart else onAddToCart,
//                    title = if (!isAvailableInCart) "Add to cart" else "Remove from cart",
//                    shape = RoundedCornerShape(8.dp),
//                    color = LocalPrimaryColor.current,
//                    contentColor = Color.White,
//                )

                IconButton(
                    onClick = if (isAvailableInCart) onRemoveFromCart else onAddToCart,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = animateColorAsState(
                            if (isAvailableInCart) Color.LightGray else LocalPrimaryColor.current
                        ).value,
                        contentColor = animateColorAsState(
                            Color.White
                        ).value
                    )
                ) {
                    Icon(
                        imageVector = if (isAvailableInCart) Icons.Rounded.Remove else Icons.Rounded.Add,
                        contentDescription = if (isAvailableInCart) "Remove from cart" else "Add to cart",
                        modifier = Modifier
                    )
                }
            }

        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ProductCardPrev(
    @PreviewParameter(ProductCardPreviewParameterProvider::class)
    productOrServiceItemDto: ProductOrServiceItemDto
) {
    FarmConnectTheme {
        FlowRow(
            maxItemsInEachRow = 3,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            repeat(10) {
                ProductCard(
                    modifier = Modifier.weight(.5f),
                    productOrServiceItemDto = productOrServiceItemDto,
                    shape = RoundedCornerShape(24.dp),
                )
            }
        }
    }
}