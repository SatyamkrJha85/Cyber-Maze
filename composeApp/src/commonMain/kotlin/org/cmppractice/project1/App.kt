import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import org.cmppractice.project1.ui.screens.MenuScreen
import org.cmppractice.project1.ui.screens.SettingsScreen
import org.cmppractice.project1.ui.screens.StatsScreen
import org.cmppractice.project1.ui.screens.GameSettings

enum class Screen {
    MENU, GAME, SETTINGS, STATS
}

// Enhanced Game State
data class GameState(
    val level: Int = 1,
    val score: Int = 0,
    val moves: Int = 0,
    val combo: Int = 0,
    val timeRemaining: Int = 300,
    val powerUps: List<PowerUp> = emptyList(),
    val achievements: Set<Achievement> = emptySet(),
    val bestScore: Int = Int.MAX_VALUE,
    val totalPlayTime: Long = 0L,
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0
)

// Power-up System
sealed class PowerUp {
    object SpeedBoost : PowerUp()
    object TimeFreeze : PowerUp()
    object ComboMultiplier : PowerUp()
    object WallBreaker : PowerUp()
    object Teleporter : PowerUp()
    
    fun getIcon(): ImageVector {
        return when (this) {
            is SpeedBoost -> Icons.Default.FlashOn
            is TimeFreeze -> Icons.Default.Timer
            is ComboMultiplier -> Icons.Default.Star
            is WallBreaker -> Icons.Default.Build
            is Teleporter -> Icons.Default.LocationOn
        }
    }
    
    fun getColor(): Color {
        return when (this) {
            is SpeedBoost -> Color(0xFFFF9800)
            is TimeFreeze -> Color(0xFF2196F3)
            is ComboMultiplier -> Color(0xFFFFEB3B)
            is WallBreaker -> Color(0xFFF44336)
            is Teleporter -> Color(0xFF9C27B0)
        }
    }
    
    fun getDuration(): Int {
        return when (this) {
            is SpeedBoost -> 10
            is TimeFreeze -> 5
            is ComboMultiplier -> 15
            is WallBreaker -> 8
            is Teleporter -> 3
        }
    }
}

// Achievement System
sealed class Achievement {
    object FirstVictory : Achievement()
    object SpeedRunner : Achievement()
    object ComboMaster : Achievement()
    object PowerUpCollector : Achievement()
    object PerfectScore : Achievement()
    object TimeMaster : Achievement()
    object LevelMaster : Achievement()
    
    fun getTitle(): String {
        return when (this) {
            is FirstVictory -> "First Victory"
            is SpeedRunner -> "Speed Runner"
            is ComboMaster -> "Combo Master"
            is PowerUpCollector -> "Power Up Collector"
            is PerfectScore -> "Perfect Score"
            is TimeMaster -> "Time Master"
            is LevelMaster -> "Level Master"
        }
    }
    
    fun getDescription(): String {
        return when (this) {
            is FirstVictory -> "Complete your first maze"
            is SpeedRunner -> "Complete a maze in under 30 seconds"
            is ComboMaster -> "Get a 10x combo"
            is PowerUpCollector -> "Collect 5 power-ups in one game"
            is PerfectScore -> "Score 2000+ points"
            is TimeMaster -> "Complete 10 mazes with time remaining"
            is LevelMaster -> "Reach level 20"
        }
    }
    
    fun getIcon(): ImageVector = Icons.Default.Star
    fun getColor(): Color = Color(0xFFFFD700)
}

