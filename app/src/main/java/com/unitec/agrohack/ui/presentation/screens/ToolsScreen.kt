package com.unitec.agrohack.ui.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val imageRes: Int? = null,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen() {
    var selectedCategory by remember { mutableStateOf("Agricultura") }
    
    val agricultureProducts = listOf(
        Product(1, "Semilla de Maíz Híbrido", "Semilla de alta calidad para máximo rendimiento", "$45.000/kg", null, "Agricultura"),
        Product(2, "Fertilizante NPK 15-15-15", "Fertilizante balanceado para todo tipo de cultivos", "$35.000/bulto", null, "Agricultura"),
        Product(3, "Semilla de Tomate Cherry", "Variedad resistente a enfermedades", "$12.000/sobre", null, "Agricultura"),
        Product(4, "Herbicida Glifosato", "Control efectivo de malezas", "$25.000/litro", null, "Agricultura"),
        Product(5, "Insecticida Orgánico", "Protección natural contra plagas", "$18.000/litro", null, "Agricultura"),
        Product(6, "Semilla de Café Variedad Colombia", "Semilla certificada de café premium", "$8.000/kg", null, "Agricultura")
    )
    
    val livestockProducts = listOf(
        Product(7, "Alimento Concentrado Bovinos", "Concentrado nutricional para ganado", "$55.000/bulto", null, "Ganadería"),
        Product(8, "Medicamento Antiparasitario", "Tratamiento contra parásitos internos", "$35.000/dosis", null, "Ganadería"),
        Product(9, "Suplemento Vitamínico", "Vitaminas y minerales esenciales", "$28.000/kg", null, "Ganadería"),
        Product(10, "Cerca Eléctrica", "Sistema de cercado eléctrico", "$150.000/rollo", null, "Ganadería"),
        Product(11, "Bebedero Automático", "Sistema de agua automático para ganado", "$85.000/unidad", null, "Ganadería"),
        Product(12, "Sales Minerales", "Suplemento mineral para bovinos", "$22.000/kg", null, "Ganadería")
    )
    
    val currentProducts = if (selectedCategory == "Agricultura") agricultureProducts else livestockProducts
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Category Selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                onClick = { selectedCategory = "Agricultura" },
                label = { Text("Agricultura") },
                selected = selectedCategory == "Agricultura",
                leadingIcon = if (selectedCategory == "Agricultura") {
                    { Icon(Icons.Default.Agriculture, contentDescription = null, modifier = Modifier.size(18.dp)) }
                } else null,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                onClick = { selectedCategory = "Ganadería" },
                label = { Text("Ganadería") },
                selected = selectedCategory == "Ganadería",
                leadingIcon = if (selectedCategory == "Ganadería") {
                    { Icon(Icons.Default.Pets, contentDescription = null, modifier = Modifier.size(18.dp)) }
                } else null,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Products List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(currentProducts) { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { 
                        // TODO: Implementar agregar al carrito
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.price,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                // Product Image Placeholder
                Card(
                    modifier = Modifier.size(80.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (product.category == "Agricultura") Icons.Default.Agriculture else Icons.Default.Pets,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Add to Cart Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar")
                }
            }
        }
    }
}

@Preview
@Composable
fun ToolsScreenPreview() {
    MaterialTheme {
        ToolsScreen()
    }
}