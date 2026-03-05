package com.akutani.ui.pupuk

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

/* ================= SCREEN ================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenggunaanPupukScreen(
    onBack: () -> Unit,
    viewModel: PenggunaanPupukViewModel = viewModel()
) {
    val state by viewModel.uiState

    PenggunaanPupukContent(
        state = state,
        onBack = onBack,
        onTanamanChange = viewModel::onTanamanChange,
        onLuasChange = viewModel::onLuasChange,
        onHasilChange = viewModel::onHasilChange,
        onTanahChange = viewModel::onTanahChange,
        onPupukChange = viewModel::onPupukChange,
        onHitung = viewModel::hitungPupuk
    )
}

/* ================= CONTENT ================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenggunaanPupukContent(
    state: PenggunaanPupukUiState,
    onBack: () -> Unit,
    onTanamanChange: (String) -> Unit,
    onLuasChange: (String) -> Unit,
    onHasilChange: (String) -> Unit,
    onTanahChange: (String) -> Unit,
    onPupukChange: (String) -> Unit,
    onHitung: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Rekomendasi Penggunaan Pupuk",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            /* ================= FORM ================= */
            Column {

                InputField(
                    label = "Nama Tanaman",
                    value = state.tanaman,
                    onValueChange = onTanamanChange
                )

                InputField(
                    label = "Luas Lahan (m²)",
                    value = state.luasInput,
                    onValueChange = onLuasChange
                )

                InputField(
                    label = "Target Hasil Panen (ton)",
                    value = state.hasilInput,
                    onValueChange = onHasilChange
                )

                InputField(
                    label = "Kondisi Tanah (Masukan Angka)",
                    value = state.tanahInput,
                    onValueChange = onTanahChange
                )

                InputField(
                    label = "Jenis Pupuk",
                    value = state.pupuk,
                    onValueChange = onPupukChange
                )

                /* ================= ERROR ================= */
                state.errorMessage?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                /* ================= RESULT ================= */
                state.rekomendasi?.let {
                    Spacer(Modifier.height(16.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E9)
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = "Hasil Rekomendasi",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF2E7D32)
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = it,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            /* ================= BUTTON ================= */
            Button(
                onClick = onHitung,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B8F3A)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Hitung Pupuk",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/* ================= INPUT ================= */

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

/* ================= PREVIEW ================= */

@Preview(showBackground = true)
@Composable
fun PenggunaanPupukPreview() {
    PenggunaanPupukContent(
        state = PenggunaanPupukUiState(
            tanaman = "Padi",
            luasInput = "1000",
            hasilInput = "6",
            tanahInput = "50",
            pupuk = "NPK",
            rekomendasi = "Rekomendasi pupuk: 250 kg"
        ),
        onBack = {},
        onTanamanChange = {},
        onLuasChange = {},
        onHasilChange = {},
        onTanahChange = {},
        onPupukChange = {},
        onHitung = {}
    )
}