// Game Modes
enum class GameMode {
    CLASSIC, TIME_ATTACK, SURVIVAL, PUZZLE
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.MENU) }
    var gameSettings by remember { mutableStateOf(GameSettings()) }
    var gameState by remember { mutableStateOf(GameState()) }
    val game = remember { Game(12, 12) }
    
    // Apply theme based on settings
    val themeColors = when (gameSettings.selectedTheme) {
        "Cyber" -> darkColors(
            primary = Color(0xFF00B4D8),
            primaryVariant = Color(0xFF90E0EF),
            secondary = Color(0xFFCAF0F8),
            background = Color(0xFF03045E),
            surface = Color(0xFF0077B6)
        )
        "Neon" -> darkColors(
            primary = Color(0xFF00FF88),
            primaryVariant = Color(0xFF88FF00),
            secondary = Color(0xFFFF0088),
            background = Color(0xFF000000),
            surface = Color(0xFF111111)
        )
        "Classic" -> darkColors(
            primary = Color(0xFF2196F3),
            primaryVariant = Color(0xFF64B5F6),
            secondary = Color(0xFFFF5722),
            background = Color(0xFF1976D2),
            surface = Color(0xFF1565C0)
        )
        "Dark" -> darkColors(
            primary = Color(0xFF424242),
            primaryVariant = Color(0xFF757575),
            secondary = Color(0xFF9C27B0),
            background = Color(0xFF212121),
            surface = Color(0xFF303030)
        )
        else -> darkColors(
            primary = Color(0xFF00B4D8),
            primaryVariant = Color(0xFF90E0EF),
            secondary = Color(0xFFCAF0F8),
            background = Color(0xFF03045E),
            surface = Color(0xFF0077B6)
        )
    }

    MaterialTheme(colors = themeColors) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            when (currentScreen) {
                Screen.MENU -> MenuScreen(
                    onNavigateToGame = { currentScreen = Screen.GAME },
                    onNavigateToStats = { currentScreen = Screen.STATS },
                    onNavigateToSettings = { currentScreen = Screen.SETTINGS },
                    gameState = gameState
                )
                Screen.GAME -> MazeGameScreen(
                    game = game,
                    onBackToMenu = { currentScreen = Screen.MENU },
                    settings = gameSettings,
                    gameState = gameState,
                    onGameStateChanged = { gameState = it }
                )
                Screen.SETTINGS -> SettingsScreen(
                    onBackPressed = { currentScreen = Screen.MENU },
                    onSettingsChanged = { settings ->
                        gameSettings = settings
                        // Apply difficulty settings to game
                        val difficulty = when (settings.difficultyLevel) {
                            "Easy" -> 8
                            "Medium" -> 12
                            "Hard" -> 16
                            "Expert" -> 20
                            else -> 12
                        }
                        game.updateMazeSize(difficulty, difficulty)
                    },
                    currentSettings = gameSettings
                )
                Screen.STATS -> StatsScreen(
                    onBackPressed = { currentScreen = Screen.MENU },
                    gameState = gameState
                )
            }
        }
    }
}

