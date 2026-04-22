package com.farmconnect.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.util.Resource
import com.farmconnect.core.util.StringUtils
import kotlinx.coroutines.delay

internal object PhoneAuthenticationFlow {
    @Composable
    fun PhoneNumberInput(
        modifier: Modifier = Modifier,
        onTextChange: (String) -> Unit = {},
        text: String = "1234",
        codeSentResource: Resource<Boolean> = Resource.Idle(),
        onDone: () -> Unit = {}
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "Enter Phone Number",
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = LocalFontFamily.current,
                )

                Text(
                    text = "Please enter your phone number. You'll receive a 4 digit code to verify next.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = LocalFontFamily.current,
                )
            }

            Column {
                BasicText(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = 32.sp
                            )
                        ) {
                            append("+ 254")
                        }

                        append("  ")

                        withStyle(
                            SpanStyle(
                                fontSize = 32.sp,
                                letterSpacing = 8.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color.LightGray
                            )
                        ) {
                            append(text)
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(top = 32.dp),
                )
            }

            Column {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    items(
                        count = 9, key = { it }) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .padding(8.dp)
                                .background(color = Color.Red, shape = RoundedCornerShape(24.dp))
                                .clickable {
                                    onTextChange((it + 1).toString())
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (it + 1).toString(),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(8.dp),
                                fontFamily = LocalFontFamily.current,
                            )
                        }
                    }

                    item {
                        IconButton(onClick = {
                            onTextChange("⌫")
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Backspace,
                                contentDescription = null,
                                modifier = Modifier.size(45.dp)
                            )
                        }
                    }


                    item {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .padding(8.dp)
                                .background(color = Color.Red, shape = RoundedCornerShape(24.dp))
                                .clickable {
                                    onTextChange("0")
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(8.dp),
                                fontFamily = LocalFontFamily.current,
                            )
                        }
                    }


                    item {
                        IconButton(onClick = onDone) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(45.dp)
                            )
                        }
                    }
                }
            }

        }

    }

    @Composable
    fun EnterOneTimePin(
        modifier: Modifier = Modifier,
        value: String = "12",
        onValueChange: (String) -> Unit = {},
        onDone: () -> Unit = {}
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Enter One Time Pin",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = LocalFontFamily.current,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Text(
                text = "Check your inbox for the OTP sent.",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = LocalFontFamily.current,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            BasicTextField(
                value = value, onValueChange = onValueChange, decorationBox = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(4) {
                            key(it) {
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .background(
                                            color = Color.Red, shape = RoundedCornerShape(24.dp)
                                        ), contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (it < value.length) value[it].toString()
                                        else "",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 16.dp, vertical = 8.dp
                                            )
                                            .padding(16.dp),
                                        fontFamily = LocalFontFamily.current,
                                    )
                                }
                            }
                        }
                    }
                })

            Spacer(Modifier.padding(32.dp))

            FarmConnectButton(
                title = "Continue", modifier = Modifier.padding(horizontal = 24.dp)
            ) {}
        }
    }

    @Composable
    fun VerificationResults(
        modifier: Modifier = Modifier,
        onDone: () -> Unit = {},
        codeVerificationResource: Resource<Boolean> = Resource.Idle(),
    ) {
        val timeTopProceed = remember { mutableIntStateOf(30) }

        LaunchedEffect(codeVerificationResource) {
            if (codeVerificationResource is Resource.Success) {
                while (true) {
                    if (timeTopProceed.intValue == 0) {
                        onDone()
                        break
                    }

                    timeTopProceed.intValue--
                    delay(1000)
                }
            }
        }

        AnimatedContent(targetState = codeVerificationResource) {
            when (it) {
                is Resource.Error<*> -> {
                    Text(
                        text = "Error : ${it.message}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = LocalFontFamily.current
                    )
                }

                is Resource.Idle<*> -> {
                    Text(text = "Idle")
                }

                is Resource.Loading<*> -> {
                    CircularProgressIndicator()
                }

                is Resource.Success<*> -> {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontSize = 32.sp, color = Color.Green
                                )
                            ) {
                                append("Success\n")
                            }

                            withStyle(
                                SpanStyle(
                                    fontSize = 18.sp, fontWeight = FontWeight.Light
                                )
                            ) {
                                append("Tap to continue\n")

                                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                                    append("Will proceed in ${timeTopProceed.value} seconds")
                                }
                            }
                        }, modifier = modifier.clickable(onClick = onDone)
                    )
                }
            }
        }

    }

}


data class PhoneAuthenticationFlowUiState(
    val phoneNumber: String = "",
    val code: String = "",
    val isAuthenticated: Boolean = false,
    val codeSentResource: Resource<Boolean> = Resource.Idle(),
    val codeVerificationResource: Resource<Boolean> = Resource.Idle(),
)

sealed class PhoneAuthenticationFlowUiEvent {
    /*
    * we are receiving number input per character azin if user types 1 we recieve 1 not plus all rest or IDK
    * */
    data class OnPhoneNumberChange(val value: String) : PhoneAuthenticationFlowUiEvent()
    data class OnCodeChange(val value: String) : PhoneAuthenticationFlowUiEvent()
    object OnDone : PhoneAuthenticationFlowUiEvent()
    object OnRequestOtp : PhoneAuthenticationFlowUiEvent()
    object OnVerifyOtp : PhoneAuthenticationFlowUiEvent()
}


@Composable
fun PhoneAuthenticationFlow(
    uiState: PhoneAuthenticationFlowUiState = PhoneAuthenticationFlowUiState(),
    onEvent: (PhoneAuthenticationFlowUiEvent) -> Unit = {}
) {

    val pagerState = rememberPagerState { 3 }
    val coroutineScope = rememberCoroutineScope()

    OnboardingPagerPlaceholder(
        pagerState = pagerState,
        userScrollEnabled = false,
        pagerContent = {
            AnimatedContent(
                targetState = uiState.isAuthenticated
            ) {
                when (it) {
                    true -> {
                        Text(
                            text = "Authenticated",
                            fontFamily = LocalFontFamily.current,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    false -> {
                        when (pagerState.currentPage) {
                            0 -> PhoneAuthenticationFlow.PhoneNumberInput(onTextChange = {
                                onEvent(PhoneAuthenticationFlowUiEvent.OnPhoneNumberChange(it))
                            }, text = uiState.phoneNumber, onDone = {
                                if (StringUtils.kenyanRegex.matches("+254${uiState.phoneNumber}")) onEvent(
                                    PhoneAuthenticationFlowUiEvent.OnRequestOtp
                                )
                            })

                            1 -> PhoneAuthenticationFlow.EnterOneTimePin(
                                value = uiState.code,
                                onValueChange = {
                                    onEvent(
                                        PhoneAuthenticationFlowUiEvent.OnCodeChange(
                                            it
                                        )
                                    )
                                },
                                onDone = {
                                    if (uiState.code.length == 6) {
                                        onEvent(PhoneAuthenticationFlowUiEvent.OnVerifyOtp)
                                    }
                                })

                            2 -> PhoneAuthenticationFlow.VerificationResults(
                                codeVerificationResource = uiState.codeVerificationResource,
                                onDone = {

                                })
                        }
                    }
                }
            }
        },
    )

}

@Preview
@Composable
private fun PhoneAuthenticationFlowPrev() {
    PhoneAuthenticationFlow()
}