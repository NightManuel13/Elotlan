package com.unitec.agrohack.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.*
import javax.inject.Inject
import com.unitec.agrohack.data.Farm
import kotlinx.serialization.Serializable

@SinceKotlin("1.4")
class LocalDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val FARM_KEY = stringPreferencesKey("farm")
        private val PRODUCTIONS_KEY = stringPreferencesKey("productions")
        private val SYNC_QUEUE_KEY = stringPreferencesKey("sync_queue")
    }

    suspend fun saveFarm(farm: Farm) {
        dataStore.edit { preferences ->
            preferences[FARM_KEY] = Json.encodeToString<Farm>(farm)
        }
    }

    suspend fun getFarm(): Farm? {
        return dataStore.data.map { preferences ->
            preferences[FARM_KEY]?.let {
                Json.decodeFromString<Farm>(it)
            }
        }.first()
    }

    suspend fun saveProductions(productions: List<Production>) {
        dataStore.edit { preferences ->
            preferences[PRODUCTIONS_KEY] = Json.encodeToString<List<Production>>(productions)
        }
    }

    suspend fun getProductions(): List<Production> {
        return dataStore.data.map { preferences ->
            preferences[PRODUCTIONS_KEY]?.let {
                Json.decodeFromString<List<Production>>(it)
            } ?: emptyList()
        }.first()
    }

    suspend fun addToSyncQueue(action: SyncAction) {
        val currentQueue = getSyncQueue().toMutableList()
        currentQueue.add(action)

        dataStore.edit { preferences ->
            preferences[SYNC_QUEUE_KEY] = Json.encodeToString<List<SyncAction>>(currentQueue)
        }
    }

    suspend fun getSyncQueue(): List<SyncAction> {
        return dataStore.data.map { preferences ->
            preferences[SYNC_QUEUE_KEY]?.let {
                Json.decodeFromString<List<SyncAction>>(it)
            } ?: emptyList()
        }.first()
    }

    suspend fun clearSyncQueue() {
        dataStore.edit { preferences ->
            preferences[SYNC_QUEUE_KEY] = Json.encodeToString<List<SyncAction>>(emptyList())
        }
    }
}

public class Production {
    val cropName = String
    val id = Int
}

// Clase para configuración de la aplicación
@Serializable
data class AppSettings(
    val isDarkMode: Boolean = false,
    val language: String = "es",
    val autoSync: Boolean = true,
    val syncOnlyOnWifi: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val lastSyncTimestamp: Long = 0L
)

// Clase para resumen de datos
data class DataSummary(
    val hasFarm: Boolean = false,
    val productionsCount: Int = 0,
    val pendingSyncCount: Int = 0,
    val hasProfile: Boolean = false,
    val lastSyncTimestamp: Long? = null
)