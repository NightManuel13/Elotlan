package com.unitec.agrohack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.unitec.agrohack.ui.LoginScreen
import com.unitec.agrohack.ui.RegisterScreen
import com.unitec.agrohack.ui.menus.AgroManagerApp
import com.unitec.agrohack.ui.theme.AgroHackTheme
import kotlinx.coroutines.delay

@Composable
fun MyLottie() {
    val url = "https://cdn.lottielab.com/l/F1CXX2BhD9oeYK.json?v=117d071e5531e18f1b3ba3b2&w=1"
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(url))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
    LottieAnimation(composition = composition, progress = { progress })
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Firebase comunication initialization
        FirebaseApp.initializeApp(this)

        setContent {
            AgroHackTheme {
                var showSplashScreen by remember { mutableStateOf(true) }
                val navController = rememberNavController()
                var isLoggedIn by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }

                LaunchedEffect(Unit) {
                    // Mostrar la animación de splash por 2 segundos
                    delay(3500)
                    showSplashScreen = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showSplashScreen) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color( 0xFF91E17B))
                                .padding(innerPadding),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            MyLottie()
                        }
                    } else {
                        if (isLoggedIn) {
                            HomeScreen(onLogout = {
                                FirebaseAuth.getInstance().signOut()
                                isLoggedIn = false
                            })
                        } else {
                            NavHost(
                                navController = navController,
                                startDestination = "login",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("login") {
                                    LoginScreen(
                                        onLoginSuccess = { isLoggedIn = true },
                                        onNavigateToRegister = { navController.navigate("register") }
                                    )
                                }
                                composable("register") {
                                    RegisterScreen(
                                        onRegisterSuccess = { isLoggedIn = FirebaseAuth.getInstance().currentUser != null },
                                        onNavigateToLogin = { navController.popBackStack() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    AgroManagerApp()
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    AgroHackTheme { HomeScreen {} }
}