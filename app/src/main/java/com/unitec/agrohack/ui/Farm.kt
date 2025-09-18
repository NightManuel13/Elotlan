package com.unitec.agrohack.ui

import androidx.compose.ui.graphics.Color
import com.unitec.agrohack.ui.theme.Chart1
import com.unitec.agrohack.ui.theme.Chart2
import com.unitec.agrohack.ui.theme.Chart3
import com.unitec.agrohack.ui.theme.Chart4

data class Farm(
    val id: Int,
    val name: String,
    val description: String,
    val location: String,
    val imageUrl: String,
    val certificate: String,
    val production: List<ProductionItem>
)

data class ProductionItem(
    val name: String,
    val value: Int,
    val color: Color
)

object FarmRepository {
    fun getFarms(): List<Farm> {
        return listOf(
            Farm(
                id = 1,
                name = "Finca La Esperanza",
                description = "Finca especializada en cultivos orgánicos de café y aguacate. Utilizamos métodos sostenibles y tecnología moderna para garantizar la mejor calidad en nuestros productos.",
                location = "Manizales, Caldas",
                imageUrl = "https://images.unsplash.com/photo-1633281122614-49d2304d57a4?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxjb2ZmZWUlMjBwbGFudGF0aW9uJTIwZmFybXxlbnwxfHx8fDE3NTcxMDgwMzh8MA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
                certificate = "Orgánico",
                production = listOf(
                    ProductionItem("Café", 60, Chart1),
                    ProductionItem("Aguacate", 30, Chart2),
                    ProductionItem("Otros", 10, Chart3)
                )
            ),
            Farm(
                id = 2,
                name = "Hacienda Verde",
                description = "Producción de aguacates premium con certificación internacional. Contamos con tecnología de riego por goteo y sistemas de monitoreo climático avanzados.",
                location = "Uruapan, Michoacán",
                imageUrl = "https://images.unsplash.com/photo-1594144970323-9bbe6baaea7a?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxhdm9jYWRvJTIwb3JjaGFyZCUyMGZhcm18ZW58MXx8fHwxNzU3MTA4MDQwfDA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
                certificate = "Global GAP",
                production = listOf(
                    ProductionItem("Aguacate", 85, Chart1),
                    ProductionItem("Limón", 10, Chart2),
                    ProductionItem("Otros", 5, Chart3)
                )
            ),
            Farm(
                id = 3,
                name = "Granja San Miguel",
                description = "Cultivo de maíz y cereales con técnicas de agricultura de precisión. Implementamos drones para monitoreo de cultivos y análisis de suelos automatizado.",
                location = "Sinaloa, México",
                imageUrl = "https://images.unsplash.com/photo-1655131468751-c4210f1c4c5e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxjb3JuJTIwZmllbGQlMjBhZ3JpY3VsdHVyZXxlbnwxfHx8fDE3NTcxMDgwNDN8MA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
                certificate = "RTRS",
                production = listOf(
                    ProductionItem("Maíz", 70, Chart1),
                    ProductionItem("Sorgo", 20, Chart2),
                    ProductionItem("Soja", 10, Chart3)
                )
            ),
            Farm(
                id = 4,
                name = "Invernaderos del Norte",
                description = "Producción hidropónica de vegetales y hortalizas en ambiente controlado. Sistemas automatizados de clima y nutrición para máxima eficiencia productiva.",
                location = "Querétaro, México",
                imageUrl = "https://images.unsplash.com/photo-1580050530399-479dc872b08b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx2ZWdldGFibGUlMjBncmVlbmhvdXNlJTIwZmFybXxlbnwxfHx8fDE3NTcxMDgwNDZ8MA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
                certificate = "BRC",
                production = listOf(
                    ProductionItem("Tomate", 40, Chart1),
                    ProductionItem("Pepino", 25, Chart2),
                    ProductionItem("Pimiento", 20, Chart3),
                    ProductionItem("Otros", 15, Chart4)
                )
            ),
            Farm(
                id = 5,
                name = "Finca Bella Vista",
                description = "Agricultura mixta con enfoque en sostenibilidad ambiental. Combinamos ganadería responsable con cultivos rotativos para mantener el equilibrio del ecosistema.",
                location = "Valle del Cauca, Colombia",
                imageUrl = "https://images.unsplash.com/photo-1619598951257-68e45c835908?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxvcmdhbmljJTIwZmFybSUyMGZpZWxkc3xlbnwxfHx8fDE3NTcxMDgwMzV8MA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
                certificate = "Rainforest",
                production = listOf(
                    ProductionItem("Ganadería", 45, Chart1),
                    ProductionItem("Caña", 30, Chart2),
                    ProductionItem("Plátano", 15, Chart3),
                    ProductionItem("Otros", 10, Chart4)
                )
            )
        )
    }
}