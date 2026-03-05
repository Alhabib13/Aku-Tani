package com.akutani.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akutani.R

/* =======================================================
   ROUTE SCREEN (TERHUBUNG KE VIEWMODEL)
   ======================================================= */

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit
) {
    val vm: AuthViewModel = viewModel()
    val state by vm.uiState

    // ✅ NAVIGASI SETELAH REGISTER SUKSES
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToLogin()
            vm.resetState()
        }
    }

    RegisterContent(
        onRegisterClick = { username, email, password, confirm ->
            vm.register(
                username = username,
                email = email,
                password = password,
                confirm = confirm
            )
        },
        onNavigateToLogin = onNavigateToLogin,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage
    )
}

/* =======================================================
   UI CONTENT (DESAIN ASLI – TIDAK DIUBAH)
   ======================================================= */

@Composable
fun RegisterContent(
    onRegisterClick: (String, String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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

            Spacer(Modifier.height(80.dp))

            AkuTaniLogo()

            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Daftar dulu yuk..... : )",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            RoundedInputField(username, { username = it }, "Username")
            Spacer(Modifier.height(12.dp))

            RoundedInputField(email, { email = it }, "Masukan Email")
            Spacer(Modifier.height(12.dp))

            RoundedInputField(password, { password = it }, "Masukan Password", true)
            Spacer(Modifier.height(12.dp))

            RoundedInputField(
                confirmPassword,
                { confirmPassword = it },
                "Konfirmasi Password",
                true
            )

            errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    onRegisterClick(
                        username,
                        email,
                        password,
                        confirmPassword
                    )
                },
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
                    Text("DAFTAR", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(16.dp))

            Row {
                Text("Sudah memiliki akun Aku Tani ? ")
                Text(
                    text = "Masuk",
                    color = Color(0xFF0D47A1),
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

/* =======================================================
   COMPONENTS
   ======================================================= */

@Composable
fun AkuTaniLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.aku_tani),
        contentDescription = "Logo Aku Tani",
        modifier = modifier.size(80.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text(placeholder) },
        shape = RoundedCornerShape(24.dp),
        visualTransformation =
            if (isPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF0F0F0),
            focusedContainerColor = Color(0xFFF0F0F0),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        )
    )
}

/* =======================================================
   PREVIEW
   ======================================================= */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview() {
    MaterialTheme {
        RegisterContent(
            onRegisterClick = { _, _, _, _ -> },
            onNavigateToLogin = {},
            isLoading = false,
            errorMessage = null
        )
    }
}
