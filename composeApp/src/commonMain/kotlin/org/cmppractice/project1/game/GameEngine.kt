package org.cmppractice.project1.game

import kotlin.random.Random
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.sqrt

// Game States
enum class GameState {
    MENU, PLAYING, PAUSED, GAME_OVER, LEVEL_COMPLETE, SETTINGS, STATS
}

// Difficulty Levels
enum class Difficulty {
    EASY, MEDIUM, HARD, EXPERT
}

// Game Modes
enum class GameMode {
    CLASSIC, TIME_TRIAL, ENDLESS, MAZE_MASTER, SPEED_RUN, SURVIVAL
}

// Power-ups
enum class PowerUp {
    SPEED_BOOST, WALL_PASS, TELEPORT, FREEZE_TIME, SHOW_PATH, DOUBLE_POINTS, SHIELD
}

// Game Statistics
data class GameStats(
    val totalMoves: Int = 0,
    val totalTime: Long = 0,
    val levelsCompleted: Int = 0,
    val bestScore: Int = Int.MAX_VALUE,
    val powerUpsUsed: Int = 0,
    val gamesPlayed: Int = 0,
    val achievements: MutableSet<Achievement> = mutableSetOf(),
    val highScores: MutableList<HighScore> = mutableListOf()
)

// Game Settings
data class GameSettings(
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val gameMode: GameMode = GameMode.CLASSIC,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val showTutorial: Boolean = true,
    val autoSave: Boolean = true,
    val particleEffects: Boolean = true,
    val animations: Boolean = true
)

// Achievement System
enum class Achievement(val title: String, val description: String, val icon: String) {
    FIRST_LEVEL("First Steps", "Complete your first level", "🎯"),
    SPEED_DEMON("Speed Demon", "Complete a level in under 30 moves", "⚡"),
    MASTER_NAVIGATOR("Master Navigator", "Complete 10 levels", "🧭"),
    POWER_USER("Power User", "Use 5 power-ups in a single game", "💎"),
    PERFECT_RUN("Perfect Run", "Complete a level without hitting walls", "⭐"),
    SURVIVOR("Survivor", "Complete 5 levels in survival mode", "🛡️"),
    TIME_MASTER("Time Master", "Complete a time trial in under 60 seconds", "⏱️"),
    MAZE_EXPLORER("Maze Explorer", "Complete 25 levels", "🗺️")
}

// High Score System
data class HighScore(
    val playerName: String,
    val score: Int,
    val level: Int,
    val gameMode: GameMode,
    val timestamp: Long
)

// Professional Game Engine
class GameEngine {
    private var gameState: GameState = GameState.MENU
    private var settings: GameSettings = GameSettings()
    private var stats: GameStats = GameStats()
    private var currentLevel: Int = 1
    private var startTime: Long = 0L
    private var powerUps: MutableList<PowerUp> = mutableListOf()
    private var activePowerUps: MutableMap<PowerUp, Long> = mutableMapOf()
    private var score: Int = 0
    private var combo: Int = 0
    private var streak: Int = 0

    // Game board properties
    private var width: Int = 12
    private var height: Int = 12
    private var maze: Array<CharArray> = Array(height) { CharArray(width) { '.' } }
    private var playerX: Int = 0
    private var playerY: Int = 0
    private var goalX: Int = width - 1
    private var goalY: Int = height - 1

    // Time trial properties
    private var timeLimit: Long = 0
    private var remainingTime: Long = 0L

    // Survival mode properties
    private var lives: Int = 3
    private var survivalLevel: Int = 1

    init {
        generateNewMaze()
    }

    // Game State Management
    fun getGameState(): GameState = gameState
    fun setGameState(state: GameState) { gameState = state }
    fun getSettings(): GameSettings = settings
    fun getStats(): GameStats = stats
    fun getCurrentLevel(): Int = currentLevel
    fun getScore(): Int = score
    fun getCombo(): Int = combo
    fun getStreak(): Int = streak
    fun getLives(): Int = lives
    fun getRemainingTime(): Long = remainingTime

