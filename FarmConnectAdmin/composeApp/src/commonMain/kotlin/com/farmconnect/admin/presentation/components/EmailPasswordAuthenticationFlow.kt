package com.farmconnect.core.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmconnect.core.ui.components.auth.EmailPasswordAuthenticationFlowPreviewParameterProvider
import com.farmconnect.core.ui.components.auth.PasswordReset
import com.farmconnect.core.ui.components.auth.SelectAccountMode
import com.farmconnect.core.ui.components.auth.SignInScreen
import com.farmconnect.core.ui.components.auth.SignUpScreen
import com.farmconnect.core.ui.components.onboarding.OnboardingScreen
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.util.AccountType
import com.farmconnect.core.util.Resource
import kotlinx.coroutines.launch

data class EmailPasswordAuthenticationFlowUiState(
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val userModes: List<AccountType> = emptyList(),
    val accountType: AccountType = AccountType.Buyer,
    val accountMode: AccountType? = null,
    val availableAccountType: List<AccountType> = AccountType.entries,
    val passwordVisibility: Boolean = false,
    val authenticationResource: Resource<Boolean> = Resource.Idle()
)

interface EmailPasswordAuthenticationFlowEvent {
    data class EmailChanged(val email: String) : EmailPasswordAuthenticationFlowEvent

    data class PhoneNumberChanged(val phoneNumber: String) : EmailPasswordAuthenticationFlowEvent

    data class PasswordChanged(val password: String) : EmailPasswordAuthenticationFlowEvent
    data class PasswordVisibilityToggled(val passwordVisibility: Boolean) :
        EmailPasswordAuthenticationFlowEvent

    data object ResetPassword : EmailPasswordAuthenticationFlowEvent
    data object SignIn : EmailPasswordAuthenticationFlowEvent
    data object PasskeySignIn : EmailPasswordAuthenticationFlowEvent
    data object PasskeySignUp : EmailPasswordAuthenticationFlowEvent
    data class AccountTypeChanged(val accountType: AccountType) :
        EmailPasswordAuthenticationFlowEvent

    data object CreateAccount : EmailPasswordAuthenticationFlowEvent
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailPasswordAuthenticationFlow(
    uiState: EmailPasswordAuthenticationFlowUiState = EmailPasswordAuthenticationFlowUiState(),
    onEvent: (EmailPasswordAuthenticationFlowEvent) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 5 }

    BackHandler(
        enabled = pagerState.canScrollBackward
    ) {
        coroutineScope.launch {
            pagerState.scrollToPage(pagerState.currentPage.dec())
        }
    }

    val scrollPage: (Int) -> Unit = { index ->
        if (index in 0..pagerState.pageCount)
            coroutineScope.launch {
                pagerState.scrollToPage(index)
            }
    }


    uiState.run {
        OnboardingPagerPlaceholder(
            pagerState = pagerState,
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize(),
            userScrollEnabled = false,
            pagerContent = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxSize()
                ) {
                    AnimatedVisibility(
                        pagerState.canScrollBackward,
                        modifier = Modifier.fillMaxWidth(),
                        enter = slideInVertically(),
                        exit = fadeOut(),
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            AnimatedVisibility(
                                pagerState.canScrollBackward,
                                enter = slideInHorizontally(),
                                modifier = Modifier.padding(end = 12.dp),
                                exit = fadeOut(),
                            ) {
                                IconButton(
                                    onClick = {
                                        scrollPage(pagerState.currentPage.dec())
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = null
                                    )

                                }
                            }

//                            if (pagerState.currentPage > 1)
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .horizontalScroll(rememberScrollState()),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                repeat(pagerState.pageCount) { index ->
                                    val isSelected = pagerState.currentPage == index
                                    Column(
                                        modifier = Modifier
                                            .clickable(true) {
                                                scrollPage(index)
                                            }
                                            .padding(8.dp)
                                            .padding(vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = when (index) {
                                                0 -> ""
                                                1 -> "Sign In"
                                                2 -> "Sign Up"
                                                3 -> "Reset pasword"
                                                4 -> if (pagerState.currentPage == 4) "Account Mode" else ""
                                                else -> ""
                                            },
                                            style = MaterialTheme.typography.displaySmall.copy(
                                                fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W300,
                                                color = animateColorAsState(
                                                    if (isSelected) Color.DarkGray else Color(
                                                        0xFF8E8E93
                                                    )
                                                ).value,
                                                fontSize = 14.sp,
                                                fontFamily = LocalFontFamily.current,
                                            ),
                                            modifier = Modifier.padding(
                                                bottom = animateDpAsState(
                                                    if (isSelected)
                                                        6.dp else 0.dp
                                                ).value
                                            )
                                        )

                                        if (index > 0)
                                            Spacer(
                                                modifier = Modifier
                                                    .height(4.dp)
                                                    .requiredWidth(70.dp)
                                                    .background(
                                                        color = animateColorAsState(
                                                            if (isSelected) LocalPrimaryColor.current else Color.White
                                                        ).value,
                                                        shape = RoundedCornerShape(50)
                                                    )
                                            )


                                    }
                                }
                            }
                        }
                    }

