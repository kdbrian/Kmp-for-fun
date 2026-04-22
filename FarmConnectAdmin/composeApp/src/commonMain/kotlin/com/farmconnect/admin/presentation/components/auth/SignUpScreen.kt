package com.farmconnect.core.ui.components.auth

import android.util.Patterns
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.components.EmailPasswordAuthenticationFlowUiState
import com.farmconnect.core.ui.components.FarmConnectButton
import com.farmconnect.core.ui.components.textfields.FarmConnectOutlinedTextField
import com.farmconnect.core.ui.theme.AppYellow
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.util.AccountType
import com.farmconnect.core.util.Resource
import com.farmconnect.core.util.StringUtils

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    uiState: EmailPasswordAuthenticationFlowUiState = EmailPasswordAuthenticationFlowUiState(),
    onEmailChange: (String) -> Unit = {},
    onAccountTypeChange: (AccountType) -> Unit = {},
    onPhoneNumberChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    passwordVisibility: Boolean = false,
    onPasswordVisibilityToggle: (Boolean) -> Unit = {},
    onDone: () -> Unit = {},
    onPasskeySignUp: () -> Unit = {},
) {
    uiState.run {
    val focusManager = LocalFocusManager.current
    val isNumberOk by remember(phoneNumber){
        derivedStateOf {
            phoneNumber.isNotEmpty() && phoneNumber.length in 10..12 &&
                    StringUtils.kenyanRegex.matches(
                        phoneNumber.replace(
                            Regex("^(0[1|7])"),
                            "+254${phoneNumber[1]}"
                        )
                    )

        }
    }

        Column(
            modifier = modifier
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {


            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                FarmConnectOutlinedTextField(
                    shape = RoundedCornerShape(50),
                    value = email,
                    isError = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        .not(),
                    placeholder = "email",
                    trailingIcon = {
                        AnimatedVisibility(
                            email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                .not(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.AlternateEmail, contentDescription = null
                        )
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
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {

                FarmConnectOutlinedTextField(
                    shape = RoundedCornerShape(50),
                    onValueChange = onPasswordChange,
                    value = password,
                    placeholder = "password",
                    leadingIcon = {
                        if (password.isNotEmpty()) AnimatedContent(
                            StringUtils.passwordRegex.matches(password).not()
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
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (StringUtils.passwordRegex.matches(password)
                                    .not()
                            ) return@KeyboardActions
                            else focusManager.moveFocus(FocusDirection.Down)
                        }),
                    visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
                )
                Text(
                    text = if (password.isEmpty()) "password is required."
                    else if (StringUtils.passwordRegex.matches(password)
                            .not()
                    ) "Password must contain at least an uppercase letter e.g(A,Z), a lowercase letter e.g(c,d), a number e.g(1,2) and a special character e.g($,#)."
                    else if (password.length < 8) "password must be at least 8 characters."
                    else "",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = LocalFontFamily.current,
                    modifier = Modifier.padding(6.dp)
                )
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {


                FarmConnectOutlinedTextField(
                    shape = RoundedCornerShape(50),
                    onValueChange = onPhoneNumberChange,
                    value = phoneNumber,
                    isError = isNumberOk.not(),
                    placeholder = "phone number",
                    trailingIcon = {
                        AnimatedContent(
                            phoneNumber.isEmpty()
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
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Phone, contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Phone
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }),
                )

                Text(
                    text = if(isNumberOk.not()
                    ) "Invalid phone number.."
                    else "",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = LocalFontFamily.current,
                    modifier = Modifier.padding(6.dp)
                )

            }



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
                            title = "Continue",
                            shape = RoundedCornerShape(50),
                            enabled = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email)
                                .matches() &&
                                    password.isNotEmpty() && StringUtils.passwordRegex.matches(
                                password
                            ) && isNumberOk
                            ,
                            color = AppYellow,

                            contentColor = Color.White,
                            onClick = onDone,
                        )

                    }
                }
            }


        }
    }
}


@Preview
@Composable
private fun SignUpScreenPrev(
    @PreviewParameter(EmailPasswordAuthenticationFlowPreviewParameterProvider::class)
    uiState: EmailPasswordAuthenticationFlowUiState
) {
    FarmConnectTheme {
        SignUpScreen(
            uiState = uiState,
        ) { }
    }

}