@Composable
fun MazeGameScreen(
    game: Game, 
    onBackToMenu: () -> Unit, 
    settings: GameSettings = GameSettings(),
    gameState: GameState,
    onGameStateChanged: (GameState) -> Unit
) {
    val scope = rememberCoroutineScope()
    var playerX by remember { mutableStateOf(game.playerX) }
    var playerY by remember { mutableStateOf(game.playerY) }
    var maze by remember { mutableStateOf(game.getMaze()) }
    var showCelebration by remember { mutableStateOf(false) }
    var showTutorial by remember { mutableStateOf(true) }
    var showAchievement by remember { mutableStateOf<Achievement?>(null) }
    var showPowerUpNotification by remember { mutableStateOf<PowerUp?>(null) }
    var activePowerUps by remember { mutableStateOf<Map<PowerUp, Int>>(emptyMap()) }
    var powerUpPositions by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }
    var gameMode by remember { mutableStateOf(GameMode.CLASSIC) }
    
    // Timer system
    var timeRemaining by remember { mutableStateOf(gameState.timeRemaining) }
    var isGamePaused by remember { mutableStateOf(false) }
    
    // Enhanced game state
    var currentState by remember { mutableStateOf(gameState) }
    
    // Animated values
    val headerScale = remember { Animatable(0.8f) }
    val controlsSlide = remember { Animatable(-100f) }
    val powerUpPulse = remember { Animatable(1f) }
    
    // Timer effect
    LaunchedEffect(timeRemaining, isGamePaused) {
        if (timeRemaining > 0 && !isGamePaused) {
            delay(1000)
            timeRemaining--
            currentState = currentState.copy(timeRemaining = timeRemaining)
            onGameStateChanged(currentState)
        }
    }
    
    // Power-up pulse animation
    LaunchedEffect(Unit) {
        while (true) {
            powerUpPulse.animateTo(1.2f, tween(500))
            powerUpPulse.animateTo(1f, tween(500))
        }
    }
    
    // Generate power-ups
    LaunchedEffect(currentState.level) {
        val newPowerUps = mutableListOf<Pair<Int, Int>>()
        repeat(3) {
            val x = Random.nextInt(1, game.width - 1)
            val y = Random.nextInt(1, game.height - 1)
            if (maze[y][x] == '.') {
                newPowerUps.add(Pair(x, y))
            }
        }
        powerUpPositions = newPowerUps
    }
    
    // Update active power-ups
    LaunchedEffect(activePowerUps) {
        delay(1000)
        activePowerUps = activePowerUps.mapValues { (_, duration) -> duration - 1 }
            .filter { (_, duration) -> duration > 0 }
    }

    // Animated values
    val headerScale = remember { Animatable(0.8f) }
    val controlsSlide = remember { Animatable(-100f) }

    LaunchedEffect(Unit) {
        headerScale.animateTo(1f, animationSpec = spring(dampingRatio = 0.7f))
        controlsSlide.animateTo(0f, animationSpec = tween(500))
    }

    fun movePlayer(dx: Int, dy: Int) {
        val newX = playerX + dx
        val newY = playerY + dy
        
        // Check for power-up collection
        val powerUpIndex = powerUpPositions.indexOf(Pair(newX, newY))
        if (powerUpIndex != -1) {
            val powerUp = when (Random.nextInt(5)) {
                0 -> PowerUp.SpeedBoost
                1 -> PowerUp.TimeFreeze
                2 -> PowerUp.ComboMultiplier
                3 -> PowerUp.WallBreaker
                else -> PowerUp.Teleporter
            }
            
            activePowerUps = activePowerUps + (powerUp to powerUp.getDuration())
            powerUpPositions = powerUpPositions.filterIndexed { index, _ -> index != powerUpIndex }
            showPowerUpNotification = powerUp
            
            scope.launch {
                delay(2000)
                showPowerUpNotification = null
            }
        }
        
        // Check if move is valid (with wall breaker power-up)
        val canMove = if (activePowerUps.containsKey(PowerUp.WallBreaker)) {
            newX in 0 until game.width && newY in 0 until game.height
        } else {
            game.isValidMove(newX, newY)
        }
        
        if (canMove) {
            playerX = newX
            playerY = newY
            game.playerX = playerX
            game.playerY = playerY
            maze = game.getMaze()
            
            val newMoves = currentState.moves + 1
            val newCombo = currentState.combo + 1
            
            // Apply settings-based scoring with power-ups
            val baseScore = if (settings.soundEnabled) 10 else 8
            val comboMultiplier = if (activePowerUps.containsKey(PowerUp.ComboMultiplier)) 3 else 1
            val comboBonus = if (settings.smoothAnimations) newCombo * 5 * comboMultiplier else newCombo * 3 * comboMultiplier
            val newScore = currentState.score + baseScore + comboBonus
            
            currentState = currentState.copy(
                moves = newMoves,
                combo = newCombo,
                score = newScore
            )
            onGameStateChanged(currentState)
            
            showTutorial = false

            if (game.isPlayerAtGoal()) {
                showCelebration = true
                
                // Check for achievements
                val newAchievements = mutableSetOf<Achievement>()
                if (currentState.gamesWon == 0) newAchievements.add(Achievement.FirstVictory)
                if (timeRemaining > 270) newAchievements.add(Achievement.SpeedRunner)
                if (newCombo >= 10) newAchievements.add(Achievement.ComboMaster)
                if (newScore >= 2000) newAchievements.add(Achievement.PerfectScore)
                
                if (newAchievements.isNotEmpty()) {
                    showAchievement = newAchievements.first()
                    scope.launch {
                        delay(3000)
                        showAchievement = null
                    }
                }
                
                val updatedState = currentState.copy(
                    level = currentState.level + 1,
                    gamesWon = currentState.gamesWon + 1,
                    achievements = currentState.achievements + newAchievements,
                    timeRemaining = when (settings.difficultyLevel) {
                        "Easy" -> 300
                        "Medium" -> 240
                        "Hard" -> 180
                        "Expert" -> 120
                        else -> 240
                    }
                )
                currentState = updatedState
                onGameStateChanged(updatedState)
                
                // Apply settings-based celebration
                val celebrationDelay = if (settings.particleEffects) 2000L else 1000L
                
                scope.launch {
                    delay(celebrationDelay)
                    showCelebration = false
                    game.generateNewMaze()
                    playerX = game.playerX
                    playerY = game.playerY
                    maze = game.getMaze()
                    currentState = currentState.copy(moves = 0, combo = 0)
                    onGameStateChanged(currentState)
                    headerScale.snapTo(0.8f)
                    headerScale.animateTo(1f, spring(dampingRatio = 0.7f))
                }
            }
        } else {
            currentState = currentState.copy(combo = 0)
            onGameStateChanged(currentState)
        }
    }

    val themeColors = MaterialTheme.colors
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
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Enhanced Header with Score
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .scale(headerScale.value)
                    .graphicsLayer {
                        shadowElevation = 20f
                        shape = RoundedCornerShape(24.dp)
                    },
                backgroundColor = themeColors.surface,
                elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Back button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBackToMenu,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Menu",
                                tint = Color(0xFF90E0EF)
                            )
                        }
                        
                        Text(
                            text = "CYBER MAZE",
                            color = themeColors.primaryVariant,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.graphicsLayer {
                                shadowElevation = 10f
                            }
                        )
                        
                        Spacer(modifier = Modifier.width(40.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AnimatedStatBox(
                            title = "LEVEL",
                            value = currentState.level.toString(),
                            icon = Icons.Default.Star,
                            color = Color(0xFF00B4D8)
                        )
                        AnimatedStatBox(
                            title = "SCORE",
                            value = currentState.score.toString(),
                            icon = Icons.Default.Star,
                            color = Color(0xFF4CAF50)
                        )
                        AnimatedStatBox(
                            title = "COMBO",
                            value = currentState.combo.toString(),
                            icon = Icons.Default.Star,
                            color = Color(0xFFFF9800)
                        )
                    }
                    
                    // Timer display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedStatBox(
                            title = "TIME",
                            value = "${timeRemaining / 60}:${String.format("%02d", timeRemaining % 60)}",
                            icon = Icons.Default.Timer,
                            color = if (timeRemaining < 30) Color(0xFFF44336) else Color(0xFF2196F3)
                        )
                        
                        // Active power-ups display
                        if (activePowerUps.isNotEmpty()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                activePowerUps.forEach { (powerUp, duration) ->
                                    Icon(
                                        imageVector = powerUp.getIcon(),
                                        contentDescription = powerUp.toString(),
                                        tint = powerUp.getColor(),
                                        modifier = Modifier
                                            .size(24.dp)
                                            .scale(powerUpPulse.value)
                                    )
                                }
                            }
                        }
                    }
                    
                    if (settings.showHints) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Difficulty: ${settings.difficultyLevel}",
                            style = MaterialTheme.typography.caption,
                            color = Color(0xFF00B4D8),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Enhanced Maze Board
            Card(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .weight(1f),
                backgroundColor = themeColors.surface.copy(alpha = 0.8f),
                elevation = 24.dp,
                shape = RoundedCornerShape(32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF001845),
                                    Color(0xFF000C1F)
                                )
                            )
                        )
                ) {
                    EnhancedMazeCanvas(maze)

                    if (showCelebration) {
                        CelebrationOverlay()
                    }

                    if (showTutorial) {
                        TutorialOverlay()
                    }
                }
            }

            // Enhanced Control Pad
            Card(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = controlsSlide.value
                    },
                backgroundColor = Color(0xFF001845),
                elevation = 16.dp,
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EnhancedControlButton(
                        icon = Icons.Default.KeyboardArrowUp,
                        onClick = { movePlayer(0, -1) }
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        EnhancedControlButton(
                            icon = Icons.Default.KeyboardArrowLeft,
                            onClick = { movePlayer(-1, 0) }
                        )
                        EnhancedControlButton(
                            icon = Icons.Default.KeyboardArrowDown,
                            onClick = { movePlayer(0, 1) }
                        )
                        EnhancedControlButton(
                            icon = Icons.Default.KeyboardArrowRight,
                            onClick = { movePlayer(1, 0) }
                        )
                    }
                }
            }
        }
        
        // Achievement notification
        showAchievement?.let { achievement ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    backgroundColor = Color(0xFF4CAF50),
                    elevation = 24.dp,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = achievement.getIcon(),
                            contentDescription = "Achievement",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "🏆 ACHIEVEMENT UNLOCKED!",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = achievement.getTitle(),
                            style = MaterialTheme.typography.h5,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = achievement.getDescription(),
                            style = MaterialTheme.typography.body1,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        
        // Power-up notification
        showPowerUpNotification?.let { powerUp ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    backgroundColor = powerUp.getColor(),
                    elevation = 16.dp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = powerUp.getIcon(),
                            contentDescription = "Power Up",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        
                        Column {
                            Text(
                                text = "POWER UP COLLECTED!",
                                style = MaterialTheme.typography.body1,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Text(
                                text = when (powerUp) {
                                    is PowerUp.SpeedBoost -> "Speed Boost"
                                    is PowerUp.TimeFreeze -> "Time Freeze"
                                    is PowerUp.ComboMultiplier -> "Combo Multiplier"
                                    is PowerUp.WallBreaker -> "Wall Breaker"
                                    is PowerUp.Teleporter -> "Teleporter"
                                },
                                style = MaterialTheme.typography.caption,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedMazeCanvas(maze: Array<CharArray>) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAnimation = infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glowAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cellSize = size.width / maze.size.toFloat()

        // Enhanced background with cyber grid pattern
        drawCyberGrid(cellSize)

        maze.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                val center = Offset(
                    (x * cellSize) + (cellSize / 2),
                    (y * cellSize) + (cellSize / 2)
                )

                when (cell) {
                    '#' -> drawWall(x, y, cellSize, pulseAnimation.value)
                    'P' -> drawPlayer(center, cellSize, pulseAnimation.value, glowAnimation.value)
                    'G' -> drawGoal(center, cellSize, pulseAnimation.value, glowAnimation.value)
                }
            }
        }
    }
}

private fun DrawScope.drawCyberGrid(cellSize: Float) {
    // Draw main grid
    for (y in 0..size.height.toInt() step cellSize.toInt()) {
        for (x in 0..size.width.toInt() step cellSize.toInt()) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF001233),
                        Color(0xFF001845)
                    ),
                    start = Offset(x.toFloat(), y.toFloat()),
                    end = Offset(x + cellSize, y + cellSize)
                ),
                topLeft = Offset(x.toFloat(), y.toFloat()),
                size = Size(cellSize - 1, cellSize - 1),
                alpha = 0.3f
            )
        }
    }

    // Draw grid lines
    for (i in 0..size.width.toInt() step (cellSize / 2).toInt()) {
        drawLine(
            color = Color(0xFF003566).copy(alpha = 0.2f),
            start = Offset(i.toFloat(), 0f),
            end = Offset(i.toFloat(), size.height),
            strokeWidth = 0.5f
        )
    }
    for (i in 0..size.height.toInt() step (cellSize / 2).toInt()) {
        drawLine(
            color = Color(0xFF003566).copy(alpha = 0.2f),
            start = Offset(0f, i.toFloat()),
            end = Offset(size.width, i.toFloat()),
            strokeWidth = 0.5f
        )
    }
}

