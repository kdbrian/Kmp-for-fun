package com.farmconnect.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.theme.LocalFontFamily

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    categoryName: String = LoremIpsum(2).values.joinToString(),
    onClick: () -> Unit = {},
    shape: Shape = RoundedCornerShape(12.dp),
    color: Color = Color.Red,
    image: Int? = R.drawable.grocery,
    contentColor: Color = Color.White
) {


    Surface(
        onClick = onClick,
        shape = shape,
        color = color,
        contentColor = contentColor,
        modifier = modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Image(
                painter = painterResource(image ?: R.drawable.placeholder),
                contentDescription = categoryName,
                modifier = Modifier
                    .size(50.dp)
                    .clip(/*color = color,*/ shape = CircleShape),
                contentScale = ContentScale.Crop
            )

            BasicText(
                text = categoryName,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 14.sp,
                    maxFontSize = 24.sp,
                    stepSize = 2.sp
                ),
                style = TextStyle(
                    color = contentColor,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontFamily = LocalFontFamily.current,
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

    }


}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun CategoryItemPrev() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        CategoryItem(
            modifier = Modifier.size(150.dp),
            image = null
        )
        CategoryItem(
            modifier = Modifier.size(300.dp),

            )

        CategoryItem(
            modifier = Modifier.fillMaxWidth(),
            image = null,
//            icon = {
//                Icon(imageVector = Icons.Rounded.Discount, contentDescription = null)
//            }
        )
    }
}