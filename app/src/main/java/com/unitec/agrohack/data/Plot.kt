package com.unitec.agrohack.data

data class Plot(
    val id: String,
    val name: String,
    val location: String,
    val crops: List<String> = emptyList()
)
