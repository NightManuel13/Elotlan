package com.unitec.agrohack.ui.menus

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unitec.agrohack.ui.presentation.components.AgroBottomAppBar
import com.unitec.agrohack.ui.presentation.components.AgroTopAppBar
import com.unitec.agrohack.ui.presentation.screens.*

enum class Screen(val route: String, val title: String) {
    Farms("farms", "Fincas"),
    Products("products", "Productos"),
    MyFarm("my_farm", "Tu Finca"),
    Statistics("statistics", "Analisis"),
    AIGeneration("ai_generation", "IA"),
    Profile("profile", "Perfil")
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AgroManagerApp() {
    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf(Screen.Farms) }

    Scaffold(
        topBar = {
            AgroTopAppBar(
                title = when (currentScreen) {
                    Screen.Profile -> "Perfil"
                    else -> "Agro Hack"
                },
                showBackButton = currentScreen == Screen.Profile,
                onBackClick = {
                    navController.popBackStack()
                    currentScreen = Screen.Farms
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                    currentScreen = Screen.Profile
                }
            )
        },
        bottomBar = {
            if (currentScreen != Screen.Profile) {
                AgroBottomAppBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Farms.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        currentScreen = screen
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Farms.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Farms.route) {
                currentScreen = Screen.Farms
                FarmsScreen()
            }
            composable(Screen.Products.route) {
                currentScreen = Screen.Products
                ProductsScreen()
            }
            composable(Screen.MyFarm.route) {
                currentScreen = Screen.MyFarm
                MyFarmScreen()
            }
            composable(Screen.Statistics.route) {
                currentScreen = Screen.Statistics
                StatisticsScreen()
            }
            composable(Screen.AIGeneration.route) {
                currentScreen = Screen.AIGeneration
                AIGenerationScreen()
            }
            composable(Screen.Profile.route) {
                currentScreen = Screen.Profile
                ProfileScreen()
            }
        }
    }
}