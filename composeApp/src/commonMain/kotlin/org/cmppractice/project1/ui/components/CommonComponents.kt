package org.cmppractice.project1.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Design System Colors
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
    val SpacingExtraLarge = 32.dp
    val TextSizeSmall = 12.sp
    val TextSizeMedium = 16.sp
    val TextSizeLarge = 20.sp
    val TextSizeHeading = 32.sp
}

// Additional design tokens for screen files
object Spacing {
    val Small = 8.dp
    val Medium = 16.dp
    val Large = 24.dp
    val ExtraLarge = 32.dp
}

object FontSize {
    val Small = 12.sp
    val Medium = 16.sp
    val Large = 20.sp
    val ExtraLarge = 24.sp
}

object CornerRadius {
    val Small = 8.dp
    val Medium = 16.dp
    val Large = 24.dp
}

object Elevation {
    val Small = 4.dp
    val Medium = 8.dp
    val Large = 16.dp
}

// Additional colors for screen files
object GradientStart {
    val value = Color(0xFF020024)
}

object GradientMiddle {
    val value = Color(0xFF090979)
}

object GradientEnd {
    val value = Color(0xFF03045E)
}

object BackgroundLight {
    val value = Color(0xFF001845)
}

object BackgroundDark {
    val value = Color(0xFF000C1F)
}

object SurfaceLight {
    val value = Color(0xFF0077B6)
}

object PrimaryLight {
    val value = Color(0xFF00B4D8)
}

object SecondaryVariant {
    val value = Color(0xFFCAF0F8)
}

object ButtonVariantColor {
    val value = Color(0xFF00B4D8)
}

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        backgroundColor = AppColors.CardBackground,
        elevation = DesignTokens.CardElevation,
        shape = RoundedCornerShape(DesignTokens.CardCornerRadius)
    ) {
        content()
    }
}

@Composable
fun AnimatedStatBox(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressedState by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressedState) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.7f)
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { isPressed = !isPressed },
        backgroundColor = AppColors.CardBackground,
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = AppColors.TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = AppColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

enum class ButtonVariant {
    PRIMARY, SECONDARY, ACCENT
}

@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    enabled: Boolean = true
) {
    val colors = when (variant) {
        ButtonVariant.PRIMARY -> ButtonDefaults.buttonColors(
            backgroundColor = AppColors.Primary,
            contentColor = AppColors.TextPrimary
        )
        ButtonVariant.SECONDARY -> ButtonDefaults.buttonColors(
            backgroundColor = AppColors.Surface,
            contentColor = AppColors.TextPrimary
        )
        ButtonVariant.ACCENT -> ButtonDefaults.buttonColors(
            backgroundColor = AppColors.Accent,
            contentColor = AppColors.TextPrimary
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        enabled = enabled,
        shape = RoundedCornerShape(DesignTokens.ButtonCornerRadius)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = DesignTokens.TextSizeMedium
        )
    }
}

@Composable
fun LoadingSpinner(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = AppColors.Primary,
        strokeWidth = 4.dp
    )
}

@Composable
fun CyberText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppColors.TextPrimary,
    fontSize: androidx.compose.ui.unit.TextUnit = DesignTokens.TextSizeMedium,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun CyberHeading(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppColors.TextPrimary
) {
    CyberText(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = DesignTokens.TextSizeHeading,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun CyberSubheading(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppColors.TextSecondary
) {
    CyberText(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = DesignTokens.TextSizeLarge,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun CyberDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier,
        color = AppColors.Surface,
        thickness = 1.dp
    )
}

@Composable
fun CyberIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = AppColors.TextPrimary,
    onClick: (() -> Unit)? = null
) {
    val iconModifier = if (onClick != null) {
        modifier.clickable { onClick() }
    } else {
        modifier
    }
    
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = iconModifier,
        tint = tint
    )
}





@Composable
fun EnhancedControlButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressedState by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressedState) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.7f)
    )

    val elevation by animateDpAsState(
        targetValue = if (isPressedState) 4.dp else 12.dp,
        animationSpec = tween(150)
    )

    Card(
        modifier = modifier
            .size(64.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        backgroundColor = AppColors.Primary,
        elevation = elevation,
        shape = CircleShape
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppColors.TextPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun PowerUpDisplay(
    powerUps: List<PowerUp>,
    onPowerUpClick: (PowerUp) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        powerUps.forEach { powerUp ->
            PowerUpIcon(
                powerUp = powerUp,
                onClick = { onPowerUpClick(powerUp) }
            )
        }
    }
}

@Composable
fun PowerUpIcon(
    powerUp: PowerUp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (powerUp) {
        PowerUp.SPEED_BOOST -> Icons.Default.Star
        PowerUp.SHIELD -> Icons.Default.Star
        PowerUp.TIME_FREEZE -> Icons.Default.Star
        PowerUp.TELEPORT -> Icons.Default.Star
        else -> Icons.Default.Star
    }
    
    Card(
        modifier = modifier
            .size(48.dp)
            .clickable { onClick() },
        backgroundColor = AppColors.Primary,
        elevation = 4.dp,
        shape = CircleShape
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = powerUp.name,
                tint = AppColors.TextPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun AchievementBadge(
    achievement: Achievement,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        backgroundColor = AppColors.Success,
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = AppColors.TextPrimary,
                modifier = Modifier.size(16.dp)
            )
            CyberText(
                text = achievement.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Additional components for screen files
@Composable
fun StatItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppColors.Primary,
                modifier = Modifier.size(20.dp)
            )
            CyberText(text = label, fontSize = FontSize.Small)
        }
        CyberText(text = value, fontSize = FontSize.Small, fontWeight = FontWeight.Bold)
    }
}


// Game-related data classes
data class PowerUp(
    val name: String,
    val description: String,
    val duration: Long
) {
    companion object {
        val SPEED_BOOST = PowerUp("Speed Boost", "Move faster", 10000L)
        val SHIELD = PowerUp("Shield", "Protection from obstacles", 15000L)
        val TIME_FREEZE = PowerUp("Time Freeze", "Freeze time", 8000L)
        val TELEPORT = PowerUp("Teleport", "Instant movement", 5000L)
    }
}

data class Achievement(
    val name: String,
    val description: String,
    val icon: ImageVector
) {
    companion object {
        val FIRST_WIN = Achievement("First Win", "Complete your first maze", Icons.Default.Star)
        val SPEED_DEMON = Achievement("Speed Demon", "Complete 5 mazes quickly", Icons.Default.Star)
        val MASTER = Achievement("Maze Master", "Complete 10 mazes", Icons.Default.Star)
    }
}

@Composable
fun FooterSection(
    pulseAlpha: Float,
    text: String = "Ready to conquer the maze?",
    modifier: Modifier = Modifier
) {
    val themeColors = MaterialTheme.colors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = themeColors.secondary.copy(alpha = pulseAlpha),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = themeColors.primary.copy(alpha = pulseAlpha),
                            shape = CircleShape
                        )
                )
            }
        }
    }
} 