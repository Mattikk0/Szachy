# ♟️ Szachy - Chess Game

A feature-rich chess application built with **Java** and **JavaFX**, with Python-based UI menus for an enhanced gaming experience.

## Features

### Core Chess Gameplay
- **Full chess rule implementation** including:
  - All piece movements (Pawns, Rooks, Knights, Bishops, Queens, Kings)
  - Special moves: Castling, En Passant, Pawn promotion
  - Check and checkmate detection
  - Stalemate conditions (50-move rule, threefold repetition, insufficient material)

### Game Modes
- **Player vs Player** - Local multiplayer chess
- **Player vs AI** - Play against a computer opponent with adjustable difficulty levels

### User Interface
- **Modern JavaFX-based chessboard** with smooth graphics and intuitive controls
- **Python-based menus** for game setup, game end, and error handling
- **Interactive piece selection** and move validation
- **Game restart capability** without closing the application

### Game Management
- **Save and load functionality** to continue games later
- **Game state tracking** with move history
- **Piece capture tracking** and move legality validation

## Technology Stack

- **Java 22** (93.8% of codebase)
  - Core game logic and chess engine
  - JavaFX for graphical interface
  - Advanced algorithms for move validation and game state management

- **Python 3** (6.2% of codebase)
  - Menu interfaces using Tkinter
  - JPype integration for Java-Python interoperability

## Project Structure

```
Szachy/
├── src/
│   └── chess/
│       ├── Main.java                 # Application entry point
│       ├── client/
│       │   ├── game/
│       │   │   └── ChessGame.java   # Game logic and rules
│       │   └── ui/
│       │       ├── board/
│       │       │   └── Board.java   # Chessboard UI component
│       │       └── menu/
│       │           ├── StartMenu.py  # Game start menu
│       │           ├── EndMenu.py    # Game end menu
│       │           ├── ErrorMenu.py  # Error handling menus
│       │           └── ChooserMenu.py # Game setup menu
│       └── shared/
│           ├── model/
│           │   ├── GameState.java   # Game state management
│           │   └── Turn.java        # Turn management
│           └── piece/
│               ├── Pieces.java      # Base piece class
│               └── [Various piece implementations]
├── README.md
└── Szachy.iml
```

## Getting Started

### Prerequisites
- Java Development Kit (JDK 22 or later)
- Python 3.x
- JPype library for Java-Python integration

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Mattikk0/Szachy.git
   cd Szachy
   ```

2. **Install Python dependencies**
   ```bash
   pip install jpype1
   ```

3. **Build the project**
   - Using IntelliJ IDEA: Build → Build Project
   - Or compile with: `javac -d out/production/Szachy src/chess/**/*.java`

4. **Run the application**
   ```bash
   java chess.Main
   ```

## How to Play

1. **Start the game** - Choose "Nowa gra" (New Game) or "Wczytaj grę" (Load Game) from the start menu
2. **Select game mode** - Choose between:
   - Playing against another player
   - Playing against the AI (select difficulty level)
3. **Choose your color** - White or Black
4. **Make your moves** - Click on a piece to select it, then click on a valid destination square
5. **Game end** - The game ends when checkmate, stalemate, or other draw conditions occur

## AI Opponent

The chess engine includes:
- **Move validation engine** that enforces all standard chess rules
- **Check/checkmate detection** with proper game termination
- **Difficulty levels** for customized challenge
- **Position evaluation** for intelligent move selection

## Game Rules Implemented

✅ Piece movement rules  
✅ Check and checkmate detection  
✅ Stalemate (no legal moves)  
✅ 50-move rule (no pawn moves or captures)  
✅ Threefold repetition rule  
✅ Insufficient material rule  
✅ Castling (with validation)  
✅ En Passant  
✅ Pawn promotion  

## Contributing

Contributions are welcome! Feel free to:
- Report bugs and issues
- Suggest new features
- Submit pull requests with improvements

## License

This project is open source and available under the terms specified in the repository.

## Author

Created by [Mattikk0](https://github.com/Mattikk0)

---

**Enjoy playing chess! ♟️♚♛**
