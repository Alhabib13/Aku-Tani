package com.akutani.ui.article

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akutani.network.ArticleDto
import androidx.compose.ui.tooling.preview.Preview



@Composable
fun ArticleListScreen(
    state: ArticleUiState,
    onBackClick: () -> Unit,
    onArticleClick: (ArticleDto) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ===== TOP BAR =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF2E7D32))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Kembali",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBackClick() }
            )
            Spacer(Modifier.width(16.dp))
            Text("Artikel", fontSize = 20.sp, color = Color.White)
        }

        // ===== CONTENT =====
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.error, color = Color.Red)
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (state.articles.isEmpty()) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Belum ada artikel", color = Color.Gray)
                        }
                    }

                    state.articles.forEach { artikel ->
                        ArticleListItem(
                            artikel = artikel,
                            onClick = { onArticleClick(artikel) }
                        )
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ArticleListScreenPreview() {
    val fakeArticles = listOf(
        ArticleDto(
            id = 1,
            title = "Manfaat Pupuk Organik",
            body = "Isi artikel pupuk organik",
            imageUrl = "https://picsum.photos/300/200",
            source = "Akutani"
        ),
        ArticleDto(
            id = 2,
            title = "Cara Mengolah Tanah yang Baik",
            body = "Isi artikel pengolahan tanah",
            imageUrl = "https://picsum.photos/300/201",
            source = "Akutani"
        )
    )

    val fakeState = ArticleUiState(
        articles = fakeArticles,
        isLoading = false,
        error = null
    )

    ArticleListScreen(
        state = fakeState,
        onBackClick = {},
        onArticleClick = {}
    )
}
