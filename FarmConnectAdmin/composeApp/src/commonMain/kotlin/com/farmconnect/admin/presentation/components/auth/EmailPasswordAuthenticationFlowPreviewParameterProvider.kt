package com.farmconnect.core.ui.components.auth

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.farmconnect.core.ui.components.EmailPasswordAuthenticationFlowUiState
import com.farmconnect.core.util.AccountType
import com.farmconnect.core.util.Resource

class EmailPasswordAuthenticationFlowPreviewParameterProvider :
    PreviewParameterProvider<EmailPasswordAuthenticationFlowUiState> {
    override val values: Sequence<EmailPasswordAuthenticationFlowUiState>
        get() = sequenceOf(
            EmailPasswordAuthenticationFlowUiState(
                accountType = AccountType.Buyer,
                availableAccountType = AccountType.entries,
                userModes = listOf(
                    AccountType.Buyer,
                ),
                authenticationResource = Resource.Idle(),
            ), EmailPasswordAuthenticationFlowUiState(
                accountType = AccountType.Seller,
                userModes = AccountType.entries,
                availableAccountType = AccountType.entries,
                authenticationResource = Resource.Idle(),
            ), EmailPasswordAuthenticationFlowUiState(
                accountType = AccountType.Buyer,
                availableAccountType = AccountType.entries,
                userModes = listOf(
                    AccountType.Seller,
                ),
                authenticationResource = Resource.Loading(),
            ), EmailPasswordAuthenticationFlowUiState(
                accountType = AccountType.Buyer,
                availableAccountType = AccountType.entries,
                authenticationResource = Resource.Error("Test error"),
            ), EmailPasswordAuthenticationFlowUiState(
                accountType = AccountType.Buyer,
                availableAccountType = AccountType.entries,
                authenticationResource = Resource.Success(true),
            ), EmailPasswordAuthenticationFlowUiState(
                accountType = AccountType.Buyer,
                availableAccountType = AccountType.entries,
                authenticationResource = Resource.Success(false),
            )
        )
}