    // Game Initialization
    fun startGame(mode: GameMode = GameMode.CLASSIC, difficulty: Difficulty = Difficulty.MEDIUM) {
        gameState = GameState.PLAYING
        settings = settings.copy(gameMode = mode, difficulty = difficulty)
        currentLevel = 1
        score = 0
        combo = 0
        streak = 0
        startTime = 0L // Using 0L for now since System.currentTimeMillis() is not available in Wasm
        
        when (mode) {
            GameMode.TIME_TRIAL -> {
                timeLimit = when (difficulty) {
                    Difficulty.EASY -> 120000L // 2 minutes
                    Difficulty.MEDIUM -> 90000L // 1.5 minutes
                    Difficulty.HARD -> 60000L // 1 minute
                    Difficulty.EXPERT -> 30000L // 30 seconds
                }
                remainingTime = timeLimit
            }
            GameMode.SURVIVAL -> {
                lives = 3
                survivalLevel = 1
            }
            else -> {
                timeLimit = 0
                remainingTime = 0
            }
        }

        generateNewMaze()
        stats = stats.copy(gamesPlayed = stats.gamesPlayed + 1)
    }

    // Maze Generation with Different Algorithms
    private fun generateNewMaze() {
        playerX = 0
        playerY = 0

        when (settings.difficulty) {
            Difficulty.EASY -> generateSimpleMaze()
            Difficulty.MEDIUM -> generateMediumMaze()
            Difficulty.HARD -> generateHardMaze()
            Difficulty.EXPERT -> generateExpertMaze()
        }

        // Ensure there's a path to the goal
        if (!hasPathToGoal()) {
            generateNewMaze() // Recursively generate a new maze
        }

        updateMaze()
    }

