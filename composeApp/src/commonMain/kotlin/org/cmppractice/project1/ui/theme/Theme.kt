package org.cmppractice.project1.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object AppColors {
    val Primary = Color(0xFF00B4D8)
    val PrimaryVariant = Color(0xFF90E0EF)
    val Secondary = Color(0xFFCAF0F8)
    val Background = Color(0xFF03045E)
    val Surface = Color(0xFF0077B6)
    val CardBackground = Color(0xFF001845)
    val TextPrimary = Color(0xFF90E0EF)
    val TextSecondary = Color(0xFFCAF0F8)
    val Accent = Color(0xFFFF9800)
    val Success = Color(0xFF4CAF50)
    val Error = Color(0xFFF44336)
}

object DesignTokens {
    val CardElevation = 16.dp
    val CardCornerRadius = 24.dp
    val ButtonCornerRadius = 16.dp
    val SpacingSmall = 8.dp
    val SpacingMedium = 16.dp
    val SpacingLarge = 24.dp
    val TextSizeSmall = 12.sp
    val TextSizeMedium = 16.sp
    val TextSizeLarge = 20.sp
    val TextSizeHeading = 32.sp
}

val DarkTheme = darkColors(
    primary = AppColors.Primary,
    primaryVariant = AppColors.PrimaryVariant,
    secondary = AppColors.Secondary,
    background = AppColors.Background,
    surface = AppColors.Surface,
    onPrimary = AppColors.TextPrimary,
    onSecondary = AppColors.TextPrimary,
    onBackground = AppColors.TextPrimary,
    onSurface = AppColors.TextPrimary
)

@Composable
fun CyberMazeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkTheme,
        content = content
    )
} 