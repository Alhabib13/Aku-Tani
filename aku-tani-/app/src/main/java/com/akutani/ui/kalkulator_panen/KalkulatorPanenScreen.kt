package com.akutani.ui.kalkulator_panen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KalkulatorPanenScreen(
    onBack: () -> Unit,
    viewModel: KalkulatorPanenViewModel = viewModel()
) {
    val state by viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Kalkulator Panen", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
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

            Column {

                InputField("Curah Hujan (mm)", state.luasLahan, viewModel::onLuasChange)
                InputField("Indeks Kualitas Tanah", state.curahHujan, viewModel::onCurahHujanChange)
                InputField("Ukuran Lahan Hektar", state.kualitasTanah, viewModel::onKualitasTanahChange)
                InputField("Jam Sinar Matahari", state.jumlahPupuk, viewModel::onJumlahPupukChange)
                InputField("Pupuk (Kg)", state.lamaTanam, viewModel::onLamaTanamChange)

                state.errorMessage?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }

                // ================= HASIL =================

                if (state.hasilCart != null && state.hasilRf != null) {
                    Spacer(Modifier.height(16.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E9)
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "Hasil Prediksi Panen",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text("CART : ${state.hasilCart}")
                            Text("Random Forest : ${state.hasilRf}")
                        }
                    }
                }
            }

            Button(
                onClick = viewModel::hitungPanen,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B8F3A)
                )
            ) {
                Text(
                    "Hitung Panen",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun KalkulatorPanenContent(
    state: KalkulatorPanenUiState,
    onLuasChange: (String) -> Unit,
    onCurahHujanChange: (String) -> Unit,
    onKualitasTanahChange: (String) -> Unit,
    onJumlahPupukChange: (String) -> Unit,
    onLamaTanamChange: (String) -> Unit,
    onHitung: () -> Unit
) {
    Column {

        InputField("Curah Hujan (mm)", state.luasLahan, onLuasChange)
        InputField("Indeks Kualitas Tanah", state.curahHujan, onCurahHujanChange)
        InputField("Ukuran Lahan Hektar", state.kualitasTanah, onKualitasTanahChange)
        InputField("Jam Sinar Matahari", state.jumlahPupuk, onJumlahPupukChange)
        InputField("Pupuk (Kg)", state.lamaTanam, onLamaTanamChange)

        state.errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color.Red, fontSize = 12.sp)
        }

        if (state.hasilCart != null && state.hasilRf != null) {
            Spacer(Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Hasil Prediksi Panen",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("CART : ${state.hasilCart}")
                    Text("Random Forest : ${state.hasilRf}")
                }
            }
        }
    }
}


@Composable
fun InputField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun KalkulatorPanenScreenPreview() {
    KalkulatorPanenScreen(
        onBack = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun KalkulatorPanenPreview() {
    KalkulatorPanenContent(
        state = KalkulatorPanenUiState(
            luasLahan = "1.5",
            curahHujan = "200",
            kualitasTanah = "8",
            jumlahPupuk = "250",
            lamaTanam = "120",
            hasilCart = "5.6 ton/ha",
            hasilRf = "6.1 ton/ha"
        ),
        onLuasChange = {},
        onCurahHujanChange = {},
        onKualitasTanahChange = {},
        onJumlahPupukChange = {},
        onLamaTanamChange = {},
        onHitung = {}
    )
}

