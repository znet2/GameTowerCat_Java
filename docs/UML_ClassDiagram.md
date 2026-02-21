# UML Class Diagram - Tower Defense Game

## Class Diagram (PlantUML)

```plantuml
@startuml TowerDefenseGame

' ============= Main Package =============
package "com.game.main" {
    class Main {
        + {static} main(String[] args): void
        - {static} createAndShowGameWindow(): void
        - {static} configureWindow(JFrame): void
        - {static} finalizeWindow(JFrame): void
    }
}

' ============= Core Package =============
package "com.game.core" {
    class MenuPanel {
        - JFrame parentFrame
        - Image wallpaperImage
        - Image startButtonImage
        - Image logoImage
        - Rectangle startButtonBounds
        + MenuPanel(JFrame)
        - loadImages(): void
        - setupPanel(): void
        # paintComponent(Graphics): void
        - handleMouseClick(MouseEvent): void
        - startGame(): void
    }

    class GamePanel {
        - Thread gameLoopThread
        - JFrame parentFrame
        - Map gameMap
        - ArrayList<BaseEnemy> activeEnemies
        - CoinManager coinManager
        - WaveManager waveManager
        - boolean isGameOver
        - boolean isWin
        - Rectangle heroBarArea
        - Rectangle tankIconArea
        - Rectangle magicIconArea
        - Rectangle archerIconArea
        - Rectangle assassinIconArea
        - boolean isDraggingTank
        - boolean isDraggingMagic
        - boolean isDraggingArcher
        - boolean isDraggingAssassin
        - int dragPositionX
        - int dragPositionY
        + GamePanel(JFrame)
        - initializeGameComponents(): void
        - setupUserInterface(): void
        - configurePanel(): void
        - startGameLoop(): void
        + run(): void
        - update(): void
        - checkGameOver(): void
        - checkWinCondition(): void
        - showGameOver(boolean): void
        # paintComponent(Graphics): void
        - renderHeroBar(Graphics): void
        - renderUnitIcons(Graphics): void
        - renderDragPreview(Graphics): void
        - renderHouseHealthBar(Graphics): void
        + mousePressed(MouseEvent): void
        + mouseReleased(MouseEvent): void
        + mouseDragged(MouseEvent): void
        + mouseClicked(MouseEvent): void
        + mouseMoved(MouseEvent): void
        + mouseEntered(MouseEvent): void
        + mouseExited(MouseEvent): void
        - isRoadTile(int, int): boolean
        - canPlaceUnit(int, int): boolean
    }

    class GameOverPanel {
        - JFrame parentFrame
        - boolean isWin
        - Image backgroundImage
        - Image resultImage
        - Image restartButtonImage
        - Rectangle restartButtonBounds
        + GameOverPanel(JFrame, boolean)
        - loadImages(): void
        - setupPanel(): void
        # paintComponent(Graphics): void
        - handleMouseClick(MouseEvent): void
        - restartGame(): void
    }
}

' ============= Utils Package =============
package "com.game.utils" {
    class Constants {
        + {static} class Game {
            + {static} TARGET_FPS: int
            + {static} TITLE: String
            + {static} NANOSECONDS_PER_FRAME: double
            + {static} WINDOW_WIDTH: int
            + {static} WINDOW_HEIGHT: int
        }
        + {static} class Map {
            + {static} TILE_SIZE: int
            + {static} HOUSE_COLUMN: int
            + {static} HOUSE_ROW: int
            + {static} TILE_ROAD: int
            + {static} TILE_GRASS: int
            + {static} TILE_WATER: int
            + {static} TILE_WATER_UP: int
            + {static} TILE_WATER_DOWN: int
            + {static} TILE_WATER_LEFT: int
            + {static} TILE_WATER_RIGHT: int
            + {static} TILE_TREE: int
        }
        + {static} class Entities {
            + {static} MINIMUM_HEALTH: int
            + {static} ATTACK_ANIMATION_DURATION: int
            + {static} SPELL_ANIMATION_DURATION: int
            + {static} ENEMY_WALK_ANIMATION_FRAMES: int
            + {static} ENEMY_ATTACK_ANIMATION_FRAMES: int
            + {static} TANK_INITIAL_HEALTH: int
            + {static} TANK_COST: int
            + {static} TANK_X_OFFSET: int
            + {static} TANK_Y_OFFSET: int
            + {static} MAGIC_INITIAL_HEALTH: int
            + {static} MAGIC_DEFENSE_RATING: int
            + {static} MAGIC_COST: int
            + {static} MAGIC_ATTACK_DAMAGE: int
            + {static} MAGIC_SPELL_DAMAGE: int
            + {static} MAGIC_ATTACK_COOLDOWN_FRAMES: int
            + {static} MAGIC_ATTACK_RANGE: int
            + {static} MAGIC_ATTACKS_BEFORE_SPELL: int
            + {static} MAGIC_X_OFFSET: int
            + {static} MAGIC_Y_OFFSET: int
            + {static} ARCHER_INITIAL_HEALTH: int
            + {static} ARCHER_DEFENSE_RATING: int
            + {static} ARCHER_COST: int
            + {static} ARCHER_ATTACK_DAMAGE: int
            + {static} ARCHER_ATTACK_COOLDOWN_FRAMES: int
            + {static} ARCHER_ATTACK_RANGE: int
            + {static} ARCHER_X_OFFSET: int
            + {static} ARCHER_Y_OFFSET: int
            + {static} ASSASSIN_COST: int
            + {static} ASSASSIN_ATTACK_DAMAGE: int
            + {static} ASSASSIN_ATTACK_RANGE: int
            + {static} ASSASSIN_ATTACK_COOLDOWN: int
            + {static} ASSASSIN_X_OFFSET: int
            + {static} ASSASSIN_Y_OFFSET: int
            + {static} HOUSE_INITIAL_HEALTH: int
            + {static} HOUSE_WIDTH_TILES: int
            + {static} HOUSE_HEIGHT_TILES: int
            + {static} ENEMY_SIZE: int
            + {static} ENEMY_SPEED: double
            + {static} ENEMY_INITIAL_HEALTH: int
            + {static} ENEMY_ATTACK_DAMAGE: int
            + {static} ENEMY_ATTACK_COOLDOWN_FRAMES: int
            + {static} ENEMY_X_OFFSET: int
            + {static} ENEMY_Y_OFFSET: int
            + {static} ENEMY_SPACING_MULTIPLIER: double
            + {static} BOSS_SIZE: int
            + {static} BOSS_SPEED: double
            + {static} BOSS_INITIAL_HEALTH: int
            + {static} BOSS_ATTACK_DAMAGE: int
            + {static} BOSS_ATTACK_COOLDOWN_FRAMES: int
            + {static} BOSS_X_OFFSET: int
            + {static} BOSS_Y_OFFSET: int
            + {static} BOSS_SKILL_COOLDOWN_FRAMES: int
            + {static} BOSS_SKILL_ANIMATION_DURATION: int
            + {static} BOSS_SKILL_BALL_COUNT: int
            + {static} BOSS_SKILL_BALL_DAMAGE: int
        }
        + {static} class Projectiles {
            + {static} ARROW_SPEED: double
            + {static} ARROW_SIZE: int
            + {static} ARROW_FALLBACK_COLOR: Color
            + {static} ARROW_FALLBACK_SIZE: int
            + {static} MAGIC_BALL_SPEED: double
            + {static} MAGIC_BALL_SIZE: int
            + {static} MAGIC_BALL_FALLBACK_COLOR: Color
            + {static} MAGIC_BALL_FALLBACK_SIZE: int
            + {static} BOSS_SKILL_BALL_SPEED: double
            + {static} BOSS_SKILL_BALL_SIZE: int
            + {static} BOSS_SKILL_BALL_FALLBACK_COLOR: Color
            + {static} BOSS_SKILL_BALL_FALLBACK_SIZE: int
        }
        + {static} class UI {
            + {static} HERO_BAR_HEIGHT: int
            + {static} TANK_ICON_SIZE: int
            + {static} TANK_ICON_MARGIN: int
            + {static} TANK_ICON_TOP_MARGIN: int
            + {static} START_BUTTON_WIDTH: int
            + {static} START_BUTTON_HEIGHT: int
            + {static} START_BUTTON_Y_OFFSET: int
            + {static} LOGO_WIDTH: int
            + {static} LOGO_HEIGHT: int
            + {static} LOGO_TOP_MARGIN: int
            + {static} WIN_IMAGE_WIDTH: int
            + {static} WIN_IMAGE_HEIGHT: int
            + {static} WIN_IMAGE_Y_OFFSET: int
            + {static} LOSE_IMAGE_WIDTH: int
            + {static} LOSE_IMAGE_HEIGHT: int
            + {static} LOSE_IMAGE_Y_OFFSET: int
            + {static} RESTART_BUTTON_WIDTH: int
            + {static} RESTART_BUTTON_HEIGHT: int
            + {static} RESTART_BUTTON_Y_OFFSET: int
            + {static} HERO_BAR_COLOR: Color
            + {static} TANK_ICON_COLOR: Color
            + {static} TANK_ICON_DISABLED_COLOR: Color
            + {static} MAGIC_ICON_COLOR: Color
            + {static} MAGIC_ICON_DISABLED_COLOR: Color
            + {static} DRAG_PREVIEW_COLOR: Color
            + {static} COIN_TEXT_COLOR: Color
            + {static} COIN_BACKGROUND_COLOR: Color
            + {static} COIN_FONT: Font
            + {static} COIN_DISPLAY_PADDING: int
        }
        + {static} class Economy {
            + {static} STARTING_COINS: int
            + {static} COINS_PER_ENEMY_KILL: int
            + {static} COINS_PER_BOSS_KILL: int
            + {static} COINS_PER_WAVE_COMPLETE: int
        }
        + {static} class Waves {
            + {static} BASE_ENEMIES_PER_WAVE: int
            + {static} ENEMIES_INCREASE_PER_WAVE: int
            + {static} SPAWN_DELAY_FRAMES: int
            + {static} MAX_WAVES: int
        }
        + {static} class Paths {
            + {static} IMAGES: String
            + {static} ENEMY_IMAGE: String
            + {static} ENEMY_WALK_IMAGE: String
            + {static} ENEMY_ATTACK_IMAGE: String
            + {static} BOSS_IMAGE: String
            + {static} BOSS_FLY_IMAGE: String
            + {static} BOSS_ATTACK_IMAGE: String
            + {static} BOSS_SKILL_IMAGE: String
            + {static} BOSS_SKILL_BALL_IMAGE: String
            + {static} TANK_IMAGE: String
            + {static} TANK_DEFEND_IMAGE: String
            + {static} HOUSE_IMAGE: String
            + {static} MAGIC_IMAGE: String
            + {static} MAGIC_BOMB_IMAGE: String
            + {static} NORMAL_MAGIC_BALL_IMAGE: String
            + {static} SUPER_MAGIC_BALL_IMAGE: String
            + {static} ARCHER_IMAGE: String
            + {static} ARCHER_ATTACK_IMAGE: String
            + {static} ARROW_IMAGE: String
            + {static} ASSASSIN_IMAGE: String
            + {static} ASSASSIN_ATTACK_IMAGE: String
            + {static} WALLPAPER_IMAGE: String
            + {static} START_BUTTON_IMAGE: String
            + {static} LOGO_GAME_IMAGE: String
            + {static} WIN_IMAGE: String
            + {static} LOSE_IMAGE: String
            + {static} WIN_BACKGROUND_IMAGE: String
            + {static} LOSE_BACKGROUND_IMAGE: String
            + {static} RESTART_BUTTON_IMAGE: String
            + {static} GRASS_TILE: String
            + {static} ROAD_TILE: String
            + {static} WATER_TILE: String
            + {static} WATER_UP_TILE: String
            + {static} WATER_DOWN_TILE: String
            + {static} WATER_LEFT_TILE: String
            + {static} WATER_RIGHT_TILE: String
            + {static} TREE_TILE: String
        }
    }

    class MathUtils {
        + {static} calculateDistance(double, double, double, double): double
        + {static} pixelToGrid(int, int): int
        + {static} isWithinBounds(int, int, int, int): boolean
    }

    class ImageLoader {
        + {static} loadImage(String): BufferedImage
    }
}

' ============= Managers Package =============
package "com.game.managers" {
    class CoinManager {
        - int currentCoins
        + CoinManager()
        + getCurrentCoins(): int
        + addCoins(int): void
        + spendCoins(int): boolean
        + canAfford(int): boolean
        + awardCoinsForEnemyKill(): void
        + awardCoinsForBossKill(): void
        + awardCoinsForWaveComplete(): void
        + renderCoinDisplay(Graphics, int, int): void
        + resetCoins(): void
    }

    class WaveManager {
        - int currentWaveNumber
        - int enemiesToSpawnInWave
        - int enemiesSpawnedInWave
        - int spawnTimer
        - boolean isCurrentlySpawning
        - Map gameMap
        - ArrayList<BaseEnemy> activeEnemies
        - CoinManager coinManager
        + WaveManager(Map, ArrayList<BaseEnemy>, CoinManager)
        + startNextWave(): void
        + update(): void
        + isWaveFinished(): boolean
        + getCurrentWave(): int
        + isGameWon(): boolean
        - calculateEnemiesForWave(): void
        - calculateBossCount(): int
        - shouldHaveBoss(): boolean
        - spawnEnemy(): void
        - shouldSpawnBoss(): boolean
    }
}

' ============= World Package =============
package "com.game.world" {
    class Map {
        - ArrayList<GameObject> mapObjects
        - ArrayList<Tank> defensiveTanks
        - ArrayList<Magic> magicTowers
        - ArrayList<Archer> archerTowers
        - ArrayList<Assassin> assassins
        - int[][] mapGrid
        - Image houseImage
        - Image tankImage
        - Image magicImage
        - Image archerImage
        - Image assassinImage
        - Image grassTile
        - Image roadTile
        - Image waterTile
        - Image waterUpTile
        - Image waterDownTile
        - Image waterLeftTile
        - Image waterRightTile
        - Image treeTile
        + Map()
        - loadImages(): void
        - initializeMapObjects(): void
        - configurePanel(): void
        # paintComponent(Graphics): void
        - renderMapTiles(Graphics): void
        - getTileImage(int): Image
        - renderMapObjects(Graphics): void
        - renderTanks(Graphics): void
        - renderMagicTowers(Graphics): void
        - renderArcherTowers(Graphics): void
        - renderAssassins(Graphics): void
        + getRawMap(): int[][]
        + getTileSize(): int
        + getMapWidth(): int
        + getMapHeight(): int
        + draw(Graphics): void
        + getTileAtPixel(double, double): int
        + getHouse(): House
        + placeTank(int, int): void
        + placeMagic(int, int, ArrayList<BaseEnemy>): void
        + placeArcher(int, int, ArrayList<BaseEnemy>): void
        + placeAssassin(int, int, ArrayList<BaseEnemy>): void
        + updateMagicTowers(): void
        + updateArcherTowers(): void
        + updateAssassins(): void
        + getTanks(): ArrayList<Tank>
        + getMagicTowers(): ArrayList<Magic>
        + getArcherTowers(): ArrayList<Archer>
        + removeDeadTanks(): void
        + removeDeadMagicTowers(): void
        + removeDeadArcherTowers(): void
    }
}

' ============= Base Entities Package =============
package "com.game.entities.base" {
    abstract class GameObject {
        # int positionX
        # int positionY
        # int objectWidth
        # int objectHeight
        # Image objectImage
        + GameObject(int, int, int, int, int, Image)
        + draw(Graphics): void
        + getGridColumn(int): int
        + getGridRow(int): int
    }

    interface Collidable {
        + getCollisionBounds(): Rectangle
    }

    interface Defensive {
        + takeDamage(int): void
        + isDestroyed(): boolean
        + getCurrentHealth(): int
        + getMaxHealth(): int
        + getDefenseRating(): int
        + getHealthPercentage(): double
        + damage(int): void
        + isDead(): boolean
    }
}

' ============= Defensive Units Package =============
package "com.game.entities.defensive" {
    class House {
        - int currentHealth
        + House(int, int, int, Image)
        + takeDamage(int): void
        + isDestroyed(): boolean
        + getCurrentHealth(): int
        + getMaxHealth(): int
        + getBounds(): Rectangle
        + getHealth(): int
        + draw(Graphics): void
    }

    class Tank {
        - int currentHealth
        - boolean hasBeenAttacked
        - Image normalImage
        - Image defendImage
        + Tank(int, int, int, Image)
        + update(): void
        + takeDamage(int): void
        + isDestroyed(): boolean
        + getCurrentHealth(): int
        + getMaxHealth(): int
        + getDefenseRating(): int
        + getCollisionBounds(): Rectangle
        + getBounds(): Rectangle
        + draw(Graphics): void
        - drawHealthBar(Graphics): void
    }

    class Magic {
        - int currentHealth
        - BaseEnemy lockedTarget
        - int attackTimer
        - int attackCounter
        - boolean isUsingSpecialSpell
        - int spellAnimationTimer
        - ArrayList<BaseEnemy> enemyList
        - ArrayList<MagicBall> magicBalls
        - Image normalImage
        - Image bombImage
        - Image normalBallImage
        - Image superBallImage
        + Magic(int, int, int, Image, ArrayList<BaseEnemy>)
        + update(): void
        - isTargetValid(): boolean
        - acquireNewTarget(): void
        - calculateDistanceToTarget(BaseEnemy): double
        - getEnemyBounds(BaseEnemy): Rectangle
        - performAttack(): void
        - executeAttack(): void
        - performNormalAttack(): void
        - castSpecialSpell(): void
        - shootMagicBall(boolean): void
        + takeDamage(int): void
        + isDestroyed(): boolean
        + getCurrentHealth(): int
        + getMaxHealth(): int
        + getDefenseRating(): int
        + getCollisionBounds(): Rectangle
        + getBounds(): Rectangle
        + draw(Graphics): void
        - drawHealthBar(Graphics): void
    }

    class Archer {
        - int currentHealth
        - BaseEnemy lockedTarget
        - int attackTimer
        - boolean isAttacking
        - int attackAnimationTimer
        - ArrayList<BaseEnemy> enemyList
        - ArrayList<Arrow> arrows
        - Image normalImage
        - Image attackImage
        - Image arrowImage
        + Archer(int, int, int, Image, ArrayList<BaseEnemy>)
        + update(): void
        - isTargetValid(): boolean
        - acquireNewTarget(): void
        - calculateDistanceToTarget(BaseEnemy): double
        - performAttack(): void
        - shootArrow(): void
        + takeDamage(int): void
        + isDestroyed(): boolean
        + getCurrentHealth(): int
        + getMaxHealth(): int
        + getDefenseRating(): int
        + getCollisionBounds(): Rectangle
        + getBounds(): Rectangle
        + draw(Graphics): void
        - drawHealthBar(Graphics): void
    }

    class Assassin {
        - ArrayList<BaseEnemy> enemyList
        - ArrayList<BaseEnemy> attackedEnemies
        - int attackCooldown
        - boolean isAttacking
        - int attackAnimationTimer
        - Image normalImage
        - Image attackImage
        + Assassin(int, int, int, Image, ArrayList<BaseEnemy>)
        + update(): void
        - calculateDistanceToEnemy(BaseEnemy): double
        - attackEnemy(BaseEnemy): void
        + draw(Graphics): void
    }
}

' ============= Enemies Package =============
package "com.game.entities.enemies" {
    abstract class BaseEnemy {
        # double positionX
        # double positionY
        # ArrayList<Point> movementPath
        # int currentPathIndex
        # Map gameMap
        # House targetHouse
        # CoinManager coinManager
        # boolean isAttacking
        # int attackTimer
        # Object currentAttackTarget
        # boolean isDead
        # int currentHealth
        # int animationTimer
        # boolean useAlternateFrame
        # ArrayList<BaseEnemy> allEnemies
        # BaseEnemy(Map, CoinManager, int)
        # {abstract} getMaxHealth(): int
        # {abstract} getAttackDamage(): int
        # {abstract} getAttackCooldown(): int
        # {abstract} getSpeed(): double
        # {abstract} getSize(): int
        # {abstract} getXOffset(): int
        # {abstract} getYOffset(): int
        # {abstract} getIdleImage(): Image
        # {abstract} getWalkImage(): Image
        # {abstract} getAttackImage(): Image
        # {abstract} getCoinReward(): int
        # buildMovementPath(): void
        # findRoadStartPosition(int[][]): Point
        # findHousePosition(int[][]): Point
        # createPathUsingBFS(int[][], int, Point, Point): void
        # reconstructPath(Point[][], Point, Point, int): void
        - initializeStartingPosition(): void
        + update(): void
        - updateMovement(): void
        - moveTowardsNextPoint(): void
        - checkForBlockingEnemies(): boolean
        - findNearestDefensiveTarget(): void
        - updateAttack(): void
        # getCurrentImage(): Image
        + takeDamage(int): void
        + isDead(): boolean
        + getBounds(): Rectangle
        + draw(Graphics): void
        # drawHealthBar(Graphics): void
        + setAllEnemies(ArrayList<BaseEnemy>): void
    }

    class Enemy {
        - Image idleImage
        - Image walkImage
        - Image attackImage
        + Enemy(Map, CoinManager)
        # getMaxHealth(): int
        # getAttackDamage(): int
        # getAttackCooldown(): int
        # getSpeed(): double
        # getSize(): int
        # getXOffset(): int
        # getYOffset(): int
        # getIdleImage(): Image
        # getWalkImage(): Image
        # getAttackImage(): Image
        # getCoinReward(): int
    }

    class EnemyBoss {
        - Image idleImage
        - Image flyImage
        - Image attackImage
        - Image skillImage
        - Image skillBallImage
        - int skillCooldownTimer
        - boolean isUsingSkill
        - int skillAnimationTimer
        - ArrayList<BossSkillBall> skillBalls
        + EnemyBoss(Map, CoinManager)
        + update(): void
        - updateSkillBalls(): void
        - useSkill(): void
        + draw(Graphics): void
        - getCurrentBossImage(): Image
        # getMaxHealth(): int
        # getAttackDamage(): int
        # getAttackCooldown(): int
        # getSpeed(): double
        # getSize(): int
        # getXOffset(): int
        # getYOffset(): int
        # getIdleImage(): Image
        # getWalkImage(): Image
        # getAttackImage(): Image
        # getCoinReward(): int
    }
}

' ============= Projectiles Package =============
package "com.game.entities.projectiles" {
    abstract class Projectile {
        # double positionX
        # double positionY
        # BaseEnemy target
        # int damage
        # boolean isActive
        # Image projectileImage
        + Projectile(double, double, BaseEnemy, int, Image)
        + update(): void
        # hitTarget(): void
        + draw(Graphics): void
        + isActive(): boolean
        # {abstract} getSpeed(): double
        # {abstract} getSize(): int
        # {abstract} getFallbackColor(): Color
        # {abstract} getFallbackSize(): int
    }

    class Arrow {
        + Arrow(double, double, BaseEnemy, int, Image)
        # getSpeed(): double
        # getSize(): int
        # getFallbackColor(): Color
        # getFallbackSize(): int
    }

    class MagicBall {
        + MagicBall(double, double, BaseEnemy, int, Image)
        # getSpeed(): double
        # getSize(): int
        # getFallbackColor(): Color
        # getFallbackSize(): int
    }

    class BossSkillBall {
        - double positionX
        - double positionY
        - double velocityX
        - double velocityY
        - int damage
        - boolean isActive
        - Image ballImage
        - ArrayList<Defensive> defensiveUnits
        + BossSkillBall(double, double, double, int, Image, ArrayList<Defensive>)
        + update(): void
        - checkCollisions(): void
        - getUnitBounds(Defensive): Rectangle
        - hitTarget(Defensive): void
        + draw(Graphics): void
        - getBounds(): Rectangle
        + isActive(): boolean
    }
}

' ============= Relationships =============

' Main relationships
Main --> MenuPanel : creates
MenuPanel --> GamePanel : creates
GamePanel --> GameOverPanel : creates

' GamePanel relationships
GamePanel --> Map : uses
GamePanel --> CoinManager : uses
GamePanel --> WaveManager : uses
GamePanel --> BaseEnemy : manages

' WaveManager relationships
WaveManager --> Map : uses
WaveManager --> CoinManager : uses
WaveManager --> BaseEnemy : spawns
WaveManager --> Enemy : creates
WaveManager --> EnemyBoss : creates

' Map relationships
Map --> House : contains
Map --> Tank : manages
Map --> Magic : manages
Map --> Archer : manages
Map --> Assassin : manages

' Inheritance - Base
GameObject <|-- House
GameObject <|-- Tank
GameObject <|-- Magic
GameObject <|-- Archer
GameObject <|-- Assassin

' Interface Implementation - Defensive
Defensive <|.. House
Defensive <|.. Tank
Defensive <|.. Magic
Defensive <|.. Archer

' Interface Implementation - Collidable
Collidable <|.. Tank
Collidable <|.. Magic
Collidable <|.. Archer

' Inheritance - Enemies
BaseEnemy <|-- Enemy
BaseEnemy <|-- EnemyBoss

' Inheritance - Projectiles
Projectile <|-- Arrow
Projectile <|-- MagicBall

' Enemy relationships
BaseEnemy --> Map : uses
BaseEnemy --> House : attacks
BaseEnemy --> CoinManager : uses
BaseEnemy --> Tank : attacks
BaseEnemy --> Magic : attacks
BaseEnemy --> Archer : attacks

' Boss relationships
EnemyBoss --> BossSkillBall : creates

' Defensive unit relationships
Magic --> MagicBall : shoots
Magic --> BaseEnemy : targets
Archer --> Arrow : shoots
Archer --> BaseEnemy : targets
Assassin --> BaseEnemy : attacks

' Projectile relationships
Projectile --> BaseEnemy : targets
BossSkillBall --> Defensive : damages

' Utility usage
GamePanel ..> Constants : uses
Map ..> Constants : uses
BaseEnemy ..> Constants : uses
BaseEnemy ..> MathUtils : uses
Magic ..> MathUtils : uses
Archer ..> MathUtils : uses
Assassin ..> MathUtils : uses

' ImageLoader usage
MenuPanel ..> ImageLoader : uses
GameOverPanel ..> ImageLoader : uses
Map ..> ImageLoader : uses
Tank ..> ImageLoader : uses
Magic ..> ImageLoader : uses
Archer ..> ImageLoader : uses
Assassin ..> ImageLoader : uses
Enemy ..> ImageLoader : uses
EnemyBoss ..> ImageLoader : uses

@enduml
```

## คำอธิบาย Diagram

### Packages หลัก
1. **main** - จุดเริ่มต้นโปรแกรม
2. **core** - UI panels (Menu, Game, GameOver)
3. **utils** - Utility classes (Constants, MathUtils, ImageLoader)
4. **managers** - Game managers (Coin, Wave)
5. **world** - Map และการจัดการแผนที่
6. **entities.base** - Base classes และ interfaces
7. **entities.defensive** - หน่วยป้องกัน
8. **entities.enemies** - ศัตรู
9. **entities.projectiles** - กระสุน

### ความสัมพันธ์สำคัญ
- **Inheritance (|--)**: Enemy, EnemyBoss extends BaseEnemy
- **Implementation (|..)**: Tank, Magic, Archer implements Defensive
- **Association (-->)**: GamePanel uses Map, WaveManager, CoinManager
- **Dependency (..>)**: Classes use Constants, MathUtils

### Design Patterns
- **Template Method**: BaseEnemy มี abstract methods ให้ subclass implement
- **Strategy**: Defensive interface ให้หน่วยต่างๆ implement
- **Manager Pattern**: CoinManager, WaveManager จัดการ game state
- **Observer-like**: WaveManager spawns enemies, GamePanel observes state
