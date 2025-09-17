package com.unitec.agrohack.data

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val isVerified: Boolean = true
)

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: UserProfile) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}