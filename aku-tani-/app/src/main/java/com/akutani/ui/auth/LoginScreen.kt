package com.akutani.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

/* =======================================================
   ROUTE SCREEN (TERHUBUNG KE VIEWMODEL)
   ======================================================= */

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val vm: AuthViewModel = viewModel()
    val state by vm.uiState

    // ✅ NAVIGASI SETELAH LOGIN SUKSES
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
            vm.resetState() // penting biar tidak ke-trigger ulang
        }
    }

    LoginContent(
        onLoginClick = { email, password ->
            vm.login(email, password)
        },
        onNavigateToRegister = onNavigateToRegister,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage
    )
}

/* =======================================================
   UI CONTENT (DESAIN ASLI – TIDAK DIUBAH)
   ======================================================= */

@Composable
fun LoginContent(
    onLoginClick: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            AkuTaniLogo()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Aku Tani",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Catat Cermat, Panen Tepat",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Login untuk mengelola data hasil panen dan\n" +
                        "menghitung kebutuhan pupuk secara mudah dan efisien",
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(Modifier.fillMaxWidth()) {
                Text("Email :")
            }

            Spacer(Modifier.height(4.dp))

            RoundedInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Masukan Email"
            )

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth()) {
                Text("Password:")
            }

            Spacer(Modifier.height(4.dp))

            RoundedInputField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Masukan Password",
                isPassword = true
            )

            errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = { onLoginClick(email, password) },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color(0xFF00C853)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF00C853)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "MASUK",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row {
                Text("Belum memiliki akun Aku Tani ? ")
                Text(
                    text = "Daftar",
                    color = Color(0xFF0D47A1),
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

/* =======================================================
   PREVIEW
   ======================================================= */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    MaterialTheme {
        LoginContent(
            onLoginClick = { _, _ -> },
            onNavigateToRegister = {},
            isLoading = false,
            errorMessage = null
        )
    }
}
