package com.farmconnect.core.ui.components.auth

import android.util.Patterns
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmconnect.core.ui.components.EmailPasswordAuthenticationFlowUiState
import com.farmconnect.core.ui.components.FarmConnectButton
import com.farmconnect.core.ui.components.textfields.FarmConnectTextField
import com.farmconnect.core.ui.theme.AppYellow
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.util.Resource

@Composable
fun PasswordReset(
    modifier: Modifier = Modifier,
    uiState: EmailPasswordAuthenticationFlowUiState = EmailPasswordAuthenticationFlowUiState(),
    onEmailChange: (String) -> Unit = {},
    onDone: () -> Unit = {},
) {

    Column(
        modifier = modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = buildAnnotatedString {
                append("Reset password")
                append('\n')
                withStyle(SpanStyle(fontSize = 14.sp, fontWeight = FontWeight.Light)) {
                    append("Enter your email, where instructions to reset your password will be sent.")
                }
            },
            style = MaterialTheme.typography.displayMedium,
            fontFamily = LocalFontFamily.current,
        )
        val focusManager = LocalFocusManager.current

        AnimatedContent(
            targetState = uiState.authenticationResource is Resource.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            when (it) {
                true -> CircularProgressIndicator(
                    trackColor = Color.White,
                    color = Color.Transparent
                )

                else -> FarmConnectTextField(
                    placeholder = "your email",
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done, keyboardType = KeyboardType.Email
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()
                                    .not()
                            ) return@KeyboardActions
                            else {
                                focusManager.clearFocus()
                                onDone()
                            }
                        }),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.AlternateEmail, contentDescription = null
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        FarmConnectButton(
            title = "Reset Password",
            color = AppYellow,
            shape = RoundedCornerShape(50),
            contentColor = Color.White,
            onClick = onDone
        )

    }

}