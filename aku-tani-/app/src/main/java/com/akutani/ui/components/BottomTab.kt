package com.akutani.ui.components

import com.akutani.R
import com.akutani.ui.navigation.Routes

sealed class BottomTab(
    val route: String,
    val label: String,
    val iconRes: Int
) {
    object Home : BottomTab(
        route = Routes.HOME,
        label = "Home",
        iconRes = R.drawable.rumah
    )

    object Profile : BottomTab(
        route = Routes.PROFILE,
        label = "Profil",
        iconRes = R.drawable.orang
    )
}
