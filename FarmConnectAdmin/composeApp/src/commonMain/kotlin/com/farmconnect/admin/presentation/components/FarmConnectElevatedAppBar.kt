package com.farmconnect.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmconnect.core.ui.theme.LocalFontFamily


data class FarmConnectElevatedAppBarPreviewParameters(
    val title: AnnotatedString = buildAnnotatedString { append(LoremIpsum(2).values.joinToString()) },
    val centerTitle: Boolean = false,
    val containerColor: Color = Color.Transparent,
    val contentColor: Color = Color.Black,
)


internal class FarmConnectElevatedAppBarPreviewParameterProvider(
    override val values: Sequence<FarmConnectElevatedAppBarPreviewParameters>
    = sequenceOf(
        FarmConnectElevatedAppBarPreviewParameters(
            title = buildAnnotatedString { append(LoremIpsum(2).values.joinToString()) },
            centerTitle = true,
            containerColor = Color.Yellow,
            contentColor = Color.White
        ),
        FarmConnectElevatedAppBarPreviewParameters(
            title = buildAnnotatedString { append(LoremIpsum(2).values.joinToString()) },
            centerTitle = false,
        )
    )
) :
    PreviewParameterProvider<FarmConnectElevatedAppBarPreviewParameters>

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmConnectElevatedAppBar(
    modifier: Modifier = Modifier,
    parameters: FarmConnectElevatedAppBarPreviewParameters = FarmConnectElevatedAppBarPreviewParameters(),
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    parameters.run {
        CenterAlignedTopAppBar(
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            modifier = modifier.padding(8.dp),
            navigationIcon = navigationIcon,
            title = {
                BasicText(
                    text = title,
                    style = TextStyle(
                        color = contentColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = if (
                            parameters.centerTitle
                        ) TextAlign.Center else TextAlign.Start,
                        fontFamily = LocalFontFamily.current
                    ),
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                ,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = containerColor,
                titleContentColor = contentColor,
                actionIconContentColor = contentColor,
                navigationIconContentColor = contentColor
            ),
            actions = actions
        )
    }
}

@Preview
@Composable
private fun FarmConnectElevatedAppBarPrev(
    @PreviewParameter(
        provider = FarmConnectElevatedAppBarPreviewParameterProvider::class
    )
    parameters: FarmConnectElevatedAppBarPreviewParameters
) {
    FarmConnectElevatedAppBar(
        parameters = parameters
    )

}