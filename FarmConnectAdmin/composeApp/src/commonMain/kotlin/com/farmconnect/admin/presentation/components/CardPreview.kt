package com.farmconnect.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.ui.util.DateUtils
import com.farmconnect.core.ui.util.DateUtils.toFormattedDate
import com.farmconnect.core.util.StringUtils


class CardPreviewParameterProvider : PreviewParameterProvider<CardUiState> {
    override val values: Sequence<CardUiState>
        get() = CardUiState.distinctCardUiStates.asSequence()
}


@Composable
fun CardPreview(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    cardUiState: CardUiState = CardUiState.distinctCardUiStates.random(),
    withAction: @Composable RowScope.() -> Unit = {}
) {

    val dateSplit = cardUiState.expiration.toFormattedDate(DateUtils.FORMAT_MONTH_YEAR).split(" ")
    val textMeasurer = rememberTextMeasurer()
    val isExpired = remember {
        derivedStateOf {
            cardUiState.expiration < System.currentTimeMillis()
        }
    }

    Row(
        modifier = modifier
            .background(
                color
            )
            .padding(6.dp),
        verticalAlignment = Alignment.Top,
    ) {

        Spacer(
            Modifier
                .width(6.dp)
                .height(30.dp)
                .background(
                    shape = RoundedCornerShape(50),
                    color = animateColorAsState(if (isExpired.value.not()) LocalPrimaryColor.current else Color.LightGray).value
                )
                .padding(12.dp)
                .padding(end = 24.dp)
        )

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicText(
                    text = cardUiState.ownerName,
                    style = TextStyle(
                        fontFamily = LocalFontFamily.current,
                        fontSize = 24.sp,
                        color = contentColor,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                withAction()
            }


            Text(
                text = "Expiry ${dateSplit.joinToString(", ")}",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = LocalFontFamily.current,
                    color = contentColor,
                    fontWeight = FontWeight.W500
                )
            )


            Text(
                text = StringUtils.maskCardNumber(cardUiState.cardNumber.toString(), hideCount = 8),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = LocalFontFamily.current,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                )
            )

            Text(
                text = cardUiState.cvc,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = LocalFontFamily.current,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.End,
                    textDecoration = TextDecoration.LineThrough,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .blur(16.dp)
            )

        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun CardPreviewPrev(
    @PreviewParameter(CardPreviewParameterProvider::class)
    cardUiState: CardUiState

) {
    FarmConnectTheme {
        CardPreview(
            cardUiState = cardUiState,
            withAction = {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null
                )
            }
        )
    }
}