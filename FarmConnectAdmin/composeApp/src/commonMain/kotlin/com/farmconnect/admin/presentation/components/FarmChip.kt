package com.farmconnect.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.AppBlue
import com.farmconnect.core.ui.theme.AppLightBlue
import com.farmconnect.core.ui.theme.AppOrange
import com.farmconnect.core.ui.theme.AppYellow
import com.farmconnect.core.ui.theme.Cancelled
import com.farmconnect.core.ui.theme.Completed
import com.farmconnect.core.ui.theme.Failed
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.Pending
import com.farmconnect.core.ui.theme.Processed
import com.farmconnect.core.ui.theme.Staged
import com.farmconnect.core.ui.theme.Success
import com.farmconnect.core.ui.theme.Unknown


class FarmChipPreviewParameterProvider :
    PreviewParameterProvider<Pair<Pair<Color, Color>, Pair<BorderStroke?, Shape>>> {

    override val values: Sequence<Pair<Pair<Color, Color>, Pair<BorderStroke?, Shape>>>
        get() = sequenceOf(
            // Solid filled variants (color to contentColor)
            Pair(Pair(AppOrange, Color.White), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(AppBlue, Color.White), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(AppLightBlue, Color.White), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(AppYellow, Color.Black), Pair(null, RoundedCornerShape(50))),

            // Status variants — filled, pill shape
            Pair(Pair(Processed, Color.White), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(Completed, Color.White), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(Cancelled, Color.White), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(Pending, Color.Black), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(Failed, Color.White), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(Success, Color.White), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(Staged, Color.White), Pair(null, RoundedCornerShape(50))),
            Pair(Pair(Unknown, Color.White), Pair(null, RoundedCornerShape(50))),

            // Outlined variants — transparent bg, colored border + text
            Pair(
                Pair(Color.Transparent, AppOrange),
                Pair(BorderStroke(1.dp, AppOrange), RoundedCornerShape(50))
            ),
            Pair(
                Pair(Color.Transparent, AppBlue),
                Pair(BorderStroke(1.dp, AppBlue), RoundedCornerShape(50))
            ),
            Pair(
                Pair(Color.Transparent, Completed),
                Pair(BorderStroke(1.dp, Completed), RoundedCornerShape(50))
            ),
            Pair(
                Pair(Color.Transparent, Failed),
                Pair(BorderStroke(1.dp, Failed), RoundedCornerShape(50))
            ),
            Pair(
                Pair(Color.Transparent, Pending),
                Pair(BorderStroke(1.dp, Pending), RoundedCornerShape(50))
            ),
            Pair(
                Pair(Color.Transparent, Staged),
                Pair(BorderStroke(1.dp, Staged), RoundedCornerShape(50))
            ),

            // Rounded rectangle shape variants
            Pair(Pair(AppBlue, Color.White), Pair(null, RoundedCornerShape(8.dp))),
            Pair(Pair(Failed, Color.White), Pair(null, RoundedCornerShape(8.dp))),
            Pair(Pair(Success, Color.White), Pair(null, RoundedCornerShape(8.dp))),

            // Cut corner shape variants
            Pair(Pair(Staged, Color.White), Pair(null, CutCornerShape(4.dp))),
            Pair(Pair(AppOrange, Color.White), Pair(null, CutCornerShape(4.dp))),

            // Outlined + rounded rect
            Pair(
                Pair(Color.Transparent, AppBlue),
                Pair(BorderStroke(1.dp, AppBlue), RoundedCornerShape(8.dp))
            ),
            Pair(
                Pair(Color.Transparent, Failed),
                Pair(BorderStroke(1.dp, Failed), RoundedCornerShape(8.dp))
            ),

            // Thick border variants
            Pair(
                Pair(Color.White, AppOrange),
                Pair(BorderStroke(2.dp, AppOrange), RoundedCornerShape(50))
            ),
            Pair(
                Pair(Color.White, Staged),
                Pair(BorderStroke(2.dp, Staged), RoundedCornerShape(50))
            ),
        )

}

@Composable
fun FarmChip(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    text: @Composable () -> Unit = {},
    onClick: () -> Unit = {},
    border: BorderStroke? = null,
    shape: Shape = RoundedCornerShape(50)
) {

    Surface(
        modifier = modifier,
        color = color,
        shape = shape,
        contentColor = contentColor,
        onClick = onClick,
        border = border
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            text()
        }

    }

}

@Preview(showBackground = true)
@Composable
fun FarmChipPrev(
    @PreviewParameter(FarmChipPreviewParameterProvider::class)
    parameters: Pair<Pair<Color, Color>, Pair<BorderStroke?, Shape>>
) {
    FarmConnectTheme {
        FarmChip(
            text = {
                Text(
                    text = "My Chip",
                    fontFamily = LocalFontFamily.current,
                    style = MaterialTheme.typography.labelLarge
                )
            },
            color = parameters.first.first,
            contentColor = parameters.first.second,
            onClick = { },
            border = parameters.second.first,
            shape = parameters.second.second,
        )
    }
}