private fun DrawScope.drawWall(x: Int, y: Int, cellSize: Float, pulseValue: Float) {
    val wallGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF023E8A),
            Color(0xFF01579B),
            Color(0xFF0277BD)
        ),
        start = Offset(x * cellSize, y * cellSize),
        end = Offset((x + 1) * cellSize, (y + 1) * cellSize)
    )

    // Wall base
    drawRect(
        brush = wallGradient,
        topLeft = Offset(x * cellSize + 1, y * cellSize + 1),
        size = Size(cellSize - 2, cellSize - 2),
    )

    // Wall highlight
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF90E0EF).copy(alpha = 0.3f),
                Color.Transparent
            ),
            center = Offset(x * cellSize + cellSize / 2, y * cellSize + cellSize / 2),
            radius = cellSize * pulseValue
        ),
        topLeft = Offset(x * cellSize + 1, y * cellSize + 1),
        size = Size(cellSize - 2, cellSize - 2),
    )
}

private fun DrawScope.drawPlayer(center: Offset, cellSize: Float, pulseValue: Float, rotationValue: Float) {
    // Outer glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFCAF0F8).copy(alpha = 0.5f),
                Color(0xFF90E0EF).copy(alpha = 0.3f),
                Color.Transparent
            ),
            center = center,
            radius = cellSize * pulseValue
        ),
        radius = cellSize / 2f,
        center = center
    )

    // Player trail effect
    rotate(rotationValue, center) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF00B4D8).copy(alpha = 0.4f),
                    Color.Transparent
                ),
                radius = cellSize * 0.8f
            ),
            radius = cellSize / 3f,
            center = center
        )
    }

    // Player core
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.White,
                Color(0xFFCAF0F8)
            )
        ),
        radius = cellSize / 4f,
        center = center
    )
}

