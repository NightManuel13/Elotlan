package com.unitec.agrohack.ui.presentation.viewmodels

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unitec.agrohack.data.Farm
import com.unitec.agrohack.data.Plot
import com.unitec.agrohack.ui.presentation.components.AddCropDialog
import com.unitec.agrohack.ui.theme.AgroHackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFarmScreen(
    onBack: () -> Unit,
    onSave: (Farm) -> Unit,
    farmData: Farm?
) {
    val context = LocalContext.current
    
    var farmName by remember { mutableStateOf(farmData?.name ?: "") }
    var farmLocation by remember { mutableStateOf(farmData?.location ?: "") }
    var farmDescription by remember { mutableStateOf(farmData?.description ?: "") }
    var plots by remember { mutableStateOf(farmData?.plots ?: emptyList()) }
    var newPlotName by remember { mutableStateOf("") }
    var newPlotLocation by remember { mutableStateOf("") }
    var isAddCropDialogOpen by remember { mutableStateOf(false) }
    var selectedPlotId by remember { mutableStateOf("") }

    val addPlot = {
        if (newPlotName.isNotBlank() && newPlotLocation.isNotBlank()) {
            val newPlot = Plot(
                id = System.currentTimeMillis().toString(),
                name = newPlotName.trim(),
                location = newPlotLocation.trim(),
                crops = emptyList()
            )
            plots = plots + newPlot
            newPlotName = ""
            newPlotLocation = ""
            Toast.makeText(context, "Parcela agregada correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Por favor completa el nombre y ubicación de la parcela", Toast.LENGTH_SHORT).show()
        }
    }

    val removePlot = { plotId: String ->
        plots = plots.filter { it.id != plotId }
        Toast.makeText(context, "Parcela eliminada", Toast.LENGTH_SHORT).show()
    }

    val openAddCropDialog = { plotId: String ->
        selectedPlotId = plotId
        isAddCropDialogOpen = true
    }

    val handleAddCrop = { cropName: String ->
        plots = plots.map { plot ->
            if (plot.id == selectedPlotId) {
                plot.copy(crops = plot.crops + cropName)
            } else {
                plot
            }
        }
        isAddCropDialogOpen = false
        Toast.makeText(context, "Cultivo agregado correctamente", Toast.LENGTH_SHORT).show()
    }

    val removeCropFromPlot = { plotId: String, cropIndex: Int ->
        plots = plots.map { plot ->
            if (plot.id == plotId) {
                plot.copy(crops = plot.crops.filterIndexed { index, _ -> index != cropIndex })
            } else {
                plot
            }
        }
        Toast.makeText(context, "Cultivo eliminado", Toast.LENGTH_SHORT).show()
    }

    val handleSave = {
        if (farmName.isNotBlank() && farmLocation.isNotBlank()) {
            val updatedFarmData = Farm(
                id = farmData?.id ?: System.currentTimeMillis().toString(),
                name = farmName.trim(),
                description = farmDescription.trim(),
                location = farmLocation.trim(),,,,
            )
            onSave(updatedFarmData)
            Toast.makeText(context, "Finca actualizada correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Por favor completa el nombre y ubicación de la finca", Toast.LENGTH_SHORT).show()
        }
    }

    val selectedPlot = plots.find { it.id == selectedPlotId }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
                Text(
                    text = "Editar Finca",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = handleSave,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Guardar")
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Información básica
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Información Básica",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        OutlinedTextField(
                            value = farmName,
                            onValueChange = { farmName = it },
                            label = { Text("Nombre de la finca *") },
                            placeholder = { Text("Ej: Finca La Esperanza") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        OutlinedTextField(
                            value = farmLocation,
                            onValueChange = { farmLocation = it },
                            label = { Text("Ubicación *") },
                            placeholder = { Text("Ej: Manizales, Caldas") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        OutlinedTextField(
                            value = farmDescription,
                            onValueChange = { farmDescription = it },
                            label = { Text("Descripción (opcional)") },
                            placeholder = { Text("Describe tu finca, métodos de cultivo, especialidades...") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                    }
                }
            }

            // Parcelas
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Parcelas",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        // Agregar nueva parcela
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Agregar nueva parcela",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                
                                OutlinedTextField(
                                    value = newPlotName,
                                    onValueChange = { newPlotName = it },
                                    label = { Text("Nombre de la parcela") },
                                    placeholder = { Text("Ej: Parcela Norte") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                
                                OutlinedTextField(
                                    value = newPlotLocation,
                                    onValueChange = { newPlotLocation = it },
                                    label = { Text("Ubicación de la parcela") },
                                    placeholder = { Text("Ej: Sector montañoso, lote 3") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                
                                OutlinedButton(
                                    onClick = addPlot,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Agregar Parcela")
                                }
                            }
                        }
                    }
                }
            }

            // Lista de parcelas
            items(plots) { plot ->
                PlotCard(
                    plot = plot,
                    onRemove = { removePlot(plot.id) },
                    onAddCrop = { openAddCropDialog(plot.id) },
                    onRemoveCrop = { cropIndex -> removeCropFromPlot(plot.id, cropIndex) }
                )
            }

            if (plots.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "No hay parcelas agregadas aún",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Agrega tu primera parcela usando el formulario de arriba",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    if (isAddCropDialogOpen) {
        AddCropDialog(
            isOpen = true,
            onClose = { isAddCropDialogOpen = false },
            onAddCrop = handleAddCrop,
            plotName = selectedPlot?.name ?: ""
        )
    }
}

@Composable
fun PlotCard(
    plot: Plot,
    onRemove: () -> Unit,
    onAddCrop: () -> Unit,
    onRemoveCrop: (Int) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(plot.name) }
    var editLocation by remember { mutableStateOf(plot.location) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                if (isEditing) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            placeholder = { Text("Nombre de la parcela") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = editLocation,
                            onValueChange = { editLocation = it },
                            placeholder = { Text("Ubicación de la parcela") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    // TODO: Implement edit functionality
                                    isEditing = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Guardar")
                            }
                            OutlinedButton(
                                onClick = {
                                    editName = plot.name
                                    editLocation = plot.location
                                    isEditing = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                        }
                    }
                } else {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = plot.name,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = plot.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Row {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = onRemove) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            if (!isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cultivos/Productos",
                        style = MaterialTheme.typography.labelMedium
                    )
                    OutlinedButton(
                        onClick = onAddCrop,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Agregar", style = MaterialTheme.typography.labelSmall)
                    }
                }
                
                if (plot.crops.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(plot.crops.size) { index ->
                            AssistChip(
                                onClick = { onRemoveCrop(index) },
                                label = { Text(plot.crops[index]) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Eliminar",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No hay cultivos agregados",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditFarmScreenPreview() {
    AgroHackTheme {
        EditFarmScreen(
            onBack = {},
            onSave = {},
            farmData = null
        )
    }
}