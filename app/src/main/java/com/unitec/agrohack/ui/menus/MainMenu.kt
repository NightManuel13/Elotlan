package com.unitec.agrohack.ui.menus

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unitec.agrohack.ui.presentation.components.AgroBottomAppBar
import com.unitec.agrohack.ui.presentation.components.AgroTopAppBar
import com.unitec.agrohack.ui.presentation.screens.AIGenerationScreen
import com.unitec.agrohack.ui.presentation.screens.FarmPlot
import com.unitec.agrohack.ui.presentation.screens.MyFarmScreen
import com.unitec.agrohack.ui.presentation.screens.ProductsScreen
import com.unitec.agrohack.ui.presentation.screens.ProfileScreen
import com.unitec.agrohack.ui.presentation.screens.StatisticsScreen
import com.unitec.agrohack.ui.presentation.screens.ToolsScreen
import com.unitec.agrohack.ui.presentation.screens.UserFarm
import com.unitec.agrohack.ui.presentation.viewmodels.AddFarmScreen
import com.unitec.agrohack.ui.presentation.viewmodels.EditFarmScreen

enum class Screen(val route: String, val title: String) {
    Tools("tools", "Herramientas"),
    Products("products", "Productos"),
    MyFarm("my_farm", "Tu Finca"),
    Statistics("statistics", "Analisis"),
    AIGeneration("ai_generation", "IA"),
    Profile("profile", "Perfil"),
    AddFarm("addFarm", "Agregar Finca"),
    EditFarm("editFarm", "Editar Finca")
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AgroManagerApp(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()
    var userFarm: UserFarm? by remember { mutableStateOf<UserFarm?>(null) }
    var currentScreen by remember { mutableStateOf(Screen.MyFarm) }

    Scaffold(
        topBar = {
            AgroTopAppBar(
                title = when (currentScreen) {
                    Screen.Profile -> "Perfil"
                    Screen.AddFarm -> ""
                    else -> "Elotlan"
                },
                showBackButton = currentScreen == Screen.Profile,
                onBackClick = {
                    navController.popBackStack()
                    currentScreen = Screen.MyFarm
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
                            popUpTo(Screen.MyFarm.route) {
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
            startDestination = Screen.MyFarm.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Tools.route) {
                currentScreen = Screen.Tools
                ToolsScreen()
            }
            composable(Screen.Products.route) {
                currentScreen = Screen.Products
                ProductsScreen()
            }
            composable(Screen.MyFarm.route) {
                currentScreen = Screen.MyFarm
                MyFarmScreen(
                    userFarm = userFarm,
                    onAddFarm = { navController.navigate("addFarm") },
                    onEditFarm = {navController.navigate("editFarm") })
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
                ProfileScreen(onLogout = onLogout)
            }
            composable(Screen.AddFarm.route) {
                currentScreen = Screen.AddFarm
                AddFarmScreen(

                    onBack = { navController.popBackStack() },
                    onSave = { farm ->
                        // Guarda la finca y regresa
                        // Actualiza el estado de la finca en ViewModel
                        userFarm = UserFarm(
                            id = farm.id,
                            name = farm.name,
                            location = farm.location,
                            description = farm.description,
                            plots = farm.plots.map { plot ->
                                FarmPlot(
                                    id = plot.id,
                                    name = plot.name,
                                    location = plot.location,
                                    crops = plot.crops
                                )
                            }
                        )
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.EditFarm.route) {
                EditFarmScreen(
                    farmData = userFarm,
                    onBack = {
                        navController.navigate(Screen.MyFarm.route) {
                            popUpTo(Screen.MyFarm.route) { inclusive = false }
                        }
                    },
                    onSave = { farm ->
                        navController.navigate(Screen.MyFarm.route) {
                            popUpTo(Screen.MyFarm.route) { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}