private fun DrawScope.drawGoal(center: Offset, cellSize: Float, pulseValue: Float, rotationValue: Float) {
    // Goal outer glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF4CAF50).copy(alpha = 0.5f),
                Color(0xFF00B4D8).copy(alpha = 0.3f),
                Color.Transparent
            ),
            center = center,
            radius = cellSize * pulseValue
        ),
        radius = cellSize / 2f,
        center = center
    )

    // Rotating elements
    rotate(rotationValue, center) {
        for (i in 0 until 4) {
            rotate(90f * i, center) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4CAF50),
                            Color(0xFF00B4D8).copy(alpha = 0.7f),
                            Color.Transparent
                        )
                    ),
                    radius = cellSize / 4f,
                    center = center + Offset(cellSize / 3f, 0f)
                )
            }
        }
    }

    // Goal core
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF4CAF50),
                Color(0xFF00B4D8)
            )
        ),
        radius = cellSize / 5f,
        center = center
    )
}

@Composable
fun AnimatedStatBox(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    val infiniteTransition = rememberInfiniteTransition()
    val iconScale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.2f),
                        color.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier
                .size(28.dp)
                .scale(iconScale.value)
        )
        Text(
            text = title,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun EnhancedControlButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Button(
        onClick = onClick,
        modifier = Modifier
            .size(72.dp)
            .scale(if (isPressed) 0.95f else 1f)
            .shadow(
                elevation = if (isPressed) 4.dp else 8.dp,
                shape = CircleShape,
                spotColor = Color(0xFF00B4D8)
            ),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF0077B6),
            contentColor = Color.White
        ),
        interactionSource = interactionSource,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun CelebrationOverlay() {
    val infiniteTransition = rememberInfiniteTransition()
    val scale = infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000).copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎉",
                fontSize = 84.sp,
                modifier = Modifier.scale(scale.value)
            )
            Text(
                text = "Level Complete!",
                color = Color(0xFF90E0EF),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.scale(scale.value)
            )
            Text(
                text = "Amazing Work!",
                color = Color(0xFFCAF0F8),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun TutorialOverlay() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha = infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000).copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Use arrows to navigate!",
                color = Color(0xFFCAF0F8).copy(alpha = alpha.value),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "↑ ← ↓ →",
                color = Color(0xFF90E0EF).copy(alpha = alpha.value),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

