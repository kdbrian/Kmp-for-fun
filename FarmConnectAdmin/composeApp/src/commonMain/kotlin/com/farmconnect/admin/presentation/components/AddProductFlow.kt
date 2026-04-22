package com.farmconnect.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Drafts
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.components.textfields.FarmConnectOutlinedTextField
import com.farmconnect.core.ui.components.textfields.FarmConnectTextField
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.util.AlertDialogs
import kotlinx.coroutines.launch
import kotlin.random.Random

data class AddProductFlowUiState(
    val productName: String = LoremIpsum(2).values.joinToString(),
    val price: String = Random.nextInt(100, 999).toString(),
    val perishable: Boolean = false,
    val expirationDate: String = LoremIpsum(2).values.joinToString(),
    val priceRange: List<Double>? = null,
    val productDescription: String = LoremIpsum(24).values.joinToString(),
    val availableLocations: List<String> = emptyList(),
    val metaInfo: Map<String, String> = emptyMap(),
)

sealed class AddProductFlowEvent {
    class SelectLocation : AddProductFlowEvent()
}

object AddProductFlow {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProductEssentialDetailsInput(
        productName: String = "",
        onProductNameChange: (String) -> Unit = {},
        productDescription: String = "",
        onProductDescriptionChange: (String) -> Unit = {},
        price: String = "",
        onPriceChange: (String) -> Unit = {},
        perishable: Boolean = false,
        onPerishableChange: (Boolean) -> Unit = {},
        onDone: () -> Unit = {},
    ) {
        val displayCategories = remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Product Essential Details",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = LocalFontFamily.current,
            )

            Text(
                text = "Fill in essential details about your product",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = LocalFontFamily.current,
            )

