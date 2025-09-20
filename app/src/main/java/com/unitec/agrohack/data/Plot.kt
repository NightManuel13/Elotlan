package com.unitec.agrohack.data

data class Plot(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val crops: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Extension functions for Plot operations
fun Plot.addCrop(cropName: String): Plot {
    return if (crops.contains(cropName)) {
        this
    } else {
        this.copy(
            crops = crops + cropName,
            updatedAt = System.currentTimeMillis()
        )
    }
}

fun Plot.removeCrop(cropName: String): Plot {
    return this.copy(
        crops = crops.filter { it != cropName },
        updatedAt = System.currentTimeMillis()
    )
}

fun Plot.removeCropAt(index: Int): Plot {
    return if (index in crops.indices) {
        this.copy(
            crops = crops.filterIndexed { i, _ -> i != index },
            updatedAt = System.currentTimeMillis()
        )
    } else {
        this
    }
}

fun Plot.updateInfo(name: String, location: String): Plot {
    return this.copy(
        name = name.trim(),
        location = location.trim(),
        updatedAt = System.currentTimeMillis()
    )
}

fun Plot.hasCrops(): Boolean {
    return crops.isNotEmpty()
}

fun Plot.getCropCount(): Int {
    return crops.size
}

// Sample data for testing
object PlotSamples {
    val samplePlots = listOf(
        Plot(
            id = "1",
            name = "Parcela Norte",
            location = "Sector montañoso, lote 1",
            crops = listOf("Café", "Plátano")
        ),
        Plot(
            id = "2",
            name = "Parcela Sur",
            location = "Zona plana, cerca del río",
            crops = listOf("Maíz", "Frijol", "Yuca")
        ),
        Plot(
            id = "3",
            name = "Huerta Familiar",
            location = "Cerca de la casa principal",
            crops = listOf("Tomate", "Lechuga", "Cilantro", "Cebolla")
        )
    )
}