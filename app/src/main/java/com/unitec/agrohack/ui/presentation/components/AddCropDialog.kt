package com.unitec.agrohack.ui.presentation.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.unitec.agrohack.ui.theme.AgroHackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCropDialog(
    isOpen: Boolean,
    onClose: () -> Unit,
    onAddCrop: (String) -> Unit,
    plotName: String
) {
    val context = LocalContext.current
    var customCrop by remember { mutableStateOf("") }
    var filteredSuggestions by remember { mutableStateOf(COMMON_CROPS) }

    val handleCustomCropChange = { value: String ->
        customCrop = value
        filteredSuggestions = if (value.isNotBlank()) {
            COMMON_CROPS.filter { crop ->
                crop.lowercase().contains(value.lowercase())
            }
        } else {
            COMMON_CROPS
        }
    }

    val handleAddCrop = { cropName: String ->
        val trimmedCrop = cropName.trim()
        if (trimmedCrop.isNotBlank()) {
            onAddCrop(trimmedCrop)
            customCrop = ""
            filteredSuggestions = COMMON_CROPS
        } else {
            Toast.makeText(context, "Por favor ingresa el nombre del cultivo", Toast.LENGTH_SHORT).show()
        }
    }

    val handleAddCustomCrop = {
        handleAddCrop(customCrop)
    }

    val handleSuggestionClick = { crop: String ->
        handleAddCrop(crop)
    }

    val handleClose = {
        customCrop = ""
        filteredSuggestions = COMMON_CROPS
        onClose()
    }

    if (isOpen) {
        Dialog(onDismissRequest = handleClose) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Agregar Cultivo",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "Agrega un cultivo a la parcela \"$plotName\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = handleClose) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar"
                            )
                        }
                    }

                    // Campo de entrada personalizado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = customCrop,
                            onValueChange = handleCustomCropChange,
                            label = { Text("Nombre del cultivo") },
                            placeholder = { Text("Ej: Café, Tomate, Maíz...") },
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = handleAddCustomCrop,
                            enabled = customCrop.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar"
                            )
                        }
                    }

                    // Sugerencias de cultivos
                    Text(
                        text = "Sugerencias (${filteredSuggestions.size})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        if (filteredSuggestions.isNotEmpty()) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filteredSuggestions) { crop ->
                                    AssistChip(
                                        onClick = { handleSuggestionClick(crop) },
                                        label = { Text(crop) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "No se encontraron cultivos con ese nombre",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Usa el campo de arriba para agregar tu cultivo personalizado",
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Botón cancelar
                    OutlinedButton(
                        onClick = handleClose,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}

// Lista de cultivos comunes para sugerencias
private val COMMON_CROPS = listOf(
    "Café", "Maíz", "Frijol", "Aguacate", "Plátano", "Tomate", "Cebolla", "Zanahoria",
    "Lechuga", "Cilantro", "Perejil", "Yuca", "Papa", "Arroz", "Cacao", "Caña de azúcar",
    "Mango", "Naranja", "Limón", "Papaya", "Guayaba", "Lulo", "Mora", "Fresa"
)

@Preview(showBackground = true)
@Composable
fun AddCropDialogPreview() {
    AgroHackTheme {
        AddCropDialog(
            isOpen = true,
            onClose = {},
            onAddCrop = {},
            plotName = "Parcela Norte"
        )
    }
}