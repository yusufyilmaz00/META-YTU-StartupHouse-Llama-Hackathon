package com.balikllama.xpguiderdemo.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.balikllama.xpguiderdemo.ui.screen.dbtest.DBTestScreen
import com.balikllama.xpguiderdemo.ui.screen.chatbot.ChatbotScreen
import com.balikllama.xpguiderdemo.ui.screen.home.HomeScreen
import com.balikllama.xpguiderdemo.ui.screen.login.LoginScreen
import com.balikllama.xpguiderdemo.ui.screen.profile.ProfileScreen
import com.balikllama.xpguiderdemo.ui.screen.register.RegisterScreen
import com.balikllama.xpguiderdemo.ui.screen.test.TestScreen
import com.balikllama.xpguiderdemo.viewmodel.CreditViewModel

@Composable
fun AppNavGraph(navController: NavHostController,
                modifier: Modifier,
                windowSizeClass: WindowSizeClass,
                creditViewModel: CreditViewModel) {

    NavHost(
        navController = navController as NavHostController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // Login başarılı olunca Home'a git ve Login'i geri yığınından (backstack) kaldır
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }
        // ============================================
        // MAIN SCREENS
        // ============================================
        composable(route = Routes.HOME) {
            HomeScreen(
                modifier = modifier.fillMaxSize(),
                navController = navController,
                windowSizeClass = windowSizeClass
            )
        }

        composable(route = Routes.CHATBOT) {
            ChatbotScreen(
                modifier = modifier.fillMaxSize(),
                navController= navController,
                windowSizeClass = windowSizeClass,
                creditViewModel = creditViewModel
            )
        }

        composable(route = Routes.PROFILE) {
            ProfileScreen(
                modifier = modifier.fillMaxSize(),
                navController= navController,
                windowSizeClass = windowSizeClass,
                creditViewModel = creditViewModel
            )
        }

        composable(Routes.TEST) {
            TestScreen(navController = navController)
        }

        // ============================================
        // FUTURE SCREENS
        // ============================================
        // Kredi satın alma ekranı
        composable(route = Routes.PURCHASE) {
            // TODO: PurchaseScreen eklenecek
        }

        // Test ekranı (kredi harcayacak)
        composable(route = Routes.DATABASE_TEST) { //
            DBTestScreen(
                modifier = modifier.fillMaxSize()
            )
        }
    }
}