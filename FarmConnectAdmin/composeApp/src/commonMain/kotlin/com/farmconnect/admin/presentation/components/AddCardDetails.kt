package com.farmconnect.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.farmconnect.core.ui.components.textfields.FarmConnectOutlinedTextField
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.util.DateUtils
import com.farmconnect.core.ui.util.DateUtils.toFormattedDate
import com.farmconnect.core.util.StringUtils
import kotlin.random.Random


data class CardUiState(
    val cardNumber: String? = null,
    val expiration: Long,
    val cvc: String = Random.nextInt(100, 999).toString(),
    val ownerName: String,
) {
    companion object {
        val distinctCardUiStates: List<CardUiState> = listOf(
            // 1. ✅ Fully Valid State
            CardUiState(
                cardNumber = "4111222233334444",
                expiration = 1860000000000L,
                ownerName = "JOHN M. DOE"
            ),

            // 2. ⏳ Initial/Empty State
            CardUiState(
                cardNumber = null,
                expiration = 0L,
                ownerName = ""
            ),

            // 3. ⏳ Partial Card Number Input
            CardUiState(
                cardNumber = "411122",
                expiration = 1860000000000L,
                ownerName = "JOHN M. DOE"
            ),

            // 4. ❌ Card Number Too Long (e.g., 17 digits)
            CardUiState(
                cardNumber = "41112222333344445",
                expiration = 1860000000000L,
                ownerName = "JOHN M. DOE"
            ),

            // 5. ❌ Card Number Fails Luhn Check
            // (A 16-digit number that is structurally correct but fails the checksum)
            CardUiState(
                cardNumber = "4111222233334445",
                expiration = 1860000000000L,
                ownerName = "JOHN M. DOE"
            ),

            // 6. ❌ Expired Date Error
            CardUiState(
                cardNumber = "4111222233334444",
                expiration = 1609459200000L,
                ownerName = "JOHN M. DOE"
            ),

            // 7. ❌ Owner Name Missing/Blank
            CardUiState(
                cardNumber = "4111222233334444",
                expiration = 1860000000000L,
                ownerName = ""
            ),

            // 8. ❌ Owner Name with Invalid Characters (Example of uncleaned input)
            CardUiState(
                cardNumber = "4111222233334444",
                expiration = 1860000000000L,
                ownerName = "John Doe!@#"
            ),

            // 9. ⏳ Card Number with Spacing/Hyphens (Needs Cleaning)
            CardUiState(
                cardNumber = "4111-2222-3333-4444",
                expiration = 1860000000000L,
                ownerName = "JOHN M. DOE"
            )
        )
    }
}


class AddCardDetailsPreviewParameterProvider : PreviewParameterProvider<CardUiState> {
    override val values: Sequence<CardUiState>
        get() = CardUiState.distinctCardUiStates.asSequence()
}


@Composable
fun AddCardDetails(
    modifier: Modifier = Modifier,
    uiState: CardUiState = CardUiState.distinctCardUiStates.random(),
    onCardNumberUpdate: (String) -> Unit = {},
    onExpirationUpdate: (Long) -> Unit = {},
    onOwnerNameUpdate: (String) -> Unit = {},
    onCvvUpdate: (String) -> Unit = {}
) {


    val focusManager = LocalFocusManager.current


    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = "Holder Name",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = LocalFontFamily.current,
                )
            )
            FarmConnectOutlinedTextField(
                placeholder = "type here",
                shape = RectangleShape,
                isError = uiState.ownerName.isDigitsOnly(),
                value = uiState.ownerName.uppercase(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onValueChange = onOwnerNameUpdate,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = "Card Number",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = LocalFontFamily.current,
                )
            )
            FarmConnectOutlinedTextField(
                placeholder = "type here",
                shape = RectangleShape,
                isError = uiState.cardNumber?.isNotEmpty() == true && StringUtils.verifyCardNumberLuhn(
                    uiState.cardNumber
                ).not(),
                value = StringUtils.maskCardNumber(uiState.cardNumber ?: ""),
                onValueChange = onCardNumberUpdate,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.CreditCard,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
            )
        }


        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = "Expiration",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = LocalFontFamily.current,
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val dateSplit = uiState.expiration.toFormattedDate(
                    DateUtils.FORMAT_MONTH_YEAR
                ).split(' ')

                FarmConnectOutlinedTextField(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = "Month",
                    value = dateSplit[0],
                    shape = RectangleShape
                )
                FarmConnectOutlinedTextField(
                    modifier = Modifier.weight(.45f),
                    placeholder = "Year",
                    value = dateSplit[1],
                    shape = RectangleShape
                )
            }
        }


        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = "CvC",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = LocalFontFamily.current,
                )
            )
            FarmConnectOutlinedTextField(
                placeholder = "type here",
                shape = RectangleShape,
                value = uiState.cvc,
                onValueChange = onCvvUpdate,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
        }


    }
}

@Preview(
    showBackground = true
)
@Composable
private fun AddCardDetailsPrev(
    @PreviewParameter(
        AddCardDetailsPreviewParameterProvider::class
    )
    uiState: CardUiState
) {
    FarmConnectTheme {
        AddCardDetails(
            uiState = uiState
        )
    }
}