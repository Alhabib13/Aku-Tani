package com.akutani.ui.edit_account

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akutani.R
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EditAccountScreen(
    initialName: String,
    initialEmail: String,
    onBack: () -> Unit,
    viewModel: EditAccountViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.setInitialData(initialName, initialEmail)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onAvatarChange(uri)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text("Ubah Akun", fontSize = 20.sp)
        }

        Spacer(Modifier.height(24.dp))

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(state.avatarUri ?: R.drawable.placeholder)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Pilih Foto")
        }

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
            enabled = !state.isLoading,
            onClick = {
                viewModel.saveProfile(
                    onSuccess = {
                        Toast.makeText(context, "Profil diperbarui", Toast.LENGTH_SHORT).show()
                        onBack()
                    },
                    onError = {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }
                )
            }
        ) {
            Text(
                if (state.isLoading) "Menyimpan..." else "Simpan",
                color = Color.White
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun EditAccountScreenLoadingPreview() {
    EditAccountScreen(
        initialName = "Husein Dagelan",
        initialEmail = "husein@email.com",
        onBack = {}
    )
}
