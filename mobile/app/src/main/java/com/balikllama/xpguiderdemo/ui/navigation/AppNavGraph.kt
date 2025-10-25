package com.balikllama.xpguiderdemo.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.balikllama.xpguiderdemo.ui.screen.dbtest.DBTestScreen
import com.balikllama.xpguiderdemo.ui.screen.chatbot.ChatbotScreen
import com.balikllama.xpguiderdemo.ui.screen.home.HomeScreen
import com.balikllama.xpguiderdemo.ui.screen.login.LoginScreen
import com.balikllama.xpguiderdemo.ui.screen.profile.ProfileScreen
import com.balikllama.xpguiderdemo.ui.screen.register.RegisterScreen
import com.balikllama.xpguiderdemo.ui.screen.test.TestScreen
import com.balikllama.xpguiderdemo.ui.screen.test.TestViewModel
import com.balikllama.xpguiderdemo.ui.screen.testresults.TestResultScreen
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
                windowSizeClass = windowSizeClass
            )
        }

        composable(route = Routes.PROFILE) {
            ProfileScreen(
                modifier = modifier.fillMaxSize(),
                navController= navController,
                windowSizeClass = windowSizeClass
            )
        }

        testGraph(navController, modifier)
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

private fun NavGraphBuilder.testGraph(navController: NavController, modifier: Modifier) {
    // Yeni bir navigasyon grafiği oluşturuyoruz.
    // Başlangıç noktası TEST ekranı, rota adı ise TEST_GRAPH olacak.
    navigation(
        startDestination = Routes.TEST,
        route = Routes.TEST_GRAPH
    ) {
        // Bu grafik içindeki ekranlar
        composable(route = Routes.TEST) { backStackEntry ->
            // hiltViewModel() fonksiyonuna, ViewModel'in hangi üst rotaya
            // bağlanacağını söylüyoruz.
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Routes.TEST_GRAPH) }
            val testViewModel: TestViewModel = hiltViewModel(parentEntry)

            TestScreen(
                modifier = modifier.fillMaxSize(),
                navController = navController,
                viewModel = testViewModel
            )
        }

        composable(route = Routes.TEST_RESULTS) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Routes.TEST_GRAPH) }
            val testViewModel: TestViewModel = hiltViewModel(parentEntry)

            TestResultScreen(
                modifier = modifier.fillMaxSize(),
                navController = navController,
                viewModel = testViewModel
            )
        }
    }
}