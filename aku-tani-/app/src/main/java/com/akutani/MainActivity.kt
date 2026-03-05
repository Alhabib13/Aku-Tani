package com.akutani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.akutani.ui.components.AppBottomBar
import com.akutani.ui.navigation.AppNavHost
import com.akutani.ui.navigation.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // ✅ BottomBar hanya muncul di halaman utama
                val showBottomBar = currentRoute in listOf(
                    Routes.HOME,
                    Routes.PROFILE
                )

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            AppBottomBar(navController = navController)
                        }
                    }
                ) { padding ->
                    Box(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        AppNavHost(navController = navController)
                    }
                }
            }
        }
    }
}
