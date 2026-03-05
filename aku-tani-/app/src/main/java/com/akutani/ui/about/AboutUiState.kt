package com.akutani.ui.about

data class AboutUiState(
    val appName: String = "AkuTani",
    val description: String =
        "AkuTani adalah aplikasi yang membantu petani mengakses informasi cuaca, artikel pertanian, " +
                "dan fitur-fitur bermanfaat lainnya.",
    val developerName: String = "HuseanAHA BOY",
    val developerEmail: String = "alhabib.husein13@gmail.com",
    val version: String = "1.0.0(beta)"
)
