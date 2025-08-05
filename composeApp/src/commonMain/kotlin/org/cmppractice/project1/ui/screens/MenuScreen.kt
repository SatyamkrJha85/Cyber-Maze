package org.cmppractice.project1.ui.screens

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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cmppractice.project1.ui.components.FooterSection

@Composable
fun MenuScreen(
    onNavigateToGame: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit,
    gameState: GameState? = null
) {
    val themeColors = MaterialTheme.colors
    val infiniteTransition = rememberInfiniteTransition()
    
    // Animated values
    val titleScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
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
        // Animated background orbs
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-50).dp, y = 100.dp)
                .background(
                    color = themeColors.primary.copy(alpha = glowAlpha * 0.3f),
                    shape = CircleShape
                )
        )
        
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(x = 300.dp, y = 400.dp)
                .background(
                    color = themeColors.primaryVariant.copy(alpha = glowAlpha * 0.2f),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // Animated title with glow effect
            Box(
                modifier = Modifier
                    .scale(titleScale)
                    .graphicsLayer {
                        shadowElevation = 20f
                        shape = RoundedCornerShape(16.dp)
                    }
            ) {
                Text(
                    text = "🎮",
                    fontSize = 80.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Glow effect
                Text(
                    text = "🎮",
                    fontSize = 80.sp,
                    color = themeColors.primary.copy(alpha = glowAlpha),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "CYBER MAZE",
                style = MaterialTheme.typography.h3,
                color = themeColors.primaryVariant,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer {
                    shadowElevation = 15f
                }
            )
            
            Text(
                text = "Navigate the Digital Labyrinth",
                style = MaterialTheme.typography.body1,
                color = themeColors.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Game stats card
            GameStatsCard()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Enhanced menu buttons
            EnhancedMenuButton(
                text = "PLAY GAME",
                icon = Icons.Default.PlayArrow,
                onClick = onNavigateToGame,
                isPrimary = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EnhancedMenuButton(
                    text = "STATS",
                    icon = Icons.Default.Star,
                    onClick = onNavigateToStats,
                    modifier = Modifier.weight(1f)
                )
                
                EnhancedMenuButton(
                    text = "SETTINGS",
                    icon = Icons.Default.Settings,
                    onClick = onNavigateToSettings,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Footer with animated elements
            FooterSection(pulseAlpha, "Ready to conquer the maze?", Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun GameStatsCard(gameState: GameState? = null) {
    val themeColors = MaterialTheme.colors
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = themeColors.surface.copy(alpha = 0.8f),
        elevation = 16.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "GAME STATS",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    "Levels", 
                    gameState?.level?.toString() ?: "1", 
                    Icons.Default.Star
                )
                StatItem(
                    "Score", 
                    gameState?.score?.toString() ?: "0", 
                    Icons.Default.Star
                )
                StatItem(
                    "Wins", 
                    gameState?.gamesWon?.toString() ?: "0", 
                    Icons.Default.Star
                )
            }
            
            if (gameState?.achievements?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "🏆 ${gameState.achievements.size} Achievements Unlocked",
                    style = MaterialTheme.typography.caption,
                    color = themeColors.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, icon: ImageVector) {
    val themeColors = MaterialTheme.colors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = themeColors.primaryVariant,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.h6,
            color = themeColors.primaryVariant,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = themeColors.secondary
        )
    }
}

@Composable
fun EnhancedMenuButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    val themeColors = MaterialTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f)
    )
    
    Card(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        backgroundColor = if (isPrimary) themeColors.primary else themeColors.surface.copy(alpha = 0.8f),
        elevation = if (isPrimary) 20.dp else 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (isPrimary) Color.White else themeColors.primaryVariant,
                modifier = Modifier.size(24.dp)
            )
            
            Text(
                text = text,
                style = MaterialTheme.typography.button,
                color = if (isPrimary) Color.White else themeColors.primaryVariant,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

 