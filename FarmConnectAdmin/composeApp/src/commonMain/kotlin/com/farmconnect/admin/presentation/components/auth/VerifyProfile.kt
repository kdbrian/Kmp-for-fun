package com.farmconnect.core.ui.components.auth

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.components.FarmConnectButton
import com.farmconnect.core.ui.components.FarmConnectElevatedAppBar
import com.farmconnect.core.ui.components.FarmConnectElevatedAppBarPreviewParameters
import com.farmconnect.core.ui.components.textfields.FarmConnectTextField
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyProfile(
    codeRequestResource: Resource<Boolean> = Resource.Idle(),
    codeVerificationResource: Resource<Boolean> = Resource.Idle(),
    onCodeRequest: (String) -> Unit = {},
    onCodeVerify: (String, String) -> Unit = { _, _ -> },
    onBack: () -> Unit = { },
) {

    val phoneNumber = rememberTextFieldState()
    val otpCode = rememberTextFieldState()
    val isValidPhoneNumber = remember(phoneNumber, phoneNumber.text.toString()) {
        phoneNumber.text.toString()
            .isNotEmpty() &&
                phoneNumber.text.toString().length == 10
    }

    val isCodeValid = remember(
        phoneNumber,
        codeRequestResource,
        otpCode
    ) {
        derivedStateOf {
            phoneNumber.text.toString().isNotEmpty() &&
                    codeRequestResource is Resource.Success &&
                    otpCode.text.toString().length == 6
        }
    }
    val focusManager = LocalFocusManager.current

    BackHandler(true, onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        FarmConnectElevatedAppBar(
            parameters = FarmConnectElevatedAppBarPreviewParameters(
                title = buildAnnotatedString {
                    append("Verify Profile")
                },
                centerTitle = true
            ),
            navigationIcon = {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.background(
                        Color.DarkGray.copy(.45f),
                        CircleShape
                    )
                ) {
                    Icon(
                        Icons.Rounded.ChevronLeft,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        )

        AnimatedContent(
            targetState = codeRequestResource is Resource.Success
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!it) {


                    Text(
                        "Submit your phone number for verification.",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = LocalFontFamily.current,
                            fontWeight = FontWeight.Light
                        ),
                    )

                    FarmConnectTextField(
                        value = phoneNumber.text.toString(),
                        onValueChange = phoneNumber::setTextAndSelectAll,
                        placeholder = "Phone Number",
                        isError = !isValidPhoneNumber,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        leadingIcon = {
                            Text(
                                "\uD83C\uDDF0\uD83C\uDDEA",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    )

                    AnimatedVisibility(
                        visible = !isValidPhoneNumber,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = "Valid number format is +254(1/7) 23...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Light,
                                fontFamily = LocalFontFamily.current,
                                color = Color.Red
                            )
                        )
                    }

                    AnimatedContent(
                        targetState = codeRequestResource is Resource.Loading
                    ) {
                        if (it)
                            CircularProgressIndicator()
                        else
                            FarmConnectButton(
                                title = "Send Code",
                                color = LocalPrimaryColor.current,
                                contentColor = Color.White,
                                onClick = { onCodeRequest(phoneNumber.text.toString()) }
                            )
                    }
                } else {


                    Text(
                        "Enter OTP code you received.",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = LocalFontFamily.current,
                            fontWeight = FontWeight.Light
                        ),
                    )


                    BasicTextField(
                        value = otpCode.text.toString(),
                        enabled = codeVerificationResource !is Resource.Success && codeRequestResource is Resource.Success && otpCode.text.toString().length in 0..6,
                        onValueChange = otpCode::setTextAndPlaceCursorAtEnd,
                        decorationBox = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                repeat(6) { index ->
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .border(
                                                width = 2.dp,
                                                color = animateColorAsState(if (otpCode.text.toString().length > index) LocalPrimaryColor.current else Color.LightGray).value,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = otpCode.text.toString().getOrNull(index)
                                                ?.toString() ?: "",
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.W400,
                                                fontFamily = LocalFontFamily.current
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    )


                    AnimatedContent(
                        codeVerificationResource is Resource.Loading
                    ) {
                        if (it)
                            CircularProgressIndicator()
                        else
                            FarmConnectButton(
                                title = "Verify Code",
                                enabled = isCodeValid.value,
                                color = LocalPrimaryColor.current,
                                contentColor = Color.White,
                                onClick = {
                                    if (otpCode.text.toString().length == 6)
                                        onCodeVerify(
                                            phoneNumber.text.toString(),
                                            otpCode.text.toString()
                                        )
                                }
                            )
                    }
                }
            }
        }
    }
}

class VerifyProfilePreviewParameterProvider :
    PreviewParameterProvider<Pair<Resource<Boolean>, Resource<Boolean>>> {
    override val values: Sequence<Pair<Resource<Boolean>, Resource<Boolean>>>
        get() = sequenceOf(
            Resource.Success(true) to Resource.Loading(),
            Resource.Success(true) to Resource.Idle(),
            Resource.Idle<Boolean>() to Resource.Idle(),
            Resource.Loading<Boolean>() to Resource.Idle(),
            Resource.Error<Boolean>("Invalid phone number format") to Resource.Idle(),
        )
}


@Preview(showBackground = true)
@Composable
fun VerifyProfilePrev(
    @PreviewParameter(VerifyProfilePreviewParameterProvider::class)
    data: Pair<Resource<Boolean>, Resource<Boolean>>
) {
    FarmConnectTheme {
        VerifyProfile(
            codeRequestResource = data.first,
            codeVerificationResource = data.second
        )
    }
}