// Complete Game class implementation
class Game(private val width: Int, private val height: Int) {
    var playerX = 0
    var playerY = 0
    private var goalX = width - 1
    private var goalY = height - 1
    private val maze: Array<CharArray> = Array(height) { CharArray(width) { '.' } }

    init {
        generateNewMaze()
    }

    fun generateNewMaze() {
        playerX = 0
        playerY = 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                maze[y][x] = if (Random.nextFloat() < 0.3) '#' else '.'
            }
        }

        maze[0][0] = '.'
        maze[height - 1][width - 1] = '.'

        // Ensure there's a path to the goal
        val buffer = Array(height) { y ->
            Array(width) { x ->
                maze[y][x] == '.'
            }
        }

        if (!hasPath(buffer, 0, 0, height - 1, width - 1)) {
            generateNewMaze() // Recursively generate a new maze if no path exists
        }

        updateMaze()
    }

    private fun hasPath(
        buffer: Array<Array<Boolean>>,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int
    ): Boolean {
        if (startX == endX && startY == endY) return true
        buffer[startY][startX] = false

        val directions = listOf(
            Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0)
        )

        for ((dx, dy) in directions) {
            val newX = startX + dx
            val newY = startY + dy
            if (newX in 0 until width &&
                newY in 0 until height &&
                buffer[newY][newX] &&
                hasPath(buffer, newX, newY, endX, endY)
            ) {
                return true
            }
        }

        return false
    }

    private fun updateMaze() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (maze[y][x] != '#') maze[y][x] = '.'
            }
        }
        maze[playerY][playerX] = 'P'
        maze[goalY][goalX] = 'G'
    }

    fun isValidMove(newX: Int, newY: Int): Boolean {
        return newX in 0 until width &&
                newY in 0 until height &&
                maze[newY][newX] != '#'
    }

    fun getMaze(): Array<CharArray> {
        updateMaze()
        return maze
    }

    fun isPlayerAtGoal(): Boolean = playerX == goalX && playerY == goalY
    
    fun updateMazeSize(newWidth: Int, newHeight: Int) {
        // This would require recreating the game with new dimensions
        // For now, we'll just regenerate the maze with current dimensions
        generateNewMaze()
    }
}