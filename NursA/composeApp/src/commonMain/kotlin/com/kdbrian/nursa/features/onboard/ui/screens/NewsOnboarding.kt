package com.kdbrian.nursa.features.onboard.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kdbrian.nursa.features.newsapp.domain.model.NewsData
import com.kdbrian.nursa.features.onboard.ui.theme.Geom
import com.kdbrian.nursa.features.onboard.ui.theme.LocalOnPrimaryColor
import com.kdbrian.nursa.features.onboard.ui.theme.LocalPrimaryColor
import com.kdbrian.nursa.features.onboard.ui.theme.PlatyPi
import io.github.aakira.napier.Napier
import nursa.composeapp.generated.resources.Res
import nursa.composeapp.generated.resources.bg
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock

@Composable
fun NewsOnboarding(
    modifier: Modifier = Modifier,
    topNews: List<NewsData> = emptyList(),
    supportedCountries: List<Pair<String, String>> = emptyList(),
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onCountryChange: (Int) -> Unit = {},
    selectedCountry: Int = 0
) {

    var isRegionsExpanded by remember { mutableStateOf(false) }

    val windowInfo = LocalWindowInfo.current
    var selectedNewsData by remember {
        mutableStateOf<NewsData?>(null)
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    LaunchedEffect(selectedNewsData) {
        selectedNewsData?.let {
            drawerState.open()
        } ?: run {
            drawerState.close()
        }
    }

    val focusManager = LocalFocusManager.current
    val searchFR = FocusRequester()

    ModalNavigationDrawer(
        modifier = Modifier
            .focusable()
            .onPreviewKeyEvent { event ->
                Napier.d("Event: $event ${event.key} ${event.isCtrlPressed  }")
                if (
                    event.type == KeyEventType.KeyDown &&
                    event.isCtrlPressed &&
                    event.key == androidx.compose.ui.input.key.Key.Spacebar
                ) {
                    searchFR.requestFocus()
                    true
                } else {
                    false
                }
            }
        ,
        drawerState = drawerState,
        drawerContent = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .fillMaxHeight(),
                color = LocalPrimaryColor.current,
                contentColor = LocalOnPrimaryColor.current
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = selectedNewsData?.image,
                        error = painterResource(Res.drawable.bg),
                        placeholder = painterResource(Res.drawable.bg),
                        modifier = Modifier.fillMaxWidth(.5f)
                            .fillMaxHeight()
                            .then(
                                if (selectedNewsData?.image?.isEmpty() == true)
                                    Modifier.blur(12.dp)
                                else Modifier
                            ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    VerticalDivider(
                        Modifier.fillMaxHeight().width(2.dp)
                            .padding(vertical = 8.dp),
                        color = LocalOnPrimaryColor.current
                    )

                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(8.dp)
                            .verticalScroll(
                                rememberScrollState()
                            ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {

                        IconButton(onClick = {
                            selectedNewsData = null
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Text(
                            selectedNewsData?.title.toString(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = Geom,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )

                        Text(
                            selectedNewsData?.summary ?: selectedNewsData?.text ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = Geom,
                            color = Color.White,
                        )

                    }
                }
            }
        },
        gesturesEnabled = false
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.fillMaxSize()
                ,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            stickyHeader {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent.copy(.3f),
                                    LocalPrimaryColor.current.copy(.4f),
                                )
                            ),
                            RectangleShape
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        OutlinedTextField(
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = Geom
                            ),
                            value = query,
                            onValueChange = onQueryChange,
                            modifier = Modifier
                                .focusRequester(searchFR)

                                .fillMaxWidth(.65f).dropShadow(
                                    shape = RoundedCornerShape(50.dp),
                                    shadow = Shadow(
                                        color = LocalPrimaryColor.current,
                                        radius = 16.dp
                                    ),
                                ),
                            shape = RoundedCornerShape(50.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Search here",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = Geom,
                                    color = Color.White
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White.copy(.7f),
                                unfocusedBorderColor = Color.White.copy(.7f),
                                focusedContainerColor = LocalPrimaryColor.current.copy(.7f),
                                unfocusedContainerColor = LocalPrimaryColor.current.copy(.7f),
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.White,
//                        focusedTextColor = LocalOnPrimaryColor.current,
//                        unfocusedTextColor = LocalOnPrimaryColor.current,
//                        focusedLeadingIconColor = Color.White,
//                        unfocusedLeadingIconColor = Color.White
                            )
                        )

                        Spacer(Modifier.weight(1f))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {

                            //region dropdown
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(
                                        LocalPrimaryColor.current,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                            ) {

                                Row(
                                    modifier = Modifier
                                        .padding(end = 6.dp)
                                        .clickable(true) {
                                            isRegionsExpanded = !isRegionsExpanded
                                        },
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = supportedCountries[selectedCountry].first,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontFamily = Geom,
                                        modifier = Modifier,
                                    )

                                    Icon(
                                        imageVector = if (isRegionsExpanded) Icons.Rounded.ArrowDropDown else Icons.Rounded.ArrowDropDown,
                                        contentDescription = null,
                                        tint = LocalOnPrimaryColor.current,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = isRegionsExpanded,
                                    onDismissRequest = {
                                        isRegionsExpanded = false
                                    },
                                    containerColor = LocalPrimaryColor.current,
                                    shadowElevation = 12.dp,
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    supportedCountries.forEachIndexed { index, pair ->
                                        Text(
                                            pair.first,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontFamily = Geom,
                                            color = LocalOnPrimaryColor.current,
                                            modifier = Modifier.fillMaxWidth().clickable(true) {
                                                onCountryChange(index)
                                                isRegionsExpanded = !isRegionsExpanded
                                            }.padding(
                                                horizontal = 12.dp,
                                                vertical = 4.dp
                                            )

                                        )


                                        if (it != supportedCountries.size - 1)
                                            HorizontalDivider(
                                                color = LocalOnPrimaryColor.current,
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp
                                                )
                                            )


                                    }
                                }

                            }
                        }


                    }

                    Spacer(Modifier.padding(8.dp))

                    Text(
                        LoremIpsum(4).values.joinToString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = PlatyPi,
                        color = LocalOnPrimaryColor.current,
                        modifier = Modifier.fillMaxSize()
                            .background(
                                LocalPrimaryColor.current,
                                RectangleShape
                            )
                            .padding(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(6) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            color = LocalOnPrimaryColor.current,
                                            CircleShape
                                        )
                                        .padding(6.dp)
                                )
                            }
                        }

                        Text(
                            text = Clock.System.now().toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(3.dp)
                                .background(
                                    Color.White.copy(.45f),
                                    RectangleShape
                                )
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                ),
                        )
                    }
                }

            }


            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                FlowRow(
                    maxItemsInEachRow = 8,
                    maxLines = 3,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth().padding(6.dp)
                ) {
                    topNews.mapNotNull {
                        it.category
                    }.distinct().forEach {
                        Text(
                            it.capitalize(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = PlatyPi,
                            color = LocalOnPrimaryColor.current,
                            modifier = Modifier
                                .padding(2.dp)
                                .background(
                                    LocalPrimaryColor.current,
                                    RoundedCornerShape(50)
                                )
                                .padding(6.dp)
                        )
                    }
                }

            }

            item(
                span = {
                    GridItemSpan(maxCurrentLineSpan)
                }
            ) {
                val newsData = topNews.randomOrNull()
                AnimatedVisibility(newsData?.summary?.isNotEmpty() == true) {
                    Text(
                        newsData?.summary.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = Geom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        LocalPrimaryColor.current.copy(.5f),
                                        Color.White.copy(.7f),
                                        Color.White.copy(.15f)
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .dropShadow(
                                shape = RoundedCornerShape(8.dp),
                                shadow = Shadow(
                                    color = LocalPrimaryColor.current,
                                    radius = 16.dp
                                )
                            )
                            .padding(8.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (
                topNews.isEmpty()
            )
                items(12) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                LocalPrimaryColor.current,
                                RectangleShape
                            )
                    ) {

                        Image(
                            painter = painterResource(Res.drawable.bg),
                            modifier = Modifier.fillMaxWidth().height(100.dp).blur(12.dp),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                            )
                                .padding(bottom = 8.dp)
                        ) {
                            Box(
                                Modifier.fillMaxWidth()
                                    .height(8.dp)
                                    .background(
                                        LocalOnPrimaryColor.current,
                                        RectangleShape
                                    )
                                    .padding(4.dp)
                            )
                            Spacer(Modifier.padding(8.dp))

                            Text(
                                LoremIpsum(8).values.joinToString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = Geom,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                maxLines = 2,
                                overflow = TextOverflow.Clip
                            )
                        }

                    }
                }
            else if (
                topNews.isNotEmpty()
            ) {

                items(topNews) { newsData ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                LocalPrimaryColor.current,
                                RectangleShape
                            )
                            .clickable(true) {
                                selectedNewsData = newsData
                            }
                    ) {

                        AsyncImage(
                            model = newsData.image,
                            error = painterResource(Res.drawable.bg),
                            placeholder = painterResource(Res.drawable.bg),
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentDescription = null,
                            colorFilter = if (newsData.image.isNullOrEmpty()) ColorFilter.tint(
                                blendMode = BlendMode.Xor,
                                color = Color.LightGray
                            ) else null,
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                            )
                                .padding(bottom = 8.dp)
                        ) {
                            Box(
                                Modifier.fillMaxWidth()
                                    .height(8.dp)
                                    .background(
                                        LocalOnPrimaryColor.current,
                                        RectangleShape
                                    )
                                    .padding(4.dp)
                            )
                            Spacer(Modifier.padding(8.dp))

                            newsData.title?.let {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontFamily = Geom,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    maxLines = 2,
                                    overflow = TextOverflow.Clip
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {

                                newsData.authors?.let {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier
                                            .background(
                                                color = Color(0xFFECCE8E),
                                                shape = RoundedCornerShape(50)
                                            )
                                            .padding(
                                                horizontal = 12.dp,
                                                vertical = 4.dp
                                            ),
                                    ) {
                                        it.forEach {
                                            Text(
                                                it,
                                                style = MaterialTheme.typography.bodySmall,
                                                fontFamily = Geom,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White,
                                                maxLines = 2,
                                                overflow = TextOverflow.Clip
                                            )
                                        }
                                    }
                                } ?: run {
                                    newsData.author?.let {
                                        Text(
                                            it,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontFamily = Geom,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                            modifier = Modifier
                                                .background(
                                                    color = Color(0xFFECCE8E),
                                                    shape = RoundedCornerShape(50)
                                                )
                                                .padding(
                                                    horizontal = 12.dp,
                                                    vertical = 4.dp
                                                ),
                                            maxLines = 2,
                                            overflow = TextOverflow.Clip
                                        )
                                    }
                                }


                                newsData.publishDate?.let {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = Geom,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        modifier = Modifier
                                            .background(
                                                color = Color.Yellow.copy(.56f),
                                                shape = RoundedCornerShape(50)
                                            )
                                            .padding(
                                                horizontal = 12.dp,
                                                vertical = 4.dp
                                            ),
                                        maxLines = 2,
                                        overflow = TextOverflow.Clip
                                    )
                                }

                            }

                        }

                    }
                }
            }

        }
    }
}

@Composable
@Preview
fun NewsOnboardingPreview() {
    NewsOnboarding()

}