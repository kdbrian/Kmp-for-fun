package com.kdbrian.weather.weatherapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kdbrian.weather.weatherapp.config.KtorProvider
import com.kdbrian.weather.weatherapp.domain.model.CurrentWeather
import com.kdbrian.weather.weatherapp.presentation.theme.lugrasimo_regular
import io.github.aakira.napier.Napier
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.async
import org.jetbrains.compose.resources.painterResource
import weatherapp.composeApp.BuildConfig
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.altitude
import weatherapp.composeapp.generated.resources.logo_white
import weatherapp.composeapp.generated.resources.mycity
import weatherapp.composeapp.generated.resources.temperature
import weatherapp.composeapp.generated.resources.windy
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.Locale
import kotlin.random.Random

val url =
    "https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=${BuildConfig.openWeatherApiKey}"


@Composable
fun WeatherApp() {

    val current = produceState<CurrentWeather?>(null) {
        Napier.d(url)
        val response = async {
            KtorProvider.client.get(url)
        }.await()

        if (response.status.value == 200) {
            value = response.body()
            Napier.d { "Got $response" }
        }

    }



    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(Res.drawable.mycity),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "My City",
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier.fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF748067),
                            Color.Black.copy(.75f),
                        ),
                    )
                ).blur(32.dp)
        )


        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent,
        ) {

            Row(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    AnimatedVisibility(
                        current.value?.weather != null && current.value?.weather?.isEmpty()
                            ?.not() == true
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            AsyncImage(
                                model = "https://openweathermap.org/payload/api/media/file/${current.value?.weather?.first()?.icon}.png",
                                contentDescription = "Weather Icon",
                                modifier = Modifier.size(64.dp).padding(8.dp)
                            )

                            Text(
                                fontFamily = lugrasimo_regular,
                                text = buildAnnotatedString {
                                    withStyle(
                                        SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 32.sp
                                        )
                                    ) {
                                        append(current.value?.weather?.first()?.main.toString())
                                    }

                                    append('\n')

                                    withStyle(
                                        SpanStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    ) {
                                        append(current.value?.weather?.first()?.description.toString())
                                    }
                                },
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Text(
                        fontFamily = lugrasimo_regular,
                        text = current.value?.main?.feelsLike?.toString() ?: "...",
                        style = MaterialTheme.typography.headlineMedium
                    )


                    HorizontalDivider(Modifier.padding(vertical = 12.dp, horizontal = 8.dp))

                    Text(
                        fontFamily = lugrasimo_regular,
                        text = OffsetDateTime.now().toLocalDate().toString(),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {

                        Text(
                            fontFamily = lugrasimo_regular,
                            text = OffsetDateTime.now().toLocalDate().dayOfWeek.name.lowercase()
                                .capitalize(
                                    Locale.getDefault()
                                ),
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(
                            Modifier.width(12.dp)
                        )

                        Text(
                            fontFamily = lugrasimo_regular,
                            text = OffsetDateTime.now().toLocalTime().toString().split(".").first(),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                    Text(
                        fontFamily = lugrasimo_regular,
                        text = current.value?.name ?: "...",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                }

                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {

                        Text(
                            fontFamily = lugrasimo_regular,
                            text = LocalDate.now().dayOfWeek.name.lowercase()
                                .capitalize(Locale.getDefault()),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 12.dp),
                        )

                        Text(
                            fontFamily = lugrasimo_regular,
                            text = "Tomorrow",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(vertical = 12.dp),
                        )
                    }

                    Spacer(Modifier.height(24.dp))


                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {

                        item {
                            Box(
                                modifier = Modifier.fillMaxSize().height(100.dp)
                                    .background(Color(Random.nextLong()), RoundedCornerShape(12.dp))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.temperature),
                                        contentDescription = "Temperature",
                                        modifier = Modifier.padding(12.dp)
                                            .size(35.dp),
                                    )

                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        current.value?.main?.tempMax?.let { max ->
                                            Text(
                                                fontFamily = lugrasimo_regular,
                                                text = max.toString(),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }

                                        current.value?.main?.tempMin?.let { min ->
                                            Text(
                                                fontFamily = lugrasimo_regular,
                                                text = min.toString(),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        }
                                    }

                                }
                            }
                        }

                        item {
                            Box(
                                modifier = Modifier.fillMaxSize().height(100.dp)
                                    .background(Color(Random.nextLong()), RoundedCornerShape(12.dp))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.windy),
                                        contentDescription = "Temperature",
                                        modifier = Modifier.padding(12.dp)
                                            .size(35.dp),
                                    )

                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        current.value?.wind?.speed?.let { max ->
                                            Text(
                                                fontFamily = lugrasimo_regular,
                                                text = max.toString(),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }

                                        current.value?.wind?.deg?.let { min ->
                                            Text(
                                                fontFamily = lugrasimo_regular,
                                                text = min.toString(),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        }

                                        current.value?.wind?.gust?.let { min ->
                                            Text(
                                                fontFamily = lugrasimo_regular,
                                                text = min.toString(),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        }
                                    }

                                }
                            }
                        }


                        item {
                            Box(
                                modifier = Modifier.fillMaxSize().height(100.dp)
                                    .background(Color(Random.nextLong()), RoundedCornerShape(12.dp))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.altitude),
                                        contentDescription = "Temperature",
                                        modifier = Modifier.padding(12.dp)
                                            .size(35.dp),
                                    )

                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        current.value?.main?.seaLevel?.let { level ->
                                            Text(
                                                fontFamily = lugrasimo_regular,
                                                text = level.toString(),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }

                                }
                            }
                        }


                    }


                    Spacer(Modifier.padding(end = 24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Image(
                            painter = painterResource(Res.drawable.logo_white),
                            contentDescription = "Logo",
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(Modifier.padding(end = 12.dp))

                        Text(
                            fontFamily = lugrasimo_regular,
                            text = "Powered by OpenWeatherApi",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(vertical = 12.dp),
                        )
                    }


                }

            }


        }


    }

}


@Composable
@Preview
fun WeatherAppPreview() {

}

