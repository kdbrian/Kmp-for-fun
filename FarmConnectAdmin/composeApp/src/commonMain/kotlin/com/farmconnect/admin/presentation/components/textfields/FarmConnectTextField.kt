package com.farmconnect.core.ui.components.textfields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmconnect.core.ui.theme.LocalFontFamily

@Composable
fun FarmConnectTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: @Composable (() -> Unit)? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    isError: Boolean = false,
    minLines: Int = 1,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: TextFieldColors = TextFieldDefaults.colors(
        errorIndicatorColor = Color.Transparent,
        errorTextColor = Color.White,
        errorContainerColor = Color(0xFFF24236).copy(0.65f),
        focusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    )
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = label,
        placeholder = {
            Text(
                placeholder ?: "",
                fontFamily = LocalFontFamily.current,
                style = MaterialTheme.typography.labelLarge,
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        supportingText = supportingText,
        singleLine = singleLine,
        minLines = minLines,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        shape = shape,
        colors = colors,
        textStyle = TextStyle(
            fontFamily = LocalFontFamily.current,
            fontSize = 16.sp
        )
    )


}

@Preview
@Composable
private fun FarmConnectTextFieldPrev() {
    var isDropDownExpanded by remember { mutableStateOf(false) }

    FarmConnectTextField(
        placeholder = "Enter phone number",
        leadingIcon = {
            Text(
                text = "😍",
                style = MaterialTheme.typography.titleMedium
            )
        },
        trailingIcon = {
            Box(
                modifier = Modifier.padding(8.dp).clickable{
                    isDropDownExpanded = !isDropDownExpanded
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(8.dp)
                ) {

                    Text(
                        text = "Expand",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Icon(
                        imageVector = if (isDropDownExpanded)
                            Icons.Rounded.KeyboardArrowUp
                        else
                            Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                    )

                }

                DropdownMenu(
                    expanded = isDropDownExpanded,
                    onDismissRequest = { isDropDownExpanded = !isDropDownExpanded },
                    modifier = Modifier,
                    content = {
                        repeat(10) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Item $it"
                                    )
                                },
                                onClick = {
                                    isDropDownExpanded = !isDropDownExpanded
                                }
                            )
                        }
                    }
                )
            }
        }
    )
}
