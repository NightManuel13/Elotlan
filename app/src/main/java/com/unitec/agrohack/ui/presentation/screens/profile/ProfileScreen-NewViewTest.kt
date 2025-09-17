package com.unitec.agrohack.ui.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.ui.text.input.ImeAction

import com.unitec.agrohack.data.UserProfile
import com.unitec.agrohack.ui.presentation.components.AvatarSelector
import com.google.firebase.auth.FirebaseAuth
import com.unitec.agrohack.ui.presentation.screens.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // Settings state (from ProfileScreen.kt functionality)
    var notificationsEnabled by remember { mutableStateOf(true) }
    var userEmail by remember { mutableStateOf("Cargando correo...") }
    
    // Profile state
    var profile by remember {
        mutableStateOf(
            UserProfile(
                name = "",
                email = "",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&w=150&h=150"
            )
        )
    }
    
    // Editing state
    var editingProfile by remember { mutableStateOf(profile) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show snackbar when needed
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(snackbarMessage)
            showSnackbar = false
        }
    }

    // Load user email from FirebaseAuth (from ProfileScreen.kt functionality)
    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val email = it.email
            userEmail = email ?: "Correo no disponible"
        } ?: run {
            userEmail = "Ningún usuario ha iniciado sesión"
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(
                            onClick = { 
                                isEditing = true
                                editingProfile = profile
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                    } else {
                        Row {
                            // Cancel button
                            IconButton(
                                onClick = { 
                                    isEditing = false
                                    editingProfile = profile
                                },
                                enabled = !isLoading
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Cancelar")
                            }
                            
                            // Save button
                            IconButton(
                                onClick = {
                                    // Save profile logic
                                    if (editingProfile.name.isNotBlank() && editingProfile.email.isNotBlank()) {
                                        isLoading = true
                                        // Simulate API call
                                        profile = editingProfile
                                        isEditing = false
                                        snackbarMessage = "Perfil actualizado correctamente"
                                        showSnackbar = true
                                        isLoading = false
                                    } else {
                                        snackbarMessage = "Por favor completa todos los campos requeridos"
                                        showSnackbar = true
                                    }
                                },
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(Icons.Default.Save, contentDescription = "Guardar")
                                }
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Personal Information Card
            PersonalInfoCard(
                profile = if (isEditing) editingProfile else profile.copy(email = userEmail),
                isEditing = isEditing,
                onProfileChange = { editingProfile = it }
            )

            // Settings Card (from ProfileScreen.kt)
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Configuración",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Notificaciones",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                    }
                }
            }
            
            // Help & Support Card
            ActionCard(
                title = "Ayuda y Soporte",
                icon = Icons.AutoMirrored.Filled.Help,
                onClick = { 
                    snackbarMessage = "Función de ayuda próximamente"
                    showSnackbar = true
                }
            )
            
            // Logout Card
            ActionCard(
                title = "Cerrar Sesión",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                isDestructive = true,
                onClick = { onLogout() }
            )
        }
    }
}

@Composable
private fun PersonalInfoCard(
    profile: UserProfile,
    isEditing: Boolean,
    onProfileChange: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Avatar and Name Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AvatarSelector(
                    currentAvatarUrl = profile.avatarUrl,
                    onAvatarSelected = { newUrl ->
                        onProfileChange(profile.copy(avatarUrl = newUrl))
                    },
                    isEditing = isEditing
                )
                
                Column(modifier = Modifier.weight(1f)) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = profile.name,
                            onValueChange = { onProfileChange(profile.copy(name = it)) },
                            label = { Text("Nombre completo") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = profile.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (profile.isVerified) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verificado",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Cuenta verificada",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
            
            // Email Information
            ContactInfoItem(
                icon = Icons.Default.Email,
                label = "Email",
                value = profile.email,
                isEditing = isEditing,
                keyboardType = KeyboardType.Email,
                onValueChange = { onProfileChange(profile.copy(email = it)) }
            )
        }
    }
}

@Composable
private fun ContactInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isEditing: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.weight(1f)
            )
        } else {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDestructive: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (isDestructive) 
                    MaterialTheme.colorScheme.error 
                else 
                    MaterialTheme.colorScheme.onSurface
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Suppress("VisualLintBounds", "VisualLintAtfColorblindCheck",
    "VisualLintAccessibilityTestFramework", "VisualLintButtonSize"
)
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(onLogout = {})
    }
}