package com.balikllama.xpguiderdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.balikllama.xpguiderdemo.ui.components.BottomNavigationBar
import com.balikllama.xpguiderdemo.ui.designsystem.AppTheme
import com.balikllama.xpguiderdemo.ui.navigation.AppNavGraph
import com.balikllama.xpguiderdemo.ui.theme.B1demoTheme
import com.balikllama.xpguiderdemo.viewmodel.CreditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val creditViewModel: CreditViewModel = viewModel()

            AppTheme {
                val navController = rememberNavController()

                MainScreen(windowSizeClass = windowSizeClass,navController=navController,creditViewModel=creditViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(windowSizeClass: WindowSizeClass, navController: NavController,creditViewModel: CreditViewModel) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController as NavHostController,
            modifier = Modifier.padding(innerPadding),
            windowSizeClass = windowSizeClass,
            creditViewModel = creditViewModel
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    B1demoTheme {
        Greeting("Android")
    }
}