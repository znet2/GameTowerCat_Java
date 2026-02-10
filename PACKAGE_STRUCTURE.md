# Tower Defense - Package Structure Documentation

## 📁 Package Organization

The project follows a clean, domain-driven package structure that separates concerns and promotes maintainability.

```
src/com/towerdefense/
├── main/                    # Application entry point
├── core/                    # Game loop and core systems
├── entities/                # Game objects and entities
│   ├── base/               # Base classes and interfaces
│   ├── enemies/            # Enemy types and AI
│   ├── defensive/          # Defensive units (tanks, houses)
│   └── projectiles/        # Future projectiles and effects
├── world/                   # Map and world systems
│   └── tiles/              # Future tile types
├── managers/                # Game state and system managers
├── ui/                      # User interface components
│   ├── hud/                # HUD elements (health bars, displays)
│   └── components/         # Reusable UI components
└── utils/                   # Utilities and constants
```

## 📋 Package Responsibilities

### `com.towerdefense.main`
**Purpose**: Application bootstrap and entry point
- `Main.java` - Creates window and initializes game

**Dependencies**: Only depends on `core` package
**Extensibility**: Minimal - handles only application startup

---

### `com.towerdefense.core`
**Purpose**: Core game systems and main game loop
- `GamePanel.java` - Main game controller, rendering, input handling

**Dependencies**: Depends on `world`, `entities`, `managers`, `ui`
**Extensibility**: Central coordination point for new systems

---

### `com.towerdefense.entities.base`
**Purpose**: Base classes and interfaces for all game entities
- `GameObject.java` - Abstract base for drawable objects
- `Defensive.java` - Interface for defensive capabilities
- `Collidable.java` - Interface for collision detection

**Dependencies**: None (foundation layer)
**Extensibility**: Add new interfaces here (e.g., `Attacker`, `Movable`)

---

### `com.towerdefense.entities.enemies`
**Purpose**: Enemy types and AI behavior
- `Enemy.java` - Basic enemy with pathfinding and combat

**Dependencies**: `base`, `world`, `managers`
**Extensibility**: 
- Add new enemy types: `FastEnemy`, `ArmoredEnemy`, `FlyingEnemy`
- Implement different AI behaviors
- Add special abilities and effects

---

### `com.towerdefense.entities.defensive`
**Purpose**: Defensive units that protect against enemies
- `Tank.java` - Pure defensive obstacle unit
- `House.java` - Main objective to defend

**Dependencies**: `base`
**Extensibility**:
- Add new defensive units: `Wall`, `Shield`, `Barrier`
- Implement upgradeable defenses
- Add special defensive abilities

---

### `com.towerdefense.world`
**Purpose**: Map, tiles, and world representation
- `Map.java` - Game world with tile rendering and object management

**Dependencies**: `entities`
**Extensibility**:
- Add new tile types in `tiles/` subpackage
- Implement dynamic map generation
- Add environmental effects and hazards

---

### `com.towerdefense.managers`
**Purpose**: Game state management and system coordination
- `CoinManager.java` - Economy and currency system
- `WaveManager.java` - Enemy wave spawning and progression

**Dependencies**: `entities`, `world`
**Extensibility**:
- Add `GameStateManager` for save/load
- Add `UpgradeManager` for unit improvements
- Add `ScoreManager` for high scores

---

### `com.towerdefense.ui.hud`
**Purpose**: Heads-up display elements
- `HealthBar.java` - Reusable health display component

**Dependencies**: None (pure UI)
**Extensibility**:
- Add `CoinDisplay`, `WaveDisplay`, `ScoreDisplay`
- Implement animated UI elements
- Add status indicators and notifications

---

### `com.towerdefense.utils`
**Purpose**: Shared utilities and constants
- `Constants.java` - Centralized configuration values
- `MathUtils.java` - Mathematical helper functions

**Dependencies**: None
**Extensibility**:
- Add `FileUtils` for save/load operations
- Add `AudioUtils` for sound management
- Add `ImageUtils` for graphics processing

## 🔄 Dependency Flow

```
main → core → {managers, world, entities, ui} → {base, utils}
```

**Key Principles**:
- **No Circular Dependencies**: Clean one-way dependency flow
- **Minimal Coupling**: Packages only depend on what they need
- **Interface Segregation**: Base interfaces prevent tight coupling

## 🚀 Adding New Features

### Adding a New Enemy Type
1. Create class in `entities.enemies` package
2. Extend base `Enemy` or implement required interfaces
3. Add to `WaveManager` spawning logic
4. No changes needed to core systems

### Adding a New Defensive Unit
1. Create class in `entities.defensive` package
2. Implement `Defensive` and `Collidable` interfaces
3. Add to `Map` placement system
4. Add to `CoinManager` for costs

### Adding UI Elements
1. Create reusable components in `ui.components`
2. Create specific HUD elements in `ui.hud`
3. Integrate into `GamePanel` rendering
4. Use `Constants` for styling values

### Adding New Managers
1. Create manager class in `managers` package
2. Initialize in `GamePanel.initializeGameComponents()`
3. Add update call to `GamePanel.updateGame()`
4. Expose through getter if needed by other systems

## 📈 Scalability Benefits

### **Easy Maintenance**
- Clear separation of concerns
- Changes isolated to specific packages
- Easy to locate and modify functionality

### **Team Development**
- Different developers can work on different packages
- Minimal merge conflicts due to separation
- Clear ownership of code areas

### **Testing**
- Each package can be unit tested independently
- Mock dependencies easily due to interface usage
- Integration tests focus on package boundaries

### **Performance**
- Only load classes when needed
- Clear optimization targets (e.g., entities vs UI)
- Easy to profile specific subsystems

## 🎯 Best Practices

### **When Adding New Classes**
1. Choose the most specific package possible
2. Implement appropriate base interfaces
3. Use `Constants` for configuration values
4. Document dependencies and purpose

### **When Modifying Existing Classes**
1. Check if changes affect package boundaries
2. Update interfaces if behavior changes
3. Consider impact on dependent packages
4. Maintain backward compatibility when possible

### **Package Guidelines**
- Keep packages focused on single responsibility
- Avoid deep nesting (max 3 levels recommended)
- Use descriptive package names
- Group related functionality together

This structure supports the current tower defense game while providing a solid foundation for future expansion into a more complex game with multiple unit types, advanced AI, and rich user interfaces.