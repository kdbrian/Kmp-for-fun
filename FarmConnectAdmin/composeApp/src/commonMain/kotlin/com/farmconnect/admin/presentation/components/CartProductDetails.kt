package com.farmconnect.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.components.cart.CartProductPreviewData
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.ui.util.toCurrency
import java.util.Locale
import kotlin.random.Random


val demoProduct = CartProductPreviewData(
    placeholder = R.drawable.grocery,
    title = "Demo Product",
    imageUrl = "https://picsum.photos/200/300",
    price = Random.nextDouble(100.0, 999.99),
)


@Composable
fun CartProductDetails(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {},
    onRemove: () -> Unit = {},
    onProductAmountUpdate: (Int) -> Unit = {},
    cartProductPreviewData: CartProductPreviewData = demoProduct
) {

    val disc = remember {
        derivedStateOf {
            if (cartProductPreviewData.discount == null)
                0.0
            else
                (cartProductPreviewData.discount / 100).times(cartProductPreviewData.price)

        }
    }


    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        color = color,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = cartProductPreviewData.imageUrl,
                modifier = Modifier
                    .width(100.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(cartProductPreviewData.placeholder),
                error = painterResource(cartProductPreviewData.placeholder),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                BasicText(
                    text = cartProductPreviewData.title,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = LocalFontFamily.current,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append(cartProductPreviewData.price.toCurrency(Locale.getDefault()))

                            withStyle(
                                SpanStyle(
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            ) {
                                append("/")
                                append(cartProductPreviewData.unit.label)
                            }
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = LocalFontFamily.current,
                        modifier = Modifier.weight(1f),
                        color = LocalPrimaryColor.current
                    )

                    if (cartProductPreviewData.discount != null && cartProductPreviewData.discount > 0)
                        Text(
                            text = String.format(
                                Locale.getDefault(),
                                "%.2f %% off",
                                cartProductPreviewData.discount
                            ),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = LocalFontFamily.current,
                                fontSize = 8.sp,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .padding(4.dp)
                                .background(
                                    shape = RectangleShape,
                                    color = Color.Red.copy(.55f)
                                )
                                .padding(4.dp),
                        )
                }


                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(50),
                            color = Color.LightGray
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    AnimatedVisibility(
                        visible = cartProductPreviewData.productAmount > 1
                    ) {
                        IconButton(
                            onClick = onRemove,
                            modifier = Modifier.background(
                                shape = CircleShape,
                                color = Color.Red
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteOutline,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        AnimatedContent(
                            cartProductPreviewData.productAmount == 1
                        ) {
                            when (it) {
                                true -> {
                                    IconButton(onClick = { onProductAmountUpdate(0) }) {
                                        Icon(
                                            imageVector = Icons.Rounded.DeleteOutline,
                                            contentDescription = null
                                        )
                                    }
                                }

                                false -> {
                                    IconButton(onClick = {
                                        onProductAmountUpdate(
                                            cartProductPreviewData.productAmount - 1
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.RemoveCircleOutline,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }


                        Text(
                            text = "${cartProductPreviewData.productAmount}",
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = LocalFontFamily.current,
                            modifier = Modifier.padding(8.dp)
                        )


                        IconButton(onClick = { onProductAmountUpdate(cartProductPreviewData.productAmount + 1) }) {
                            Icon(
                                imageVector = Icons.Rounded.AddCircleOutline,
                                contentDescription = null
                            )
                        }


                    }

                }

            }

        }
    }

}

@Preview
@Composable
private fun CartProductDetailsPrev(
    @PreviewParameter(CartProductDetailsPreviewParameterProvider::class)
    cartProductPreviewData: CartProductPreviewData
) {
    FarmConnectTheme {
        CartProductDetails(
            modifier = Modifier,
            cartProductPreviewData = cartProductPreviewData
        )
    }
}