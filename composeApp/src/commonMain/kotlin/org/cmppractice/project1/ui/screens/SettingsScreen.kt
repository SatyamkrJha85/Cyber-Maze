package org.cmppractice.project1.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cmppractice.project1.ui.components.FooterSection

// Game Settings Data Class
data class GameSettings(
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val darkModeEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val autoSaveEnabled: Boolean = true,
    val difficultyLevel: String = "Medium",
    val selectedTheme: String = "Cyber",
    val musicVolume: Float = 0.7f,
    val sfxVolume: Float = 0.8f,
    val showHints: Boolean = true,
    val particleEffects: Boolean = true,
    val smoothAnimations: Boolean = true
)

// Theme Configuration
sealed class GameTheme {
    object Cyber : GameTheme()
    object Neon : GameTheme()
    object Classic : GameTheme()
    object Dark : GameTheme()
    
    fun getColors(): ThemeColors {
        return when (this) {
            is Cyber -> ThemeColors(
                primary = Color(0xFF00B4D8),
                secondary = Color(0xFF90E0EF),
                background = Color(0xFF03045E),
                surface = Color(0xFF001845),
                accent = Color(0xFFFF9800)
            )
            is Neon -> ThemeColors(
                primary = Color(0xFF00FF88),
                secondary = Color(0xFF88FF00),
                background = Color(0xFF000000),
                surface = Color(0xFF111111),
                accent = Color(0xFFFF0088)
            )
            is Classic -> ThemeColors(
                primary = Color(0xFF2196F3),
                secondary = Color(0xFF64B5F6),
                background = Color(0xFF1976D2),
                surface = Color(0xFF1565C0),
                accent = Color(0xFFFF5722)
            )
            is Dark -> ThemeColors(
                primary = Color(0xFF424242),
                secondary = Color(0xFF757575),
                background = Color(0xFF212121),
                surface = Color(0xFF303030),
                accent = Color(0xFF9C27B0)
            )
        }
    }
}

data class ThemeColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val accent: Color
)

// Difficulty Configuration
sealed class Difficulty {
    object Easy : Difficulty()
    object Medium : Difficulty()
    object Hard : Difficulty()
    object Expert : Difficulty()
    
    fun getMazeSize(): Int {
        return when (this) {
            is Easy -> 8
            is Medium -> 12
            is Hard -> 16
            is Expert -> 20
        }
    }
    
    fun getTimeLimit(): Int {
        return when (this) {
            is Easy -> 300 // 5 minutes
            is Medium -> 240 // 4 minutes
            is Hard -> 180 // 3 minutes
            is Expert -> 120 // 2 minutes
        }
    }
    
    fun getObstacleCount(): Int {
        return when (this) {
            is Easy -> 2
            is Medium -> 4
            is Hard -> 6
            is Expert -> 8
        }
    }
}

