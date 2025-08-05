# Cyber Maze

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.5.0-orange.svg)](https://github.com/JetBrains/compose-multiplatform)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS%20%7C%20Web-lightgrey.svg)](https://kotlinlang.org/docs/multiplatform.html)

> A modern, cross-platform maze navigation game built with Kotlin Multiplatform and Compose Multiplatform

## Media

### Screenshots

![Game Screenshot](https://raw.githubusercontent.com/SatyamkrJha85/Cyber-Maze/main/composeApp/src/androidMain/res/drawable/demomazeimg.webp)
*Cyber Maze gameplay showing the futuristic maze interface*



### Demo Video

<video width="100%" controls>
  <source src="https://raw.githubusercontent.com/SatyamkrJha85/Cyber-Maze/main/composeApp/src/androidMain/res/drawable/demovideo.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>



### Gameplay Preview

The game features:
- Dynamic maze generation with cyberpunk aesthetics
- Smooth animations and particle effects
- Power-up collection and usage
- Real-time score tracking and achievements
- Cross-platform compatibility (Android, iOS, Web)

## Features

### Core Gameplay
- **Dynamic Maze Generation**: Procedurally generated mazes with varying difficulty levels
- **Multiple Game Modes**: Classic, Time Attack, Survival, and Puzzle modes
- **Power-up System**: Collect and use special abilities like Speed Boost, Wall Breaker, and Teleporter
- **Achievement System**: Unlock achievements as you progress through the game
- **Combo System**: Chain moves for bonus points and multipliers

### Visual Experience
- **Cyberpunk Theme**: Futuristic UI with neon colors and glowing effects
- **Smooth Animations**: Fluid transitions and particle effects
- **Dynamic Lighting**: Real-time lighting and shadow effects
- **Responsive Design**: Optimized for different screen sizes and orientations

### Technical Features
- **Cross-Platform**: Runs on Android, iOS, and Web browsers
- **Real-time Updates**: Live score tracking and game state management
- **Customizable Settings**: Adjust difficulty, themes, and game preferences
- **Statistics Tracking**: Comprehensive game statistics and progress tracking

## Getting Started

### Prerequisites

- **Kotlin**: 1.9.0 or higher
- **Android Studio**: Arctic Fox or later
- **Xcode**: 14.0 or later (for iOS development)
- **Node.js**: 16.0 or later (for web development)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/cyber-maze.git
   cd cyber-maze
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run on your preferred platform**

   **Android:**
   ```bash
   ./gradlew :composeApp:assembleDebug
   ```

   **iOS:**
   ```bash
   ./gradlew :composeApp:iosSimulatorArm64Test
   ```

   **Web:**
   ```bash
   ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
   ```

## How to Play

### Basic Controls
- **Arrow Keys**: Navigate through the maze
- **Touch Controls**: Swipe gestures on mobile devices
- **Mouse**: Click and drag on web platform

### Game Objectives
1. **Navigate** from the starting position (blue circle) to the goal (green circle)
2. **Collect Power-ups** to gain special abilities
3. **Complete Levels** to unlock new challenges
4. **Achieve High Scores** and unlock achievements

### Power-ups
- **Speed Boost**: Move faster for a limited time
- **Time Freeze**: Pause the timer temporarily
- **Combo Multiplier**: Increase score multipliers
- **Wall Breaker**: Pass through walls
- **Teleporter**: Instantly move to a random location

## Architecture

### Project Structure
```
Cyber-Maze/
├── composeApp/                 # Main application module
│   ├── src/
│   │   ├── commonMain/        # Shared Kotlin code
│   │   │   ├── kotlin/
│   │   │   │   └── org/cmppractice/project1/
│   │   │   │       ├── App.kt                    # Main application entry
│   │   │   │       ├── game/
│   │   │   │       │   └── GameEngine.kt         # Core game logic
│   │   │   │       └── ui/
│   │   │   │           ├── components/           # Reusable UI components
│   │   │   │           ├── screens/              # Screen implementations
│   │   │   │           └── theme/                # Theme and styling
│   │   │   └── resources/                        # Shared resources
│   │   ├── androidMain/       # Android-specific code
│   │   ├── iosMain/           # iOS-specific code
│   │   └── wasmJsMain/        # Web-specific code
│   └── build.gradle.kts       # Build configuration
├── iosApp/                    # iOS application wrapper
├── gradle/                    # Gradle configuration
└── README.md                  # This file
```

### Technology Stack
- **Kotlin Multiplatform**: Cross-platform development
- **Compose Multiplatform**: Modern declarative UI framework
- **Kotlin/Wasm**: Web platform support
- **Gradle**: Build system and dependency management

## Customization

### Themes
The game supports multiple visual themes:
- **Cyber**: Default futuristic blue theme
- **Neon**: High-contrast neon colors
- **Classic**: Traditional gaming colors
- **Dark**: Minimalist dark theme

### Difficulty Levels
- **Easy**: 8x8 maze, 5 minutes
- **Medium**: 12x12 maze, 4 minutes
- **Hard**: 16x16 maze, 3 minutes
- **Expert**: 20x20 maze, 2 minutes

## Contributing

We welcome contributions from the community! Here's how you can help:

### Development Setup
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes and add tests
4. Commit your changes: `git commit -m 'Add amazing feature'`
5. Push to the branch: `git push origin feature/amazing-feature`
6. Open a Pull Request

### Guidelines
- Follow Kotlin coding conventions
- Add tests for new features
- Update documentation as needed
- Ensure cross-platform compatibility

### Areas for Contribution
- New game modes and features
- UI/UX improvements
- Bug fixes and performance optimizations
- Platform-specific enhancements
- Documentation improvements

## Statistics

- **Lines of Code**: 2,000+
- **Supported Platforms**: 3 (Android, iOS, Web)
- **Game Modes**: 4
- **Power-ups**: 5
- **Achievements**: 8

## Achievements

Players can unlock various achievements:
- **First Victory**: Complete your first maze
- **Speed Runner**: Complete a maze in under 30 seconds
- **Master Navigator**: Complete 10 levels
- **Power User**: Use 5 power-ups in a single game
- **Perfect Run**: Complete a level without hitting walls
- **Survivor**: Complete 5 levels in survival mode
- **Time Master**: Complete a time trial in under 60 seconds
- **Maze Explorer**: Complete 25 levels

## Platform Support

| Platform | Status | Notes |
|----------|--------|-------|
| Android | Full Support | Optimized for touch controls |
| iOS | Full Support | Native iOS integration |
| Web | Full Support | Progressive Web App ready |

## Development

### Building from Source
```bash
# Clone the repository
git clone https://github.com/yourusername/cyber-maze.git
cd cyber-maze

# Build all platforms
./gradlew build

# Run tests
./gradlew test

# Build for specific platform
./gradlew :composeApp:assembleDebug          # Android
./gradlew :composeApp:iosSimulatorArm64Test  # iOS
./gradlew :composeApp:wasmJsBrowserDevelopmentRun  # Web
```

### Testing
```bash
# Run all tests
./gradlew test

# Run specific platform tests
./gradlew :composeApp:androidTest
./gradlew :composeApp:iosTest
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **JetBrains** for Kotlin Multiplatform and Compose Multiplatform
- **Kotlin/Wasm** team for web platform support
- **Compose Multiplatform** community for excellent documentation
- **Open Source Contributors** who help improve this project

## Support

- **Issues**: [GitHub Issues](https://github.com/yourusername/cyber-maze/issues)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/cyber-maze/discussions)
- **Email**: your.email@example.com

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=yourusername/cyber-maze&type=Date)](https://star-history.com/#yourusername/cyber-maze&Date)

---

<div align="center">
  <p>Made with love by the Cyber Maze Team</p>
  <p>If you find this project helpful, please consider giving it a star</p>
</div>