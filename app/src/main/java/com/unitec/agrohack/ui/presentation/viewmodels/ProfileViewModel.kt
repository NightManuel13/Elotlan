package com.unitec.agrohack.ui.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.unitec.agrohack.data.UserProfile
import com.unitec.agrohack.data.ProfileUiState
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    private val _profile = MutableStateFlow(
        UserProfile(
            name = "",
            email = "",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&w=150&h=150"
        )
    )
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()
    
    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    

    
    init {
        loadProfile()
    }
    
    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val user = FirebaseAuth.getInstance().currentUser
                val current = _profile.value
                val updated = if (user != null) {
                    current.copy(
                        name = if (!user.displayName.isNullOrBlank()) user.displayName!! else current.name,
                        email = user.email ?: current.email
                    )
                } else {
                    current
                }
                _profile.value = updated
                delay(300)
                _uiState.value = ProfileUiState.Success(updated)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al cargar el perfil")
            }
        }
    }
    
    fun startEditing() {
        _isEditing.value = true
    }
    
    fun cancelEditing() {
        _isEditing.value = false
    }
    
    fun updateProfile(updatedProfile: UserProfile) {
        _profile.value = updatedProfile
    }
    
    fun saveProfile(updatedProfile: UserProfile) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Validate required fields
                if (updatedProfile.name.isBlank() || updatedProfile.email.isBlank()) {
                    _uiState.value = ProfileUiState.Error("Por favor completa todos los campos requeridos")
                    return@launch
                }
                
                // Simulate API call
                delay(1500)
                
                _profile.value = updatedProfile
                _isEditing.value = false
                _uiState.value = ProfileUiState.Success(updatedProfile)
                
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al guardar el perfil")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateAvatar(avatarUrl: String) {
        _profile.value = _profile.value.copy(avatarUrl = avatarUrl)
    }
    

    
    fun showHelpSupport() {
        // Navigate to help screen or show help dialog
    }
    
    fun showLogoutDialog() {
        // Show logout confirmation dialog
    }
    
    fun logout() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                FirebaseAuth.getInstance().signOut()
                _profile.value = UserProfile()
                _isEditing.value = false
                _uiState.value = ProfileUiState.Success(_profile.value)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al cerrar sesión")
            } finally {
                _isLoading.value = false
            }
        }
    }
}