package com.akutani.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.akutani.ui.article.ArticleListScreen
import com.akutani.ui.article.ArticleViewModel
import com.akutani.ui.article.detail.ArticleDetailScreen
import com.akutani.ui.article.detail.ArticleDetailViewModel
import com.akutani.ui.auth.LoginScreen
import com.akutani.ui.auth.RegisterScreen
import com.akutani.ui.home.HomeScreen
import com.akutani.ui.home.HomeViewModel
import com.akutani.ui.profile.ProfileScreen
import com.akutani.ui.profile.ProfileViewModel
import com.akutani.ui.pupuk.PenggunaanPupukScreen
import com.akutani.ui.edit_account.EditAccountScreen
import com.akutani.ui.about.AboutScreen
import com.akutani.ui.info_harga.InfoHargaScreen
import com.akutani.ui.security.SecurityScreen
import com.akutani.ui.kalkulator_panen.KalkulatorPanenScreen
import com.akutani.ui.intro.IntroScreen
import com.akutani.utils.PrefsKeys
import com.akutani.ui.notification.NotificationScreen
import com.akutani.ui.notification.NotificationUiState


@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("aku_tani_prefs", Context.MODE_PRIVATE)
    NavHost(
        navController = navController,
        startDestination = Routes.INTRO
    ) {

        /* ================= INTRO ================= */
        composable(Routes.INTRO) {

            LaunchedEffect(Unit) {
                val alreadyShown = prefs.getBoolean(PrefsKeys.INTRO_SHOWN, false)
                if (alreadyShown) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.INTRO) { inclusive = true }
                    }
                }
            }

            IntroScreen(
                onMulaiClick = {
                    prefs.edit()
                        .putBoolean(PrefsKeys.INTRO_SHOWN, true)
                        .apply()

                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.INTRO) { inclusive = true }
                    }
                }
            )
        }
        /* ================= HOME ================= */
        composable(Routes.HOME) {
            val homeVM: HomeViewModel = viewModel()
            val articleVM: ArticleViewModel = viewModel()

            HomeScreen(
                uiState = homeVM.homeUiState.value,
                articleState = articleVM.uiState.collectAsStateWithLifecycle().value,
                onPupukClick = { navController.navigate(Routes.ROUTE_PUPUK) },
                onInfoHargaClick = { navController.navigate(Routes.INFO_HARGA) },
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATION)},
                onKalkulatorPanenClick = {
                    navController.navigate(Routes.KALKULATOR_PANEN)
                },
                onLihatSemuaArtikelClick = { navController.navigate(Routes.ARTICLE_LIST) },
                onArticleClick = { article ->
                    navController.navigate("${Routes.ARTICLE_DETAIL}/${article.id}")
                },
                onRequestLocation = {
                    homeVM.fetchWeatherWithLocation()
                }
            )
        }

        /* ================= NOTIFICATION ================= */
        composable(Routes.NOTIFICATION) {
            NotificationScreen(
                uiState = NotificationUiState(), // sementara
                onBack = { navController.popBackStack() }
            )
        }

        /* ================= INFO HARGA ================= */
        composable(Routes.INFO_HARGA) {
            InfoHargaScreen(
                onBack = { navController.popBackStack() }
            )
        }

        /* ================= PUPUK ================= */
        composable(Routes.ROUTE_PUPUK) {
            PenggunaanPupukScreen(
                onBack = { navController.popBackStack() }
            )
        }

        /* ================= KALKULATOR PANEN ================= */
        composable(Routes.KALKULATOR_PANEN) {
            KalkulatorPanenScreen(
                onBack = { navController.popBackStack() }
            )
        }

        /* ================= PROFILE ================= */
        composable(Routes.PROFILE) {
            val profileVM: ProfileViewModel = viewModel()

            ProfileScreen(
                navController = navController,
                viewModel = profileVM,
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onEditAccountClick = { navController.navigate(Routes.EDIT_ACCOUNT) },
                onSecurityClick = { navController.navigate(Routes.SECURITY) },
                onAboutClick = { navController.navigate(Routes.ABOUT) }
            )
        }

        /* ================= EDIT ACCOUNT ================= */
        composable(Routes.EDIT_ACCOUNT) {
            EditAccountScreen(
                initialName = "",
                initialEmail = "",
                onBack = { navController.popBackStack() }
            )
        }

        /* ================= SECURITY ================= */
        composable(Routes.SECURITY) {
            SecurityScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        /* ================= ABOUT ================= */
        composable(Routes.ABOUT) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        /* ================= LOGIN ================= */
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        /* ================= REGISTER ================= */
        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        /* ================= ARTICLES ================= */
        composable(Routes.ARTICLE_LIST) {
            val articleVM: ArticleViewModel = viewModel()
            val state = articleVM.uiState.collectAsStateWithLifecycle().value

            ArticleListScreen(
                state = state,
                onBackClick = { navController.popBackStack() },
                onArticleClick = {
                    navController.navigate("${Routes.ARTICLE_DETAIL}/${it.id}")
                }
            )
        }

        /* ================= ARTICLE DETAIL ================= */
        composable("${Routes.ARTICLE_DETAIL}/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            val detailVM: ArticleDetailViewModel = viewModel()

            LaunchedEffect(id) {
                detailVM.loadArticleById(id.toInt())
            }

            ArticleDetailScreen(
                uiState = detailVM.uiState.value,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
