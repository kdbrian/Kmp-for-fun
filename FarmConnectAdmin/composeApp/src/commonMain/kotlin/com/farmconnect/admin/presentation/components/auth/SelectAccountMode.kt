package com.farmconnect.core.ui.components.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.components.EmailPasswordAuthenticationFlowEvent
import com.farmconnect.core.ui.components.EmailPasswordAuthenticationFlowUiState
import com.farmconnect.core.ui.components.EmptyListPlaceholder
import com.farmconnect.core.ui.components.EmptyListPlaceholderUiState
import com.farmconnect.core.ui.components.SettingsRowItem
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.util.AccountType
import com.farmconnect.core.util.Resource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAccountMode(
    modifier: Modifier = Modifier,
    emailPasswordAuthenticationFlowUiState: EmailPasswordAuthenticationFlowUiState = EmailPasswordAuthenticationFlowUiState(),
    onEvent: (EmailPasswordAuthenticationFlowEvent) -> Unit = {}
) {


    emailPasswordAuthenticationFlowUiState.run {
        AnimatedContent(
            targetState = authenticationResource is Resource.Loading,
            modifier = modifier
                .safeDrawingPadding()
        ) {
            if (it)
                Surface(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    shadowElevation = 12.dp,
                ) {
                    BasicAlertDialog(
                        onDismissRequest = {},
                        properties = DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false
                        ),

                        ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Processing request please wait.",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = LocalFontFamily.current
                                )
                            )

                            CircularProgressIndicator()

                        }
                    }
                }
            else
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.user_account),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Select Account Mode",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = LocalFontFamily.current
                        )
                    )

                    Spacer(
                        Modifier.padding(16.dp)
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .dropShadow(
                                shape = RoundedCornerShape(18.dp),
                                shadow = Shadow(
                                    radius = 12.dp,
                                    color = Color.White,
                                )
                            )
                            .dropShadow(
                                shape = RoundedCornerShape(24.dp),
                                shadow = Shadow(
                                    radius = 16.dp,
                                    color = Color.LightGray.copy(.65f)
                                )
                            ),
                        shape = RoundedCornerShape(18.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (authenticationResource is Resource.Success && userModes.isEmpty()) {
                                EmptyListPlaceholder(
                                    uiState = EmptyListPlaceholderUiState(
                                        image = R.drawable.no_account,
                                        text = "No account linked"
                                    ),
                                    modifier = Modifier.padding(16.dp),
                                )
                            } else if (authenticationResource is Resource.Success)
                                userModes.forEachIndexed { index, type ->
                                    key(index) {
                                        SettingsRowItem(
                                            endIcon = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Rounded.ArrowRight,
                                                    contentDescription = null
                                                )
                                            },
                                            title = buildAnnotatedString {
                                                append("${type.name} account found.")
                                                append('\n')

                                                withStyle(
                                                    SpanStyle(
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Light
                                                    )
                                                ) {
                                                    append(type.description)
                                                }
                                            },
                                            onClick = {
                                                onEvent(
                                                    EmailPasswordAuthenticationFlowEvent.AccountTypeChanged(
                                                        type
                                                    )
                                                )
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                        )
                                    }

                                    if (index != userModes.lastIndex)
                                        HorizontalDivider(
                                            Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 4.dp
                                            )
                                        )
                                }
                            else
                                AccountType.entries.forEachIndexed { index, type ->
                                    key(index) {
                                        SettingsRowItem(
                                            endIcon = {
                                                AnimatedContent(
                                                    targetState = type == accountType &&
                                                            authenticationResource is Resource.Loading
                                                ) {
                                                    when (it) {
                                                        true -> CircularProgressIndicator(
                                                            color = Color.White,
                                                            trackColor = LocalPrimaryColor.current
                                                        )

                                                        false -> Icon(
                                                            imageVector = Icons.AutoMirrored.Rounded.ArrowRight,
                                                            contentDescription = null
                                                        )
                                                    }
                                                }
                                            },
                                            title = buildAnnotatedString {
                                                append("${type.name} account.")
                                                append('\n')

                                                withStyle(
                                                    SpanStyle(
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Light
                                                    )
                                                ) {
                                                    append(type.description)
                                                }
                                            },
                                            onClick = {
                                                onEvent(
                                                    EmailPasswordAuthenticationFlowEvent.AccountTypeChanged(
                                                        type
                                                    )
                                                )
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                        )

                                        if (index < AccountType.entries.size - 1)
                                            HorizontalDivider(
                                                Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 4.dp
                                                )
                                            )
                                    }

                                }
                        }
                    }

                }

        }
    }


}


@Preview
@Composable
private fun SelectAccountModePrev(
    @PreviewParameter(EmailPasswordAuthenticationFlowPreviewParameterProvider::class)
    uiState: EmailPasswordAuthenticationFlowUiState
) {
    FarmConnectTheme {
        SelectAccountMode(
            emailPasswordAuthenticationFlowUiState = uiState
        )
    }
}