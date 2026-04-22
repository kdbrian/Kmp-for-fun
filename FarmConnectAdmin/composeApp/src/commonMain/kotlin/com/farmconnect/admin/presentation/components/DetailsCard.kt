package com.farmconnect.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.LocalFontFamily


@Composable
fun String.ToComposeText(
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        this,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
        fontFamily = LocalFontFamily.current
    )
}
typealias CompLamb = RowScope.(Modifier) -> Unit

@Composable
fun DetailsCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    header: (@Composable () -> Unit)? = {
        Text(
            title ?: LoremIpsum(2).values.joinToString(),
            style = MaterialTheme.typography.titleLarge
        )
    },
    separator: (@Composable () -> Unit)? = {
        VerticalDivider()
    },
    contents: List<Pair<CompLamb, CompLamb>> = emptyList()
) {
    val shadow = Shadow(
        radius = 8.dp, color = Color.LightGray.copy(.7f)
    )

//    Composable<U>

    val cardsModifier = modifier
        .padding(6.dp)
        .dropShadow(
            shape = RoundedCornerShape(12.dp), shadow = shadow
        )

    Surface(
        shape = RoundedCornerShape(12.dp), modifier = cardsModifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            header?.invoke()

            Spacer(Modifier.padding(4.dp))

            contents.forEach { content ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    content.first(this, Modifier.weight(1f))
                    separator?.invoke()
                    content.second(this, Modifier.weight(1f))
                }
            }
        }


    }

}

@Composable
@Preview
fun DetailsCardPreview() {
    DetailsCard(
    )

}