@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    onSettingsChanged: (GameSettings) -> Unit = {},
    currentSettings: GameSettings = GameSettings()
) {
    var settings by remember { mutableStateOf(currentSettings) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showThemePreview by remember { mutableStateOf(false) }
    
    val infiniteTransition = rememberInfiniteTransition()
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Apply theme colors based on selected theme
    val themeColors = when (settings.selectedTheme) {
        "Cyber" -> GameTheme.Cyber.getColors()
        "Neon" -> GameTheme.Neon.getColors()
        "Classic" -> GameTheme.Classic.getColors()
        "Dark" -> GameTheme.Dark.getColors()
        else -> GameTheme.Cyber.getColors()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        themeColors.background,
                        themeColors.background.copy(alpha = 0.8f),
                        themeColors.surface
                    )
                )
            )
    ) {
        // Animated background elements
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = (-20).dp, y = 150.dp)
                .background(
                    color = themeColors.primary.copy(alpha = glowAlpha * 0.2f),
                    shape = CircleShape
                )
        )
        
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = 320.dp, y = 250.dp)
                .background(
                    color = themeColors.secondary.copy(alpha = glowAlpha * 0.15f),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .background(
                            color = themeColors.surface.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = themeColors.secondary
                    )
                }

                Text(
                    text = "⚙️ SETTINGS",
                    style = MaterialTheme.typography.h5,
                    color = themeColors.secondary,
                    fontWeight = FontWeight.ExtraBold
                )

                IconButton(
                    onClick = { showResetDialog = true },
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF44336).copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Settings",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AudioSettingsCard(
                        settings = settings,
                        onSettingsChanged = { newSettings ->
                            settings = newSettings
                            onSettingsChanged(newSettings)
                        },
                        themeColors = themeColors
                    )
                }
                
                item {
                    GameplaySettingsCard(
                        settings = settings,
                        onSettingsChanged = { newSettings ->
                            settings = newSettings
                            onSettingsChanged(newSettings)
                        },
                        themeColors = themeColors
                    )
                }
                
                item {
                    DifficultySettingsCard(
                        difficultyLevel = settings.difficultyLevel,
                        onDifficultyChanged = { difficulty ->
                            settings = settings.copy(difficultyLevel = difficulty)
                            onSettingsChanged(settings)
                        },
                        themeColors = themeColors
                    )
                }
                
                item {
                    ThemeSettingsCard(
                        selectedTheme = settings.selectedTheme,
                        onThemeChanged = { theme ->
                            settings = settings.copy(selectedTheme = theme)
                            onSettingsChanged(settings)
                        },
                        onThemePreview = { showThemePreview = true },
                        themeColors = themeColors
                    )
                }
                
                item {
                    DisplaySettingsCard(
                        settings = settings,
                        onSettingsChanged = { newSettings ->
                            settings = newSettings
                            onSettingsChanged(newSettings)
                        },
                        themeColors = themeColors
                    )
                }
                
                item {
                    AdvancedSettingsCard(
                        settings = settings,
                        onSettingsChanged = { newSettings ->
                            settings = newSettings
                            onSettingsChanged(newSettings)
                        },
                        themeColors = themeColors
                    )
                }
                
                item {
                    FooterSection(pulseAlpha, "Settings saved automatically")
                }
            }
        }
        
        // Reset Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset Settings", color = themeColors.secondary) },
                text = { Text("Are you sure you want to reset all settings to default?", color = themeColors.secondary) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            settings = GameSettings()
                            onSettingsChanged(settings)
                            showResetDialog = false
                        }
                    ) {
                        Text("Reset", color = Color(0xFFF44336))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel", color = themeColors.primary)
                    }
                },
                backgroundColor = themeColors.surface,
                shape = RoundedCornerShape(16.dp)
            )
        }
        
        // Theme Preview Dialog
        if (showThemePreview) {
            ThemePreviewDialog(
                theme = settings.selectedTheme,
                onDismiss = { showThemePreview = false },
                themeColors = themeColors
            )
        }
    }
}

@Composable
fun AudioSettingsCard(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit,
    themeColors: ThemeColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = themeColors.surface.copy(alpha = 0.9f),
        elevation = 16.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🔊 AUDIO SETTINGS",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingToggle(
                title = "Sound Effects",
                subtitle = "Enable game sound effects",
                icon = Icons.Default.Star,
                enabled = settings.soundEnabled,
                onChanged = { enabled ->
                    onSettingsChanged(settings.copy(soundEnabled = enabled))
                },
                themeColors = themeColors
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            VolumeSlider(
                title = "Music Volume",
                volume = settings.musicVolume,
                onVolumeChanged = { volume ->
                    onSettingsChanged(settings.copy(musicVolume = volume))
                },
                icon = Icons.Default.Star,
                themeColors = themeColors
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            VolumeSlider(
                title = "SFX Volume",
                volume = settings.sfxVolume,
                onVolumeChanged = { volume ->
                    onSettingsChanged(settings.copy(sfxVolume = volume))
                },
                icon = Icons.Default.Star,
                themeColors = themeColors
            )
        }
    }
}

