package com.unitec.agrohack.ui.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.unitec.agrohack.ui.menus.Screen

@Composable
fun AgroBottomAppBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    NavigationBar {
        val items = listOf(
            BottomNavItem(Screen.Tools, Icons.Default.AddBusiness, "Comprar"),
            BottomNavItem(Screen.Products, Icons.Default.ShoppingCart, "Productos"),
            BottomNavItem(Screen.MyFarm, Icons.Default.Eco, "Tu Finca"),
            BottomNavItem(Screen.Statistics, Icons.Default.BarChart, "Estadísticas"),
            BottomNavItem(Screen.AIGeneration, Icons.Default.AutoAwesome, "IA")
        )

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = currentScreen == item.screen,
                onClick = { onScreenSelected(item.screen) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

@Preview
@Composable
fun AgroBottomAppBarPreview() {
    MaterialTheme {
        AgroBottomAppBar(
            currentScreen = Screen.Tools,
            onScreenSelected = {}
        )
    }
}