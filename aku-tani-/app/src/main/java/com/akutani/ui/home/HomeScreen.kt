package com.akutani.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akutani.R
import com.akutani.network.ArticleDto
import com.akutani.network.WeatherResponse
import com.akutani.ui.article.ArticleUiState
import java.util.Locale
import androidx.compose.ui.text.style.TextOverflow
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat


@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNotificationClick: () -> Unit,
    articleState: ArticleUiState,
    onPupukClick: () -> Unit,
    onKalkulatorPanenClick: () -> Unit,
    onInfoHargaClick: () -> Unit,
    onLihatSemuaArtikelClick: () -> Unit,
    onArticleClick:(ArticleDto) -> Unit,
    onRequestLocation: () -> Unit
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("Petani") }

    LaunchedEffect(Unit) {
        val prefs =
            context.getSharedPreferences("aku_tani_prefs", android.content.Context.MODE_PRIVATE)

        username =
            if (prefs.getBoolean("is_logged_in", false))
                prefs.getString("username", "Petani") ?: "Petani"
            else "Petani"
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) onRequestLocation()
        }

    LaunchedEffect(Unit) {
        val granted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (granted) onRequestLocation()
        else permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        HeaderSection(username, onNotificationClick)

        Spacer(Modifier.height(16.dp))

        WeatherCard(uiState.weather, uiState.locationName)

        Spacer(Modifier.height(16.dp))
        ForecastSection(uiState.forecast)

        Spacer(Modifier.height(24.dp))
        FiturKamiSection(
            onInfoHargaClick = onInfoHargaClick,
            onPupukClick = onPupukClick,
            onKalkulatorPanenClick = onKalkulatorPanenClick
        )

        Spacer(Modifier.height(24.dp))
        HomeArticlesSection(
            state = articleState,
            onSeeAllClick = onLihatSemuaArtikelClick,
            onArticleClick = onArticleClick
        )
    }
}
/*HEADER*/

@Composable
fun HeaderSection(
    username: String,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Hai, $username!",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Image(
            painter = painterResource(R.drawable.notif),
            contentDescription = "Profil",
            modifier = Modifier
                .size(32.dp)
                .clickable { onNotificationClick() }
        )
    }
}

/*WEATHER*/

@Composable
fun WeatherCard(weather: WeatherResponse?, locationName: String) {
    val temp = weather?.main?.temp?.toInt()?.toString() ?: "--"
    val desc = weather?.weather?.firstOrNull()?.description ?: "Mengambil cuaca..."
    val main = weather?.weather?.firstOrNull()?.main ?: "Clouds"

    val iconRes = when (main) {
        "Clear" -> R.drawable.sunny
        "Clouds" -> R.drawable.berawan
        "Rain", "Drizzle" -> R.drawable.rainy
        "Thunderstorm" -> R.drawable.rainthunder
        "Snow" -> R.drawable.snowy
        else -> R.drawable.berawan
    }

    val transition = rememberInfiniteTransition(label = "weather")
    val offsetX by transition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            tween(4000, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "offset"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF2E7D32)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("Cuaca Hari Ini", color = Color.White, fontSize = 16.sp)
                Spacer(Modifier.height(6.dp))
                Text(
                    "$temp°",
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    desc.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    },
                    color = Color.White
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = locationName,
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Image(
                painter = painterResource(iconRes),
                contentDescription = main,
                modifier = Modifier
                    .size(120.dp)
                    .offset(x = offsetX.dp)
            )
        }
    }
}

/*FORECAST*/

@Composable
fun ForecastSection(forecast: List<DailyForecast>) {
    Column {
        Text("Perkiraan 5 Hari", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (forecast.isEmpty()) {
                repeat(5) {
                    ForecastItemCard(DailyForecast("--/--", 0, "Clouds"), true)
                }
            } else {
                forecast.forEach { ForecastItemCard(it) }
            }
        }
    }
}

@Composable
fun ForecastItemCard(day: DailyForecast, isLoading: Boolean = false) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            if (isLoading) Color(0xFFEEEEEE) else Color(0xFFE8F5E9)
        )
    ) {
        Column(
            modifier = Modifier.width(80.dp).padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(day.dateLabel, fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            if (isLoading) {
                Spacer(
                    Modifier.size(45.dp).background(Color.Gray.copy(.3f), CircleShape)
                )
            } else {
                Image(
                    painterResource(R.drawable.berawan),
                    null,
                    Modifier.size(40.dp)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(if (isLoading) "--°" else "${day.temp}°", fontSize = 12.sp)
        }
    }
}

/*FITUR*/

@Composable
fun FiturKamiSection(
    onPupukClick: () -> Unit,
    onInfoHargaClick: () -> Unit,
    onKalkulatorPanenClick: () -> Unit
) {
    Column {
        Text("Fitur Kami", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {

            FiturItem(
                icon = R.drawable.pupuk,
                label = "Penggunaan\nPupuk",
                onClick = onPupukClick
            )

            FiturItem(
                icon = R.drawable.rizal,
                label = "Info Harga",
                onClick = onInfoHargaClick
            )

            FiturItem(
                icon = R.drawable.kalkulator,
                label = "Kalkulator\nPanen",
                onClick = onKalkulatorPanenClick
            )
        }
    }
}


@Composable
fun FiturItem(
    icon: Int,
    label: String,
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(enabled = onClick != null) {
            onClick?.invoke()
        }
    ) {
        Box(
            Modifier.size(64.dp).background(Color(0xFF2E7D32), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(icon), null, Modifier.size(32.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(label, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

/*ARTICLE*/

@Composable
fun HomeArticlesSection(
    state: ArticleUiState,
    onSeeAllClick: () -> Unit,
    onArticleClick: (ArticleDto) -> Unit
) {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text("Artikel Terbaru", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "Lihat Semua",
                fontSize = 12.sp,
                color = Color(0xFF0D47A1),
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }

        Spacer(Modifier.height(8.dp))

        when {
            state.isLoading -> {
                LazyRow {
                    items(3) {
                        ArtikelCard(
                            title = "Memuat...",
                            subtitle = "Sumber",
                            imageUrl = null,
                            isLoading = true
                        )
                    }
                }
            }

            state.error != null -> {
                Text(state.error, color = Color.Red)
            }

            else -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.articles.take(3)) { artikel ->
                        ArtikelCard(
                            title = artikel.title ?: "Tanpa judul",
                            subtitle = artikel.excerpt ?: artikel.source ?: "",
                            imageUrl = artikel.imageUrl,
                            onClick = { onArticleClick(artikel) }
                        )
                    }
                }
            }
        }
    }
}

/* ARTIKEL CARD  */

@Composable
fun ArtikelCard(
    title: String,
    subtitle: String,
    imageUrl: String?,
    isLoading: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable(enabled = !isLoading) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            if (isLoading) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.LightGray)
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(Modifier.padding(12.dp)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, fontSize = 11.sp, color = Color.Gray, maxLines = 2)
            }
        }
    }
}

/* PREVIEW */

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(
        uiState = HomeUiState(),
        onNotificationClick = {},
        articleState = ArticleUiState(),
        onLihatSemuaArtikelClick = {},
        onArticleClick = {},
        onRequestLocation = {},
        onPupukClick = {},
        onKalkulatorPanenClick = {},
        onInfoHargaClick = {}
    )
}
