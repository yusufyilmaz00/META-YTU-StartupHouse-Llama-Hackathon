package com.balikllama.xpguiderdemo.ui.screen.home

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.ui.navigation.Routes
import com.balikllama.xpguiderdemo.ui.screen.dbtest.DBTestViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: HomeViewModel = hiltViewModel(),
    dbTestViewModel : DBTestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // HomeView'a state'i ve event lambda'larını iletiyoruz
    HomeView(
        modifier = modifier,
        windowSizeClass = windowSizeClass,
        uiState = uiState,
        onAddCredit = { viewModel.addCredit(5) },
        onDecreaseCredit = { viewModel.decreaseCredit(10)},
        onNavigateToDbTest = { navController.navigate( Routes.DATABASE_TEST) },
        onResetDatabase = { dbTestViewModel.resetDatabase()},
        onNavigateToTest = { navController.navigate(Routes.TEST_GRAPH) }
    )
}