    private fun generateSimpleMaze() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                maze[y][x] = if (Random.nextFloat() < 0.2) '#' else '.'
            }
        }
        maze[0][0] = '.'
        maze[height - 1][width - 1] = '.'
    }

    private fun generateMediumMaze() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                maze[y][x] = if (Random.nextFloat() < 0.3) '#' else '.'
            }
        }
        maze[0][0] = '.'
        maze[height - 1][width - 1] = '.'
    }

    private fun generateHardMaze() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                maze[y][x] = if (Random.nextFloat() < 0.4) '#' else '.'
            }
        }
        maze[0][0] = '.'
        maze[height - 1][width - 1] = '.'
    }

    private fun generateExpertMaze() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                maze[y][x] = if (Random.nextFloat() < 0.5) '#' else '.'
            }
        }
        maze[0][0] = '.'
        maze[height - 1][width - 1] = '.'
    }

    private fun hasPathToGoal(): Boolean {
        val buffer = Array(height) { y ->
            Array(width) { x ->
                maze[y][x] == '.'
            }
        }
        return hasPath(buffer, 0, 0, height - 1, width - 1)
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

    // Player Movement with Power-up Support
    fun movePlayer(dx: Int, dy: Int): Boolean {
        val newX = playerX + dx
        val newY = playerY + dy

        if (isValidMove(newX, newY)) {
            playerX = newX
            playerY = newY
            updateMaze()
            
            // Update score and combo
            updateScore()
            
            // Check for level completion
            if (isPlayerAtGoal()) {
                handleLevelCompletion()
                return true
            }
            
            return true
        } else if (isPowerUpActive(PowerUp.WALL_PASS)) {
            // Wall pass power-up allows moving through walls
            playerX = newX
            playerY = newY
            updateMaze()
            updateScore()
            
            if (isPlayerAtGoal()) {
                handleLevelCompletion()
            }
            return true
        }
        
        return false
    }

    private fun updateScore() {
        val baseScore = 10
        val comboBonus = combo * 5
        val streakBonus = streak * 2
        
        score += baseScore + comboBonus + streakBonus
        combo++
        streak++
    }

    private fun handleLevelCompletion() {
        gameState = GameState.LEVEL_COMPLETE
        
        // Level completion bonus
        val levelBonus = currentLevel * 100
        val timeBonus = calculateTimeBonus()
        val comboBonus = combo * 50
        
        score += levelBonus + timeBonus.toInt() + comboBonus
        
        // Update statistics
        stats = stats.copy(
            levelsCompleted = stats.levelsCompleted + 1,
            totalMoves = stats.totalMoves + combo
        )
        
        // Check for achievements
        checkAchievements()
        
        // Update high scores
        updateHighScores()
        
        // Prepare for next level
        currentLevel++
        combo = 0
        
        if (settings.gameMode == GameMode.SURVIVAL) {
            survivalLevel++
        }
    }

    private fun calculateTimeBonus(): Long {
        val elapsedTime = 0L - startTime
        val timeInSeconds = elapsedTime / 1000
        return maxOf(0L, 1000L - (timeInSeconds * 10L))
    }

    // Power-up System
    fun addPowerUp(powerUp: PowerUp) {
        powerUps.add(powerUp)
    }

    fun usePowerUp(powerUp: PowerUp): Boolean {
        if (powerUp in powerUps) {
            powerUps.remove(powerUp)
            activePowerUps[powerUp] = 0L + 30000 // 30 seconds
            stats = stats.copy(powerUpsUsed = stats.powerUpsUsed + 1)
            return true
        }
        return false
    }

    fun isPowerUpActive(powerUp: PowerUp): Boolean {
        val endTime = activePowerUps[powerUp] ?: return false
        val currentTime = 0L // Using 0L for now since System.currentTimeMillis() is not available in Wasm
        if (currentTime > endTime) {
            activePowerUps.remove(powerUp)
            return false
        }
        return true
    }

    fun getAvailablePowerUps(): List<PowerUp> = powerUps.toList()
    fun getActivePowerUps(): Map<PowerUp, Long> = activePowerUps.toMap()

    // Achievement System
    private fun checkAchievements() {
        when {
            stats.levelsCompleted == 1 -> unlockAchievement(Achievement.FIRST_LEVEL)
            combo <= 30 -> unlockAchievement(Achievement.SPEED_DEMON)
            stats.levelsCompleted >= 10 -> unlockAchievement(Achievement.MASTER_NAVIGATOR)
            stats.powerUpsUsed >= 5 -> unlockAchievement(Achievement.POWER_USER)
            combo == stats.totalMoves -> unlockAchievement(Achievement.PERFECT_RUN)
            settings.gameMode == GameMode.SURVIVAL && survivalLevel >= 5 -> unlockAchievement(Achievement.SURVIVOR)
            settings.gameMode == GameMode.TIME_TRIAL && remainingTime > 0 -> unlockAchievement(Achievement.TIME_MASTER)
            stats.levelsCompleted >= 25 -> unlockAchievement(Achievement.MAZE_EXPLORER)
        }
    }

    private fun unlockAchievement(achievement: Achievement) {
        if (achievement !in stats.achievements) {
            stats.achievements.add(achievement)
        }
    }

    // High Score System
    private fun updateHighScores() {
        val highScore = HighScore(
            playerName = "Player",
            score = score,
            level = currentLevel,
            gameMode = settings.gameMode,
            timestamp = 0L
        )
        
        stats.highScores.add(highScore)
        stats.highScores.sortByDescending { it.score }
        
        // Keep only top 10 scores
        if (stats.highScores.size > 10) {
            val topScores = stats.highScores.take(10).toMutableList()
            stats.highScores.clear()
            stats.highScores.addAll(topScores)
        }
    }

    // Game Logic
    fun isValidMove(newX: Int, newY: Int): Boolean {
        return newX in 0 until width &&
                newY in 0 until height &&
                maze[newY][newX] != '#'
    }

    fun isPlayerAtGoal(): Boolean = playerX == goalX && playerY == goalY

    fun getMaze(): Array<CharArray> = maze
    fun getPlayerX(): Int = playerX
    fun getPlayerY(): Int = playerY
    fun getGoalX(): Int = goalX
    fun getGoalY(): Int = goalY

    private fun updateMaze() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (maze[y][x] != '#') maze[y][x] = '.'
            }
        }
        maze[playerY][playerX] = 'P'
        maze[goalY][goalX] = 'G'
    }

    // Time Management
    fun updateTime(deltaTime: Long) {
        if (settings.gameMode == GameMode.TIME_TRIAL) {
            remainingTime -= deltaTime
            if (remainingTime <= 0) {
                gameState = GameState.GAME_OVER
            }
        }
    }

    // Game Over Handling
    fun handleGameOver() {
        gameState = GameState.GAME_OVER
        updateStats()
    }

    private fun updateStats() {
        val gameDuration = (0L - startTime) / 1000
        stats = stats.copy(totalTime = stats.totalTime + gameDuration)
    }

    // Settings Management
    fun updateSettings(newSettings: GameSettings) {
        settings = newSettings
    }

    // Reset Game
    fun resetGame() {
        gameState = GameState.MENU
        currentLevel = 1
        score = 0
        combo = 0
        streak = 0
        powerUps.clear()
        activePowerUps.clear()
        generateNewMaze()
    }
} 