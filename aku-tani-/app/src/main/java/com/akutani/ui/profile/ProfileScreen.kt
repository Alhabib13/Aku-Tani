package com.akutani.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akutani.R

/* ================= ROOT ================= */

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    onLoginClick: () -> Unit,
    onEditAccountClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val state by viewModel.uiState

    // 🔥 AUTO REFRESH SETIAP PROFILE DIBUKA
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Box(Modifier.fillMaxSize()) {
        if (!state.isLoggedIn) {
            ProfileLoggedOut(
                onLoginClick = onLoginClick
            )
        } else {
            ProfileLoggedIn(
                state = state,
                navController = navController,
                onEditAccountClick = onEditAccountClick,
                onSecurityClick = onSecurityClick,
                onAboutClick = onAboutClick,
                onLogoutClick = {
                    viewModel.showLogoutDialog(true)
                }
            )
        }

        /* ================= LOGOUT DIALOG ================= */

        if (state.showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showLogoutDialog(false) },
                title = { Text("Konfirmasi Keluar") },
                text = { Text("Yakin ingin keluar dari akun?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.showLogoutDialog(false)
                        viewModel.logout {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.showLogoutDialog(false) }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

/* ================= LOGGED OUT ================= */

@Composable
fun ProfileLoggedOut(
    onLoginClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.aku_tani),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Masuk ke akun kamu dulu, yuk!",
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onLoginClick, // ✅ INI FIX-NYA
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32),
                    contentColor = Color.White
                )
            ) {
                Text("Masuk")
            }
        }
    }
}

/* ================= LOGGED IN ================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileLoggedIn(
    state: ProfileUiState,
    navController: NavController,
    onEditAccountClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Profil", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF2E7D32)
            )
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 72.dp, bottom = 100.dp)
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.avatarUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(state.username, fontWeight = FontWeight.Bold)
                    Text(state.email, fontSize = 12.sp, color = Color.Gray)
                }
            }

            ProfileMenuRow("Ubah akun", onEditAccountClick)
            ProfileMenuRow("Keamanan akun", onSecurityClick)
            ProfileMenuRow("Tentang", onAboutClick)
            ProfileMenuRow("Keluar", onLogoutClick, destructive = true)
        }
    }
}

/* ================= MENU ROW ================= */

@Composable
fun ProfileMenuRow(
    title: String,
    onClick: () -> Unit,
    destructive: Boolean = false
) {
    val color = if (destructive) Color(0xFFD32F2F) else Color.Black

    HorizontalDivider()

    Text(
        text = title,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    )
}

/* ================= PREVIEW ================= */

@Preview(showBackground = true)
@Composable
fun ProfileLoggedOutPreview() {
    ProfileLoggedOut(
        onLoginClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileLoggedInPreview() {
    ProfileLoggedIn(
        state = ProfileUiState(
            isLoggedIn = true,
            username = "Husein Dage'lan",
            email = "husein@email.com",
            avatarUrl = null
        ),
        navController = rememberNavController(),
        onEditAccountClick = {},
        onSecurityClick = {},
        onAboutClick = {},
        onLogoutClick = {}
    )
}
