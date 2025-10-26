package com.balikllama.xpguiderdemo.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.balikllama.xpguiderdemo.repository.UserPreferencesRepository
import com.balikllama.xpguiderdemo.ui.screen.calculationtest.CalculationTestScreen
import com.balikllama.xpguiderdemo.ui.screen.dbtest.DBTestScreen
import com.balikllama.xpguiderdemo.ui.screen.chatbot.ChatbotScreen
import com.balikllama.xpguiderdemo.ui.screen.home.HomeScreen
import com.balikllama.xpguiderdemo.ui.screen.interestselection.InterestSelectionScreen
import com.balikllama.xpguiderdemo.ui.screen.login.LoginScreen
import com.balikllama.xpguiderdemo.ui.screen.profile.ProfileScreen
import com.balikllama.xpguiderdemo.ui.screen.register.RegisterScreen
import com.balikllama.xpguiderdemo.ui.screen.splash.SplashScreen
import com.balikllama.xpguiderdemo.ui.screen.test.TestScreen
import com.balikllama.xpguiderdemo.ui.screen.test.TestViewModel
import com.balikllama.xpguiderdemo.ui.screen.testresults.TestResultScreen
import com.balikllama.xpguiderdemo.viewmodel.CreditViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    fun isInterestSelectionCompleted(): Boolean {
        return userPreferencesRepository.isInterestSelectionCompleted()
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    windowSizeClass: WindowSizeClass,
    creditViewModel: CreditViewModel
) {
    NavHost(
        navController = navController,
        // Uygulamanın başlangıç ekranı olarak RegisterScreen'i ayarlıyoruz.
        startDestination = Routes.REGISTER,
        modifier = modifier
    ) {
        // KAYIT EKRANI
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    // Kayıt başarılı olunca Login'e git ve Register'ı yığından temizle.
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    // "Giriş Yap" butonuna basınca da Login'e git ve Register'ı yığından temizle.
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        // GİRİŞ EKRANI
        composable(Routes.LOGIN) {
            // LoginScreen zaten başarılı girişte Splash'e yönlendirme yapıyor.
            // Bu en doğru akış çünkü Splash, ilgi alanı seçimi gerekip gerekmediğini kontrol ediyor.
            LoginScreen(
                navController = navController,
                onNavigateToRegister = {
                    // Kullanıcı "Kayıt Ol"a basarsa Register'a geri dönsün.
                    navController.navigate(Routes.REGISTER) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // SPLASH EKRANI (Yönlendirme merkezi)
        composable(Routes.SPLASH) {
            SplashScreen(navController = navController)
        }

        // İLGİ ALANI SEÇİM EKRANI
        composable(Routes.INTEREST_SELECTION) {
            InterestSelectionScreen(navController = navController)
        }

        // ============================================
        // ANA EKRANLAR (Giriş yapıldıktan sonra)
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

        // Test ile ilgili ekranları içeren alt graf
        testGraph(navController, modifier)

        // ============================================
        // DİĞER EKRANLAR
        // ============================================
        composable(route = Routes.PURCHASE) {
            // TODO: PurchaseScreen eklenecek
        }

        composable(route = Routes.DATABASE_TEST) {
            DBTestScreen(
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

private fun NavGraphBuilder.testGraph(navController: NavController, modifier: Modifier) {
    navigation(
        startDestination = Routes.TEST,
        route = Routes.TEST_GRAPH
    ) {
        composable(route = Routes.TEST) { backStackEntry ->
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

        composable(route = Routes.CALCULATION_TEST) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Routes.TEST_GRAPH) }
            val testViewModel: TestViewModel = hiltViewModel(parentEntry)

            CalculationTestScreen(
                modifier = modifier,
                navController = navController,
                sharedTestViewModel = testViewModel
            )
        }
    }
}