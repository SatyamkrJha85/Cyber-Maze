package org.cmppractice.project1.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun StatsScreen(
    onBackPressed: () -> Unit
) {
    val themeColors = MaterialTheme.colors
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
                .size(150.dp)
                .offset(x = (-30).dp, y = 200.dp)
                .background(
                    color = themeColors.primary.copy(alpha = glowAlpha * 0.2f),
                    shape = CircleShape
                )
        )
        
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = 350.dp, y = 300.dp)
                .background(
                    color = themeColors.primaryVariant.copy(alpha = glowAlpha * 0.15f),
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
                        tint = themeColors.primaryVariant
                    )
                }

                Text(
                    text = "📊 STATISTICS",
                    style = MaterialTheme.typography.h5,
                    color = themeColors.primaryVariant,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    OverallStatsCard()
                }
                
                item {
                    ProgressSection()
                }
                
                item {
                    AchievementsSection()
                }
                
                item {
                    RecentGamesSection()
                }
            }
        }
    }
}

@Composable
fun OverallStatsCard() {
    val themeColors = MaterialTheme.colors
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
                text = "🏆 OVERALL PERFORMANCE",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Games Played", "42", Icons.Default.PlayArrow, Color(0xFF4CAF50))
                StatItem("Best Score", "1,250", Icons.Default.Star, Color(0xFFFFD700))
                StatItem("Avg Time", "2:34", Icons.Default.Star, Color(0xFF00B4D8))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Levels", "15", Icons.Default.Star, Color(0xFF9C27B0))
                StatItem("Win Rate", "78%", Icons.Default.Star, Color(0xFF4CAF50))
                StatItem("Combo", "8x", Icons.Default.Star, Color(0xFFFF9800))
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, icon: ImageVector, color: Color) {
    val themeColors = MaterialTheme.colors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(28.dp)
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
            color = themeColors.secondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProgressSection() {
    val themeColors = MaterialTheme.colors
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
                text = "📈 PROGRESS TRACKING",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ProgressItem("Level Progress", 15, 20, Color(0xFF4CAF50))
            ProgressItem("Score Target", 1250, 2000, Color(0xFFFFD700))
            ProgressItem("Speed Challenge", 8, 10, Color(0xFF00B4D8))
        }
    }
}

@Composable
fun ProgressItem(label: String, current: Int, target: Int, color: Color) {
    val themeColors = MaterialTheme.colors
    val progress = current.toFloat() / target.toFloat()
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.body2,
                color = themeColors.secondary
            )
            
            Text(
                text = "$current/$target",
                style = MaterialTheme.typography.body2,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            backgroundColor = Color(0xFF001845),
            color = color
        )
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun AchievementsSection() {
    val themeColors = MaterialTheme.colors
    val achievements = listOf(
        Achievement("First Victory", "Complete your first maze", true, Icons.Default.Star),
        Achievement("Speed Runner", "Complete 5 mazes under 2 minutes", true, Icons.Default.Star),
        Achievement("Combo Master", "Get a 10x combo", false, Icons.Default.Star),
        Achievement("Perfect Score", "Score 2000+ points", false, Icons.Default.Star),
        Achievement("Level Master", "Complete all 20 levels", false, Icons.Default.Star)
    )
    
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
                text = "🏅 ACHIEVEMENTS",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            achievements.forEach { achievement ->
                AchievementItem(achievement)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AchievementItem(achievement: Achievement) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = achievement.icon,
            contentDescription = achievement.title,
            tint = if (achievement.unlocked) Color(0xFF4CAF50) else Color(0xFF666666),
            modifier = Modifier.size(24.dp)
        )
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = achievement.title,
                style = MaterialTheme.typography.body2,
                color = if (achievement.unlocked) Color(0xFF90E0EF) else Color(0xFF666666),
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = achievement.description,
                style = MaterialTheme.typography.caption,
                color = if (achievement.unlocked) Color(0xFFCAF0F8) else Color(0xFF444444)
            )
        }
        
        if (achievement.unlocked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Unlocked",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun RecentGamesSection() {
    val themeColors = MaterialTheme.colors
    val recentGames = listOf(
        RecentGame("Level 15", "1,250 pts", "2:34", true),
        RecentGame("Level 14", "1,180 pts", "2:45", true),
        RecentGame("Level 13", "1,100 pts", "3:12", false),
        RecentGame("Level 12", "980 pts", "2:58", true)
    )
    
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
                text = "🎮 RECENT GAMES",
                style = MaterialTheme.typography.h6,
                color = themeColors.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            recentGames.forEach { game ->
                RecentGameItem(game)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RecentGameItem(game: RecentGame) {
    val themeColors = MaterialTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (game.won) Icons.Default.Star else Icons.Default.Star,
                contentDescription = if (game.won) "Won" else "Lost",
                tint = if (game.won) Color(0xFF4CAF50) else Color(0xFFF44336),
                modifier = Modifier.size(20.dp)
            )
            
            Column {
                Text(
                    text = game.level,
                    style = MaterialTheme.typography.body2,
                    color = themeColors.primaryVariant,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = game.score,
                    style = MaterialTheme.typography.caption,
                    color = themeColors.secondary
                )
            }
        }
        
        Text(
            text = game.time,
            style = MaterialTheme.typography.body2,
            color = themeColors.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

data class Achievement(
    val title: String,
    val description: String,
    val unlocked: Boolean,
    val icon: ImageVector
)

data class RecentGame(
    val level: String,
    val score: String,
    val time: String,
    val won: Boolean
) 