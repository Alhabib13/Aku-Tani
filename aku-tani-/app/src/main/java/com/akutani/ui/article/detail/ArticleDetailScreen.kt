package com.akutani.ui.article.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akutani.R
import androidx.compose.ui.text.AnnotatedString
import androidx.core.text.HtmlCompat
import androidx.compose.ui.tooling.preview.Preview
import com.akutani.network.ArticleDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    uiState: ArticleDetailUiState,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artikel", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32)
                )
            )
        }
    ) { padding ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(uiState.error, color = Color.Red)
                }
            }

            uiState.article != null -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(ctx)
                            .data(uiState.article.imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.aku_tani),
                        error = painterResource(R.drawable.aku_tani),
                        contentDescription = uiState.article.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = uiState.article.title.orEmpty(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (!uiState.article.source.isNullOrBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Sumber : ${uiState.article.source}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    val content = uiState.article.body.orEmpty()

                    Text(
                        text = AnnotatedString(
                            HtmlCompat.fromHtml(
                                content,
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            ).toString()
                        ),
                        fontSize = 14.sp
                    )

                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ArticleDetailScreenPreview() {
    val fakeArticle = ArticleDto(
        id = 1,
        title = "Manfaat Pupuk Organik untuk Tanaman",
        body = """
            <p>Pupuk organik membantu meningkatkan kesuburan tanah.</p>
            <p>Selain itu, pupuk ini ramah lingkungan dan aman digunakan.</p>
        """.trimIndent(),
        imageUrl = "https://picsum.photos/800/400",
        source = "Akutani"
    )

    val fakeUiState = ArticleDetailUiState(
        article = fakeArticle,
        isLoading = false,
        error = null
    )

    ArticleDetailScreen(
        uiState = fakeUiState,
        onBack = {}
    )
}
