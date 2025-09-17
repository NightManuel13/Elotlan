package com.unitec.agrohack.ui.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unitec.agrohack.data.UserProfile
import com.unitec.agrohack.data.ProfileUiState
import com.unitec.agrohack.ui.presentation.components.AvatarSelector
import com.unitec.agrohack.ui.presentation.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedProfileScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var editingProfile by remember(profile) { mutableStateOf(profile) }
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (uiState) {
            is ProfileUiState.Success -> {
                snackbarHostState.showSnackbar("Perfil actualizado correctamente")
            }
            is ProfileUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as ProfileUiState.Error).message)
            }
            else -> {}
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    ProfileTopBarActions(
                        isEditing = isEditing,
                        isLoading = isLoading,
                        onStartEditing = {
                            viewModel.startEditing()
                            editingProfile = profile
                        },
                        onCancelEditing = {
                            viewModel.cancelEditing()
                            editingProfile = profile
                        },
                        onSaveProfile = {
                            viewModel.saveProfile(editingProfile)
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is ProfileUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = (uiState as ProfileUiState.Error).message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* Retry loading */ }
                    ) {
                        Text("Reintentar")
                    }
                }
            }
            
            is ProfileUiState.Success -> {
                ProfileContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    profile = if (isEditing) editingProfile else profile,
                    isEditing = isEditing,
                    onProfileChange = { editingProfile = it },
                    onAvatarChange = { viewModel.updateAvatar(it) },
                    onHelpClick = viewModel::showHelpSupport,
                    onLogoutClick = viewModel::showLogoutDialog
                )
            }
        }
    }
}

@Composable
private fun ProfileTopBarActions(
    isEditing: Boolean,
    isLoading: Boolean,
    onStartEditing: () -> Unit,
    onCancelEditing: () -> Unit,
    onSaveProfile: () -> Unit
) {
    if (!isEditing) {
        IconButton(onClick = onStartEditing) {
            Icon(Icons.Default.Edit, contentDescription = "Editar")
        }
    } else {
        Row {
            IconButton(
                onClick = onCancelEditing,
                enabled = !isLoading
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cancelar")
            }
            
            IconButton(
                onClick = onSaveProfile,
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

@Composable
private fun ProfileContent(
    profile: UserProfile,
    isEditing: Boolean,
    onProfileChange: (UserProfile) -> Unit,
    onAvatarChange: (String) -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Personal Information Card
        PersonalInfoCard(
            profile = profile,
            isEditing = isEditing,
            onProfileChange = onProfileChange,
            onAvatarChange = onAvatarChange
        )
        
        // Help & Support Card
        ActionCard(
            title = "Ayuda y Soporte",
            icon = Icons.Default.Help,
            onClick = onHelpClick
        )
        
        // Logout Card
        ActionCard(
            title = "Cerrar Sesión",
            icon = Icons.Default.ExitToApp,
            isDestructive = true,
            onClick = onLogoutClick
        )
    }
}

@Composable
private fun PersonalInfoCard(
    profile: UserProfile,
    isEditing: Boolean,
    onProfileChange: (UserProfile) -> Unit,
    onAvatarChange: (String) -> Unit,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Información Personal",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // Avatar and Name Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AvatarSelector(
                    currentAvatarUrl = profile.avatarUrl,
                    onAvatarSelected = onAvatarChange,
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
                        Text(
                            text = "Nombre",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
    icon: ImageVector,
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
    icon: ImageVector,
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

@Preview(showBackground = true)
@Composable
fun EnhancedProfileScreenPreview() {
    MaterialTheme {
        EnhancedProfileScreen()
    }
}