@Composable
fun GameplaySettingsCard(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit,
    themeColors: ThemeColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = themeColors.surface.copy(alpha = 0.9f),
        elevation = 12.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🎮 GAMEPLAY SETTINGS",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingToggle(
                title = "Vibration",
                subtitle = "Haptic feedback on actions",
                icon = Icons.Default.Star,
                enabled = settings.vibrationEnabled,
                onChanged = { enabled ->
                    onSettingsChanged(settings.copy(vibrationEnabled = enabled))
                },
                themeColors = themeColors
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingToggle(
                title = "Notifications",
                subtitle = "Game notifications and alerts",
                icon = Icons.Default.Star,
                enabled = settings.notificationsEnabled,
                onChanged = { enabled ->
                    onSettingsChanged(settings.copy(notificationsEnabled = enabled))
                },
                themeColors = themeColors
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingToggle(
                title = "Auto Save",
                subtitle = "Automatically save progress",
                icon = Icons.Default.Star,
                enabled = settings.autoSaveEnabled,
                onChanged = { enabled ->
                    onSettingsChanged(settings.copy(autoSaveEnabled = enabled))
                },
                themeColors = themeColors
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingToggle(
                title = "Show Hints",
                subtitle = "Display helpful hints during gameplay",
                icon = Icons.Default.Star,
                enabled = settings.showHints,
                onChanged = { enabled ->
                    onSettingsChanged(settings.copy(showHints = enabled))
                },
                themeColors = themeColors
            )
        }
    }
}

@Composable
fun DifficultySettingsCard(
    difficultyLevel: String,
    onDifficultyChanged: (String) -> Unit,
    themeColors: ThemeColors
) {
    val difficulties = listOf("Easy", "Medium", "Hard", "Expert")
    val difficulty = when (difficultyLevel) {
        "Easy" -> Difficulty.Easy
        "Medium" -> Difficulty.Medium
        "Hard" -> Difficulty.Hard
        "Expert" -> Difficulty.Expert
        else -> Difficulty.Medium
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = themeColors.surface.copy(alpha = 0.9f),
        elevation = 12.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🎯 DIFFICULTY SETTINGS",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                difficulties.forEach { diff ->
                    DifficultyButton(
                        text = diff,
                        selected = difficultyLevel == diff,
                        onClick = { onDifficultyChanged(diff) },
                        themeColors = themeColors
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Difficulty Info
            Card(
                backgroundColor = themeColors.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Current Difficulty: $difficultyLevel",
                        style = MaterialTheme.typography.body2,
                        color = themeColors.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Maze Size: ${difficulty.getMazeSize()}x${difficulty.getMazeSize()}",
                        style = MaterialTheme.typography.caption,
                        color = themeColors.secondary
                    )
                    
                    Text(
                        text = "Time Limit: ${difficulty.getTimeLimit() / 60} minutes",
                        style = MaterialTheme.typography.caption,
                        color = themeColors.secondary
                    )
                    
                    Text(
                        text = "Obstacles: ${difficulty.getObstacleCount()}",
                        style = MaterialTheme.typography.caption,
                        color = themeColors.secondary
                    )
                }
            }
        }
    }
}

