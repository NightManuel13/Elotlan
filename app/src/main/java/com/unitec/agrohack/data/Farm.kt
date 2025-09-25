package com.unitec.agrohack.data

import kotlinx.serialization.Serializable

@Serializable
data class Farm(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val description: String = "",
    val plots: List<Plot> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isLocal: Boolean = false //Para datos no sincronizados
)

@Serializable
data class Plot(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val crops: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
