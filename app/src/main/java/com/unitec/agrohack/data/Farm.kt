package com.unitec.agrohack.data

// Core Farm data model used across the app
// Includes plots so Add/Edit screens can persist and display them

data class Farm(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val plots: List<Plot> = emptyList()
)
