package com.unitec.agrohack.storage

import com.unitec.agrohack.data.Farm
import com.unitec.agrohack.data.UserProfile
import kotlinx.serialization.Serializable

@Serializable
data class SyncAction(
    val type: String,
    val data: Any, // En Kotlin/Serialization esto será problemático, mejor usar sealed class
    val timestamp: Long
)

// Mejor implementación usando sealed class para type safety
@Serializable
sealed class TypedSyncAction {
    abstract val timestamp: Long
    abstract val id: String
    
    @Serializable
    data class SaveFarm(
        val farm: Farm,
        override val timestamp: Long = System.currentTimeMillis(),
        override val id: String = "save_farm_$timestamp"
    ) : TypedSyncAction()
    
    @Serializable
    data class AddProduction(
        val production: Production,
        override val timestamp: Long = System.currentTimeMillis(),
        override val id: String = "add_production_$timestamp"
    ) : TypedSyncAction()
    
    @Serializable
    data class UpdateProduction(
        val production: Production,
        override val timestamp: Long = System.currentTimeMillis(),
        override val id: String = "update_production_$timestamp"
    ) : TypedSyncAction()
    
    @Serializable
    data class DeleteProduction(
        val productionId: String,
        override val timestamp: Long = System.currentTimeMillis(),
        override val id: String = "delete_production_$timestamp"
    ) : TypedSyncAction()
    
    @Serializable
    data class UpdateProfile(
        val profile: UserProfile,
        override val timestamp: Long = System.currentTimeMillis(),
        override val id: String = "update_profile_$timestamp"
    ) : TypedSyncAction()
}

// Extension para convertir a string legible
fun TypedSyncAction.getActionDescription(): String {
    return when (this) {
        is TypedSyncAction.SaveFarm -> "Guardar finca: ${farm.name}"
        is TypedSyncAction.AddProduction -> "Agregar producción: ${production.cropName}"
        is TypedSyncAction.UpdateProduction -> "Actualizar producción: ${production.cropName}"
        is TypedSyncAction.DeleteProduction -> "Eliminar producción: $productionId"
        is TypedSyncAction.UpdateProfile -> "Actualizar perfil: ${profile.name}"
    }
}

// Estados de sincronización más detallados
enum class SyncItemStatus {
    PENDING,    // Esperando sincronización
    SYNCING,    // En proceso de sincronización
    SYNCED,     // Sincronizado exitosamente
    FAILED,     // Falló la sincronización
    RETRY       // Reintentando sincronización
}

@Serializable
data class SyncQueueItem(
    val action: TypedSyncAction,
    val status: SyncItemStatus = SyncItemStatus.PENDING,
    val retryCount: Int = 0,
    val lastError: String? = null,
    val lastAttempt: Long? = null
)

// Utilidades para manejo de cola de sincronización
object SyncQueueUtils {
    const val MAX_RETRY_COUNT = 3
    const val RETRY_DELAY_MS = 5000L // 5 segundos
    const val MAX_QUEUE_SIZE = 100
    
    fun shouldRetry(item: SyncQueueItem): Boolean {
        return item.status == SyncItemStatus.FAILED && 
               item.retryCount < MAX_RETRY_COUNT
    }
    
    fun isReadyForRetry(item: SyncQueueItem): Boolean {
        if (!shouldRetry(item)) return false
        
        val lastAttempt = item.lastAttempt ?: return true
        val timeSinceLastAttempt = System.currentTimeMillis() - lastAttempt
        
        return timeSinceLastAttempt >= RETRY_DELAY_MS * (item.retryCount + 1)
    }
    
    fun createRetryItem(item: SyncQueueItem, error: String): SyncQueueItem {
        return item.copy(
            status = SyncItemStatus.FAILED,
            retryCount = item.retryCount + 1,
            lastError = error,
            lastAttempt = System.currentTimeMillis()
        )
    }
    
    fun markAsSuccessful(item: SyncQueueItem): SyncQueueItem {
        return item.copy(
            status = SyncItemStatus.SYNCED,
            lastError = null,
            lastAttempt = System.currentTimeMillis()
        )
    }
    
    fun markAsSyncing(item: SyncQueueItem): SyncQueueItem {
        return item.copy(
            status = SyncItemStatus.SYNCING,
            lastAttempt = System.currentTimeMillis()
        )
    }
}