                    when (pagerState.currentPage) {
                        0 -> OnboardingScreen(
                            onGetStarted = {
                                scrollPage(
                                    2
                                )
                            }, onSignIn = {
                                scrollPage(
                                    1
                                )
                            }, uiState = uiState
                        )

                        1 -> SignInScreen(
                            uiState = uiState,
                            onEmailChange = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.EmailChanged(it))
                            },
                            onPasswordChange = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.PasswordChanged(it))
                            },
                            passwordVisibility = uiState.passwordVisibility,
                            onPasswordReset = {
                                scrollPage(
                                    3
                                )
                            },
                            onPasswordVisibilityToggle = {
                                onEvent(
                                    EmailPasswordAuthenticationFlowEvent.PasswordVisibilityToggled(
                                        it
                                    )
                                )
                            },
                            onDone = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.SignIn)
                            },
                            onPasskeySignIn = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.PasskeySignIn)
                            },
                            onSetUpAccount = {
                                coroutineScope.launch {
                                    scrollPage(
                                        2
                                    )
                                }
                            })

                        2 -> SignUpScreen(
                            uiState = uiState,
                            onEmailChange = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.EmailChanged(it))
                            },
                            onAccountTypeChange = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.AccountTypeChanged(it))
                            },
                            onPasswordChange = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.PasswordChanged(it))
                            },
                            onPhoneNumberChange = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.PhoneNumberChanged(it))
                            },
                            passwordVisibility = uiState.passwordVisibility,
                            onPasswordVisibilityToggle = {
                                onEvent(
                                    EmailPasswordAuthenticationFlowEvent.PasswordVisibilityToggled(
                                        it
                                    )
                                )
                            },
                            onDone = {
                                scrollPage(
                                    4
                                )
                            },
                            onPasskeySignUp = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.PasskeySignUp)
                            },
                        )

                        3 -> PasswordReset(
                            uiState = uiState,
                            onEmailChange = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.EmailChanged(it))
                            },
                            onDone = {
                                onEvent(EmailPasswordAuthenticationFlowEvent.ResetPassword)
                            },
                        )

                        4 -> {
                            SelectAccountMode(
                                emailPasswordAuthenticationFlowUiState = uiState,
                                onEvent = {
                                    when (it) {
                                        is EmailPasswordAuthenticationFlowEvent.AccountTypeChanged -> {
                                            onEvent(
                                                EmailPasswordAuthenticationFlowEvent.CreateAccount
                                            )
                                        }
                                    }
                                }
                            )
                        }


                    }
                }
            },
        )
    }

}

@Preview
@Composable
private fun EmailPasswordAuthenticationFlowPrev(
    @PreviewParameter(EmailPasswordAuthenticationFlowPreviewParameterProvider::class) uiState: EmailPasswordAuthenticationFlowUiState = EmailPasswordAuthenticationFlowUiState()
) {
    FarmConnectTheme {
        EmailPasswordAuthenticationFlow(
            uiState = uiState
        )
    }
}