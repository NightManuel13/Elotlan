package com.unitec.agrohack.ui.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unitec.agrohack.ui.Farm
import com.unitec.agrohack.ui.FarmRepository
import com.unitec.agrohack.ui.presentation.components.FarmCard

@Composable
fun FarmsScreen() {
    val farms = remember { FarmRepository.getFarms() }
    var selectedFarm by remember { mutableStateOf<Farm?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Todas las Fincas",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(farms) { farm ->
                FarmCard(
                    farm = farm,
                    onClick = { selectedFarm = it }
                )
            }
        }
    }
    
    // TODO: Show farm details when selectedFarm is not null
    selectedFarm?.let { farm ->
        // Navigate to farm details or show dialog
    }
}

@Preview(showBackground = true)
@Composable
fun FarmsScreenPreview() {
    MaterialTheme {
        FarmsScreen()
    }
}