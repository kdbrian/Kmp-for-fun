package com.farmconnect.core.ui.components.auth

import android.util.Patterns
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.components.EmailPasswordAuthenticationFlowUiState
import com.farmconnect.core.ui.components.FarmConnectButton
import com.farmconnect.core.ui.components.textfields.FarmConnectOutlinedTextField
import com.farmconnect.core.ui.theme.AppLightBlue
import com.farmconnect.core.ui.theme.AppYellow
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.util.Resource

internal class SignInScreenPreviewParameterProvider :
    PreviewParameterProvider<EmailPasswordAuthenticationFlowUiState> {

    override val values: Sequence<EmailPasswordAuthenticationFlowUiState>
        get() = sequenceOf(
            EmailPasswordAuthenticationFlowUiState(
                email = "james.wilson@example-pet-store.com",
                password = "password",
                authenticationResource = Resource.Success(true)
            ),
            EmailPasswordAuthenticationFlowUiState(
                email = "william.paterson@my-own-personal-domain.com",
                password = "password",
                authenticationResource = Resource.Loading()
            ),
            EmailPasswordAuthenticationFlowUiState(
                email = "william.henry.harrison@example-pet-store.com",
                password = "password",
                authenticationResource = Resource.Error("error")
            ),
            EmailPasswordAuthenticationFlowUiState(
                email = "",
                password = "",
                authenticationResource = Resource.Success(false)
            ),
        )
}


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    uiState: EmailPasswordAuthenticationFlowUiState = EmailPasswordAuthenticationFlowUiState(),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    passwordVisibility: Boolean = false,
    onPasswordVisibilityToggle: (Boolean) -> Unit = {},
    onDone: () -> Unit = {},
    onPasskeySignIn: () -> Unit = {},
    onSetUpAccount: () -> Unit = {},
    onPasswordReset: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    uiState.run {
        Column(
            modifier = modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            FarmConnectOutlinedTextField(
                shape = RoundedCornerShape(50),
                value = email,
                isError = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    .not(),
                placeholder = "email",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.AlternateEmail, contentDescription = null
                    )
                },
                trailingIcon = {
                    AnimatedContent(
                        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                            .not()
                    ) {
                        if (it) Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = null,
                            tint = Color.Red
                        )
                        else Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }
                },
                onValueChange = onEmailChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                .not()
                        ) return@KeyboardActions
                        else focusManager.moveFocus(FocusDirection.Down)
                    }),
            )

            Text(
                text = if (email.isEmpty()) "email is required."
                else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        .not()
                ) "email is not valid."
                else "",
                style = MaterialTheme.typography.labelSmall,
                fontFamily = LocalFontFamily.current,
                modifier = Modifier.padding(6.dp)
            )

            FarmConnectOutlinedTextField(
                shape = RoundedCornerShape(50),
                onValueChange = onPasswordChange,
                value = password,
                placeholder = "password",
                leadingIcon = {
                    if (password.isNotEmpty()) AnimatedContent(
                        password.length !in 8..12
                    ) {
                        if (it) Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = null,
                            tint = Color.Red
                        )
                        else Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }
                    else Icon(
                        imageVector = Icons.Rounded.Lock, contentDescription = null
                    )
                },
                isError = password.isNotEmpty() && password.length < 8,
                trailingIcon = {
                    val icon = if (passwordVisibility) Icons.Rounded.VisibilityOff
                    else Icons.Rounded.Visibility

                    IconButton(onClick = {
                        onPasswordVisibilityToggle(!passwordVisibility)
                    }) {
                        Icon(
                            imageVector = icon, contentDescription = null
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                .not()
                        ) return@KeyboardActions
                        else focusManager.moveFocus(FocusDirection.Down)
                    }),
                visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
            )

            Text(
                text = if (password.isEmpty()) "password is required."
                else if (password.length < 8) "password must be at least 8 characters."
                else "",
                style = MaterialTheme.typography.labelSmall,
                fontFamily = LocalFontFamily.current,
                modifier = Modifier.padding(6.dp)
            )


            Spacer(Modifier.padding(8.dp))

            AnimatedContent(
                authenticationResource is Resource.Loading
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (it) {
                        CircularProgressIndicator(
                            color = Color.White,
                            trackColor = LocalPrimaryColor.current
                        )
                    } else {
                        FarmConnectButton(
                            title = "Sign In",
                            shape = RoundedCornerShape(50),
                            color = AppYellow,
                            onClick = onDone,
                        )
                    }
                }
            }


            Spacer(Modifier.padding(4.dp))


            Text(
                text = buildAnnotatedString {
                    append("I am new.")

                    withStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            color = AppLightBlue
                        )
                    ) {
                        append("Create Account")
                    }
                },
                style = MaterialTheme.typography.labelLarge.copy(
                    textAlign = TextAlign.Center,
                ),
                fontFamily = LocalFontFamily.current,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSetUpAccount() })

            Spacer(Modifier.padding(12.dp))

            Text(
                text = buildAnnotatedString {
                    append("Forgot password? ")

                },
                style = MaterialTheme.typography.labelLarge.copy(
                    textAlign = TextAlign.Center,
                ),
                fontFamily = LocalFontFamily.current,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPasswordReset() })


        }
    }
}


@Preview
@Composable
private fun SignInScreenPrev(
    @PreviewParameter(SignInScreenPreviewParameterProvider::class)
    uiState: EmailPasswordAuthenticationFlowUiState
) {
    FarmConnectTheme {
        SignInScreen(
            uiState = uiState,
            onEmailChange = {},
            onPasswordChange = {},
            onDone = {},
            onPasswordVisibilityToggle = {},
            onPasskeySignIn = {},
            onSetUpAccount = {},
            onPasswordReset = {}
        )

    }
}