@Composable
fun DifficultyButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    themeColors: ThemeColors
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f)
    )
    
    Card(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        backgroundColor = if (selected) themeColors.primary else themeColors.surface.copy(alpha = 0.6f),
        elevation = if (selected) 8.dp else 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = if (selected) Color.White else themeColors.secondary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ThemeSettingsCard(
    selectedTheme: String,
    onThemeChanged: (String) -> Unit,
    onThemePreview: () -> Unit,
    themeColors: ThemeColors
) {
    val themes = listOf("Cyber", "Neon", "Classic", "Dark")
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = themeColors.surface.copy(alpha = 0.9f),
        elevation = 12.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎨 THEME SETTINGS",
                    style = MaterialTheme.typography.h6,
                    color = themeColors.primary,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(onClick = onThemePreview) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Preview Theme",
                        tint = themeColors.secondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                themes.forEach { theme ->
                    ThemeButton(
                        text = theme,
                        selected = selectedTheme == theme,
                        onClick = { onThemeChanged(theme) },
                        themeColors = themeColors
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    themeColors: ThemeColors
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f)
    )
    
    Card(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        backgroundColor = if (selected) themeColors.accent else themeColors.surface.copy(alpha = 0.6f),
        elevation = if (selected) 8.dp else 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = if (selected) Color.White else themeColors.secondary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DisplaySettingsCard(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit,
    themeColors: ThemeColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = themeColors.surface.copy(alpha = 0.9f),
        elevation = 12.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🌙 DISPLAY SETTINGS",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingToggle(
                title = "Dark Mode",
                subtitle = "Use dark theme interface",
                icon = Icons.Default.Star,
                enabled = settings.darkModeEnabled,
                onChanged = { enabled ->
                    onSettingsChanged(settings.copy(darkModeEnabled = enabled))
                },
                themeColors = themeColors
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingToggle(
                title = "Particle Effects",
                subtitle = "Show particle effects during gameplay",
                icon = Icons.Default.Star,
                enabled = settings.particleEffects,
                onChanged = { enabled ->
                    onSettingsChanged(settings.copy(particleEffects = enabled))
                },
                themeColors = themeColors
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingToggle(
                title = "Smooth Animations",
                subtitle = "Enable smooth transitions and animations",
                icon = Icons.Default.Star,
                enabled = settings.smoothAnimations,
                onChanged = { enabled ->
                    onSettingsChanged(settings.copy(smoothAnimations = enabled))
                },
                themeColors = themeColors
            )
        }
    }
}

@Composable
fun AdvancedSettingsCard(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit,
    themeColors: ThemeColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = themeColors.surface.copy(alpha = 0.9f),
        elevation = 12.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🔧 ADVANCED SETTINGS",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "These settings affect game performance and behavior",
                style = MaterialTheme.typography.caption,
                color = themeColors.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SettingToggle(
    title: String,
    subtitle: String,
    icon: ImageVector,
    enabled: Boolean,
    onChanged: (Boolean) -> Unit,
    themeColors: ThemeColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = themeColors.secondary,
                modifier = Modifier.size(24.dp)
            )
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1,
                    color = themeColors.secondary,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.caption,
                    color = themeColors.secondary.copy(alpha = 0.7f)
                )
            }
        }

        Switch(
            checked = enabled,
            onCheckedChange = onChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = themeColors.primary,
                checkedTrackColor = themeColors.secondary,
                uncheckedThumbColor = Color(0xFF666666),
                uncheckedTrackColor = Color(0xFF444444)
            )
        )
    }
}

@Composable
fun VolumeSlider(
    title: String,
    volume: Float,
    onVolumeChanged: (Float) -> Unit,
    icon: ImageVector,
    themeColors: ThemeColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = themeColors.secondary,
            modifier = Modifier.size(20.dp)
        )
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body2,
                color = themeColors.secondary
            )
            
            Slider(
                value = volume,
                onValueChange = onVolumeChanged,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = themeColors.primary,
                    activeTrackColor = themeColors.secondary,
                    inactiveTrackColor = Color(0xFF444444)
                )
            )
        }
        
        Text(
            text = "${(volume * 100).toInt()}%",
            style = MaterialTheme.typography.body2,
            color = themeColors.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ThemePreviewDialog(
    theme: String,
    onDismiss: () -> Unit,
    themeColors: ThemeColors
) {
    val previewColors = when (theme) {
        "Cyber" -> GameTheme.Cyber.getColors()
        "Neon" -> GameTheme.Neon.getColors()
        "Classic" -> GameTheme.Classic.getColors()
        "Dark" -> GameTheme.Dark.getColors()
        else -> GameTheme.Cyber.getColors()
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Theme Preview: $theme",
                color = previewColors.secondary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    "This theme will change the game's visual appearance",
                    color = previewColors.secondary,
                    style = MaterialTheme.typography.body2
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Theme preview elements
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(previewColors.primary, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(previewColors.secondary, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(previewColors.accent, CircleShape)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = previewColors.primary)
            }
        },
        backgroundColor = previewColors.surface,
        shape = RoundedCornerShape(16.dp)
    )
}

 