            FarmConnectTextField(
                value = productName,
                onValueChange = onProductNameChange,
                placeholder = "Product Name",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            FarmConnectTextField(
                value = productDescription,
                onValueChange = onProductDescriptionChange,
                placeholder = "Product Description",
                singleLine = false,
                minLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() })
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable {
                        onPerishableChange(!perishable)
                    }) {
                Text(
                    text = buildAnnotatedString {
                        append("Indicate if your product is ")

                        withStyle(
                            SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Light
                            )
                        ) {
                            append("perishable.")
                        }

                        withStyle(
                            SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                fontStyle = FontStyle.Italic,
                                fontSize = 12.sp
                            )
                        ) {
                            append("\nLearn more in settings.")
                        }
                    }, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = perishable, onCheckedChange = onPerishableChange
                )
            }

            Column(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                FarmConnectTextField(
                    trailingIcon = {
                        Icon(
                            imageVector = if (displayCategories.value) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },
                    enabled = false,
                    placeholder = "select category",
                    modifier = Modifier.clickable {
                        displayCategories.value = !displayCategories.value
                    })

                DropdownMenu(
                    expanded = displayCategories.value,
                    onDismissRequest = { displayCategories.value = !displayCategories.value },
                ) {
                    repeat(4) {
                        DropdownMenuItem(text = {
                            Text(
                                text = "Item $it"
                            )
                        }, onClick = {
                            displayCategories.value = !displayCategories.value
                        })
                    }
                }
            }

            FarmConnectTextField(
                value = price, onValueChange = onPriceChange, placeholder = "Price", leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.AttachMoney, contentDescription = null
                    )
                }, keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Decimal
                ), keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )

            FarmConnectButton(
                title = "Select zones",
                color = Color(0xFF449DD1),
                contentColor = Color.White,
                onClick = onDone,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

    }

    @Composable
    fun ProductLocationInput(
        query: String = "",
        onQueryChange: (String) -> Unit = {},
        useMyLocation: Boolean = false,
        onUseMyLocationChange: (Boolean) -> Unit = {},
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {

                Text(
                    text = "Availability zones",
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = LocalFontFamily.current,
                )

                Text(
                    text = "Select areas where your product is available.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = LocalFontFamily.current,
                )

                FarmConnectOutlinedTextField(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(top = 16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null
                        )
                    },
                    placeholder = "Area name or address"
                )
            }

            item {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp
                            )
                        ) {
                            append("Tip\n")
                        }

                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        ) {
                            append("With more regions your product receives a wider audience.")
                        }

                    },
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .background(
                            color = Color(0xFFCBA135),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp),
                    fontFamily = LocalFontFamily.current
                )
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onUseMyLocationChange(!useMyLocation)
                    }
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Add my current location.\n")
                            }

                            withStyle(
                                SpanStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Light
                                )
                            ) {
                                append("Let people in your area know you are selling")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        fontFamily = LocalFontFamily.current,
                    )

                    Checkbox(
                        checked = useMyLocation,
                        onCheckedChange = onUseMyLocationChange
                    )

                }
            }

        }

    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SelectProductTimeLines(
        selectedDate: Long = System.currentTimeMillis(),
        onDateChange: (Long) -> Unit = {}
    ) {

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate,
            yearRange = 2025..2030,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= System.currentTimeMillis()
                }
            }
        )


        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            DatePicker(
                state = datePickerState,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                title = {
                    Text(
                        text = "Select an expiration date for your product",
                        style = MaterialTheme.typography.displaySmall,
                        fontFamily = LocalFontFamily.current
                    )
                },
                showModeToggle = false,
                headline = null,
                colors = DatePickerDefaults.colors(
                    containerColor = Color(0xFFF3EFE0),
                    selectedDayContainerColor = Color(0xFFCBA135),
                    weekdayContentColor = Color(0xFFFFC49B)
                ),
                dateFormatter = DatePickerDefaults.dateFormatter(
                )
            )


            FarmConnectButton(
                title = "Summary",
                color = Color(0xFF449DD1),
                contentColor = Color.White,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 12.dp),
            ){}

        }


    }


    @Composable
    fun ProductSummary(
        onConfirm: () -> Unit = {},
        uiState: AddProductFlowUiState = AddProductFlowUiState()
    ) {
        Column {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Product details",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = LocalFontFamily.current
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Name",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = LocalFontFamily.current
                        )

                        Text(
                            text = uiState.productName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = LocalFontFamily.current
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = LocalFontFamily.current
                        )

                        Text(
                            text = uiState.productName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = LocalFontFamily.current
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Price",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = LocalFontFamily.current
                        )

                        Text(
                            text = uiState.price,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = LocalFontFamily.current
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Regions",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = LocalFontFamily.current
                        )

                        Text(
                            text = buildAnnotatedString { append("10"); append(" "); append("added.") },
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = LocalFontFamily.current
                        )
                    }

                }

            }

            FarmConnectButton(
                title = "Save & Upload product"
            ){}

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductFlow(
    uiState: AddProductFlowUiState = AddProductFlowUiState(),
    onBack: () -> Unit = {}
) {

    val pageState = rememberPagerState { 4 }
    val alertUser by remember {
        derivedStateOf {
            pageState.canScrollBackward
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(alertUser) {
        val snackbarResult = snackbarHostState.showSnackbar(
            message = "Edit later?",
            actionLabel = "Save"
        )

        if (snackbarResult == SnackbarResult.ActionPerformed) {
            //save somewhere
            onBack()
        }
    }

    Scaffold(
        topBar = {
            FarmConnectElevatedAppBar(
                parameters = FarmConnectElevatedAppBarPreviewParameters(
                    title = buildAnnotatedString {
                        append("Add product")
                    },
                ),
                actions = {
                    Text(
                        text = "(${pageState.currentPage + 1}/${pageState.pageCount})",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = LocalFontFamily.current
                    )

                    AnimatedVisibility(
                        pageState.currentPage == pageState.pageCount - 1
                    ) {
                        IconButton(onClick = {
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Drafts,
                                contentDescription = stringResource(R.string.save_to_draft)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (pageState.canScrollForward)
                            coroutineScope.launch {
                                pageState.scrollToPage(pageState.currentPage.inc())
                            }

                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        AnimatedContent(
            targetState = alertUser,
            modifier = Modifier.padding(paddingValues)
        ) {
            when (it) {
                true -> BasicAlertDialog(
                    onDismissRequest = onBack, Modifier, DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = false,
                        usePlatformDefaultWidth = true
                    )
                ) {
                    AlertDialogs.FarmConnectBasicDialog(
                        title = "Discard draft?",
                    )
                }

                else -> OnboardingPagerPlaceholder(
                    pagerState = pageState,
                    pagerContent = {
                        when (pageState.currentPage) {
                            0 -> AddProductFlow.ProductEssentialDetailsInput(
                                onDone = {
                                    coroutineScope.launch {
                                        pageState.scrollToPage(1)
                                    }
                                }
                            )

                            1 -> AddProductFlow.ProductLocationInput()

                            2 -> AddProductFlow.SelectProductTimeLines()

                            3 -> AddProductFlow.ProductSummary()
                        }
                    },
                    userScrollEnabled = true
                )

            }
        }

    }


}

@Preview
@Composable
private fun AddProductFlowPrev() {
    val someVal = remember { mutableStateOf(false) }
    AnimatedContent(
        someVal.value
    ) {
        if (it) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        someVal.value = !someVal.value
                    }
            ) {
                Text(
                    text = "Add product",
                )
            }
        } else
            AddProductFlow(
                onBack = {
                    someVal.value = !someVal.value
                }
            )
    }
}