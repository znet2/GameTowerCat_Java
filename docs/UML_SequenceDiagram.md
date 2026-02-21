# UML Sequence Diagrams - Tower Defense Game

## 1. Game Startup Sequence

```plantuml
@startuml GameStartup
actor Player
participant Main
participant MenuPanel
participant GamePanel
participant Map
participant CoinManager
participant WaveManager

Player -> Main : เริ่มเกม
activate Main
Main -> Main : createAndShowGameWindow()
Main -> MenuPanel : new MenuPanel(frame)
activate MenuPanel
MenuPanel -> MenuPanel : loadImages()
MenuPanel -> MenuPanel : setupPanel()
Main -> MenuPanel : show()
deactivate MenuPanel

Player -> MenuPanel : คลิก Start Button
activate MenuPanel
MenuPanel -> GamePanel : new GamePanel(frame)
activate GamePanel
GamePanel -> Map : new Map()
activate Map
Map -> Map : loadImages()
Map -> Map : initializeMapObjects()
Map --> GamePanel : return map
deactivate Map

GamePanel -> CoinManager : new CoinManager()
activate CoinManager
CoinManager --> GamePanel : return coinManager
deactivate CoinManager

GamePanel -> WaveManager : new WaveManager(map, enemies, coinManager)
activate WaveManager
WaveManager --> GamePanel : return waveManager
deactivate WaveManager

GamePanel -> GamePanel : setupUserInterface()
GamePanel -> GamePanel : startGameLoop()
GamePanel --> MenuPanel : show game
deactivate GamePanel
deactivate MenuPanel
deactivate Main

@enduml
```

## 2. Wave Spawning Sequence

```plantuml
@startuml WaveSpawning
participant GamePanel
participant WaveManager
participant Map
participant Enemy
participant EnemyBoss
participant CoinManager

GamePanel -> WaveManager : startNextWave()
activate WaveManager
WaveManager -> WaveManager : calculateEnemiesForWave()
WaveManager -> WaveManager : calculateBossCount()
WaveManager -> WaveManager : resetWaveState()
WaveManager -> WaveManager : announceWaveStart()
WaveManager --> GamePanel : wave started
deactivate WaveManager

loop Every Frame
    GamePanel -> WaveManager : update()
    activate WaveManager
    
    alt Time to spawn enemy
        WaveManager -> WaveManager : shouldSpawnEnemy()
        
        alt Should spawn boss
            WaveManager -> WaveManager : shouldSpawnBoss()
            WaveManager -> EnemyBoss : new EnemyBoss(map, coinManager)
            activate EnemyBoss
            EnemyBoss -> Map : getRawMap()
            EnemyBoss -> EnemyBoss : buildMovementPath()
            EnemyBoss --> WaveManager : return boss
            deactivate EnemyBoss
        else Spawn normal enemy
            WaveManager -> Enemy : new Enemy(map, coinManager)
            activate Enemy
            Enemy -> Map : getRawMap()
            Enemy -> Enemy : buildMovementPath()
            Enemy --> WaveManager : return enemy
            deactivate Enemy
        end
        
        WaveManager -> WaveManager : add to activeEnemies
    end
    
    alt All enemies spawned
        WaveManager -> WaveManager : completeWave()
        WaveManager -> WaveManager : currentWaveNumber++
    end
    
    WaveManager --> GamePanel : updated
    deactivate WaveManager
end

@enduml
```

## 3. Player Places Defensive Unit Sequence

```plantuml
@startuml PlaceDefensiveUnit
actor Player
participant GamePanel
participant CoinManager
participant Map
participant Tank
participant Magic
participant Archer

Player -> GamePanel : เลือกหน่วย (Tank/Magic/Archer)
activate GamePanel
GamePanel -> GamePanel : selectedUnit = "tank"
GamePanel --> Player : แสดง preview
deactivate GamePanel

Player -> GamePanel : คลิกวางหน่วย
activate GamePanel
GamePanel -> GamePanel : getTileAtPixel(x, y)
GamePanel -> GamePanel : checkValidPlacement()

alt Can afford and valid position
    GamePanel -> CoinManager : canAfford(cost)
    activate CoinManager
    CoinManager --> GamePanel : true
    deactivate CoinManager
    
    GamePanel -> CoinManager : spendCoins(cost)
    activate CoinManager
    CoinManager -> CoinManager : currentCoins -= cost
    CoinManager --> GamePanel : true
    deactivate CoinManager
    
    alt Place Tank
        GamePanel -> Map : placeTank(col, row)
        activate Map
        Map -> Tank : new Tank(col, row, tileSize, image)
        activate Tank
        Tank --> Map : return tank
        deactivate Tank
        Map -> Map : add to defensiveTanks
        Map -> Map : repaint()
        Map --> GamePanel : placed
        deactivate Map
    else Place Magic
        GamePanel -> Map : placeMagic(col, row, enemies)
        activate Map
        Map -> Magic : new Magic(col, row, tileSize, image, enemies)
        activate Magic
        Magic --> Map : return magic
        deactivate Magic
        Map -> Map : add to magicTowers
        Map -> Map : repaint()
        Map --> GamePanel : placed
        deactivate Map
    else Place Archer
        GamePanel -> Map : placeArcher(col, row, enemies)
        activate Map
        Map -> Archer : new Archer(col, row, tileSize, image, enemies)
        activate Archer
        Archer --> Map : return archer
        deactivate Archer
        Map -> Map : add to archerTowers
        Map -> Map : repaint()
        Map --> GamePanel : placed
        deactivate Map
    end
    
    GamePanel --> Player : หน่วยถูกวาง
else Cannot afford or invalid
    GamePanel --> Player : แสดงข้อความ error
end
deactivate GamePanel

@enduml
```

## 4. Enemy Movement and Attack Sequence

```plantuml
@startuml EnemyMovementAttack
participant GamePanel
participant BaseEnemy
participant Map
participant Tank
participant Magic
participant House

loop Every Frame
    GamePanel -> BaseEnemy : update()
    activate BaseEnemy
    
    BaseEnemy -> BaseEnemy : updateAnimation()
    
    alt Not attacking
        BaseEnemy -> BaseEnemy : checkForDefensiveCollision()
        
        alt Collision with Tank
            BaseEnemy -> Map : getTanks()
            activate Map
            Map --> BaseEnemy : tanks list
            deactivate Map
            
            BaseEnemy -> Tank : getBounds()
            activate Tank
            Tank --> BaseEnemy : bounds
            deactivate Tank
            
            BaseEnemy -> BaseEnemy : getBounds().intersects()
            
            alt Collision detected
                BaseEnemy -> BaseEnemy : startAttacking(tank)
                BaseEnemy -> BaseEnemy : isAttacking = true
            end
        else Collision with Magic
            BaseEnemy -> Map : getMagicTowers()
            activate Map
            Map --> BaseEnemy : magic list
            deactivate Map
            
            BaseEnemy -> Magic : getBounds()
            activate Magic
            Magic --> BaseEnemy : bounds
            deactivate Magic
            
            alt Collision detected
                BaseEnemy -> BaseEnemy : startAttacking(magic)
            end
        end
        
        alt No collision
            BaseEnemy -> BaseEnemy : moveAlongPath()
            BaseEnemy -> BaseEnemy : isPathBlocked()
            
            alt Path not blocked
                BaseEnemy -> BaseEnemy : moveTowardsPoint()
            end
            
            BaseEnemy -> BaseEnemy : checkForHouseCollision()
            
            alt Reached house
                BaseEnemy -> House : getBounds()
                activate House
                House --> BaseEnemy : bounds
                deactivate House
                
                BaseEnemy -> BaseEnemy : startAttacking(house)
            end
        end
    else Attacking
        BaseEnemy -> BaseEnemy : processAttack()
        BaseEnemy -> BaseEnemy : attackTimer++
        
        alt Attack cooldown finished
            BaseEnemy -> BaseEnemy : executeAttack()
            
            alt Target is Tank
                BaseEnemy -> Tank : takeDamage(damage)
                activate Tank
                Tank -> Tank : currentHealth -= damage
                
                alt Tank destroyed
                    Tank -> Tank : currentHealth = 0
                    BaseEnemy -> BaseEnemy : stopAttacking()
                end
                deactivate Tank
            else Target is House
                BaseEnemy -> House : takeDamage(damage)
                activate House
                House -> House : currentHealth -= damage
                
                alt House destroyed
                    House -> House : currentHealth = 0
                    GamePanel -> GamePanel : gameOver(lose)
                end
                deactivate House
            end
        end
    end
    
    BaseEnemy --> GamePanel : updated
    deactivate BaseEnemy
end

@enduml
```

## 5. Magic Tower Attack Sequence

```plantuml
@startuml MagicTowerAttack
participant GamePanel
participant Map
participant Magic
participant BaseEnemy
participant MagicBall
participant CoinManager

loop Every Frame
    GamePanel -> Map : updateMagicTowers()
    activate Map
    
    Map -> Magic : update()
    activate Magic
    
    loop For each magic ball
        Magic -> MagicBall : update()
        activate MagicBall
        
        MagicBall -> MagicBall : move towards target
        
        alt Reached target
            MagicBall -> BaseEnemy : takeDamage(damage)
            activate BaseEnemy
            BaseEnemy -> BaseEnemy : currentHealth -= damage
            
            alt Enemy killed
                BaseEnemy -> BaseEnemy : isDead = true
                BaseEnemy -> CoinManager : addCoins(reward)
                activate CoinManager
                CoinManager -> CoinManager : currentCoins += reward
                CoinManager --> BaseEnemy : coins added
                deactivate CoinManager
            end
            
            BaseEnemy --> MagicBall : damaged
            deactivate BaseEnemy
            
            MagicBall -> MagicBall : isActive = false
        end
        
        MagicBall --> Magic : updated
        deactivate MagicBall
    end
    
    Magic -> Magic : isTargetValid()
    
    alt No valid target
        Magic -> Magic : acquireNewTarget()
        
        loop For each enemy
            Magic -> BaseEnemy : getBounds()
            activate BaseEnemy
            BaseEnemy --> Magic : bounds
            deactivate BaseEnemy
            
            Magic -> Magic : calculateDistanceToTarget()
            
            alt Enemy in range
                Magic -> Magic : lockedTarget = enemy
            end
        end
    end
    
    alt Has target
        Magic -> Magic : performAttack()
        Magic -> Magic : attackTimer++
        
        alt Attack cooldown finished
            Magic -> Magic : executeAttack()
            Magic -> Magic : attackCounter++
            
            alt 5th attack (spell)
                Magic -> Magic : castSpecialSpell()
                Magic -> MagicBall : new MagicBall(super)
                activate MagicBall
                MagicBall --> Magic : return ball
                deactivate MagicBall
                Magic -> Magic : attackCounter = 0
            else Normal attack
                Magic -> MagicBall : new MagicBall(normal)
                activate MagicBall
                MagicBall --> Magic : return ball
                deactivate MagicBall
            end
        end
    end
    
    Magic --> Map : updated
    deactivate Magic
    
    Map --> GamePanel : updated
    deactivate Map
end

@enduml
```

## 6. Boss Special Skill Sequence

```plantuml
@startuml BossSkill
participant GamePanel
participant EnemyBoss
participant BossSkillBall
participant Tank
participant Magic
participant Archer
participant House

loop Every Frame
    GamePanel -> EnemyBoss : update()
    activate EnemyBoss
    
    EnemyBoss -> EnemyBoss : skillCooldownTimer--
    
    alt Skill ready (timer = 0)
        EnemyBoss -> EnemyBoss : useSkill()
        EnemyBoss -> EnemyBoss : isUsingSkill = true
        
        EnemyBoss -> EnemyBoss : get all defensive units
        
        loop 16 times (360 degrees)
            EnemyBoss -> EnemyBoss : calculate angle
            EnemyBoss -> BossSkillBall : new BossSkillBall(x, y, angle, damage, image, units)
            activate BossSkillBall
            BossSkillBall -> BossSkillBall : calculate velocity
            BossSkillBall --> EnemyBoss : return ball
            deactivate BossSkillBall
            EnemyBoss -> EnemyBoss : add to skillBalls
        end
        
        EnemyBoss -> EnemyBoss : skillCooldownTimer = 600
    end
    
    loop For each skill ball
        EnemyBoss -> BossSkillBall : update()
        activate BossSkillBall
        
        BossSkillBall -> BossSkillBall : positionX += velocityX
        BossSkillBall -> BossSkillBall : positionY += velocityY
        
        alt Out of bounds
            BossSkillBall -> BossSkillBall : isActive = false
        else Check collisions
            loop For each defensive unit
                alt Collision with Tank
                    BossSkillBall -> Tank : takeDamage(damage)
                    activate Tank
                    Tank -> Tank : currentHealth -= damage
                    Tank --> BossSkillBall : damaged
                    deactivate Tank
                    BossSkillBall -> BossSkillBall : isActive = false
                else Collision with Magic
                    BossSkillBall -> Magic : takeDamage(damage)
                    activate Magic
                    Magic -> Magic : currentHealth -= damage
                    Magic --> BossSkillBall : damaged
                    deactivate Magic
                    BossSkillBall -> BossSkillBall : isActive = false
                else Collision with Archer
                    BossSkillBall -> Archer : takeDamage(damage)
                    activate Archer
                    Archer -> Archer : currentHealth -= damage
                    Archer --> BossSkillBall : damaged
                    deactivate Archer
                    BossSkillBall -> BossSkillBall : isActive = false
                else Collision with House
                    BossSkillBall -> House : takeDamage(damage)
                    activate House
                    House -> House : currentHealth -= damage
                    House --> BossSkillBall : damaged
                    deactivate House
                    BossSkillBall -> BossSkillBall : isActive = false
                end
            end
        end
        
        BossSkillBall --> EnemyBoss : updated
        deactivate BossSkillBall
    end
    
    EnemyBoss -> EnemyBoss : call super.update()
    EnemyBoss --> GamePanel : updated
    deactivate EnemyBoss
end

@enduml
```

## 7. Game Over Sequence

```plantuml
@startuml GameOver
participant GamePanel
participant House
participant WaveManager
participant CoinManager
participant GameOverPanel

loop Every Frame
    GamePanel -> House : getCurrentHealth()
    activate House
    House --> GamePanel : health
    deactivate House
    
    alt House destroyed
        GamePanel -> GamePanel : isGameRunning = false
        GamePanel -> GameOverPanel : new GameOverPanel(frame, false)
        activate GameOverPanel
        GameOverPanel -> GameOverPanel : loadImages() [lose images]
        GameOverPanel -> GameOverPanel : setupPanel()
        GameOverPanel --> GamePanel : show lose screen
        deactivate GameOverPanel
    else House alive
        GamePanel -> WaveManager : getCurrentWave()
        activate WaveManager
        WaveManager --> GamePanel : waveNumber
        deactivate WaveManager
        
        GamePanel -> WaveManager : isWaveFinished()
        activate WaveManager
        WaveManager --> GamePanel : finished
        deactivate WaveManager
        
        alt All waves completed
            GamePanel -> GamePanel : isGameRunning = false
            GamePanel -> GameOverPanel : new GameOverPanel(frame, true)
            activate GameOverPanel
            GameOverPanel -> GameOverPanel : loadImages() [win images]
            GameOverPanel -> GameOverPanel : setupPanel()
            GameOverPanel --> GamePanel : show win screen
            deactivate GameOverPanel
        else More waves remaining
            GamePanel -> CoinManager : awardCoinsForWaveComplete()
            activate CoinManager
            CoinManager -> CoinManager : addCoins(bonus)
            CoinManager --> GamePanel : coins awarded
            deactivate CoinManager
            
            GamePanel -> WaveManager : startNextWave()
            activate WaveManager
            WaveManager -> WaveManager : calculateEnemiesForWave()
            WaveManager --> GamePanel : wave started
            deactivate WaveManager
        end
    end
end

@enduml
```

## คำอธิบาย Sequence Diagrams

### 1. Game Startup
แสดงขั้นตอนการเริ่มเกมตั้งแต่ Main → MenuPanel → GamePanel และการสร้าง components ต่างๆ

### 2. Wave Spawning
แสดงกระบวนการ spawn ศัตรูในแต่ละ wave รวมถึงการตัดสินใจว่าจะ spawn Boss หรือไม่

### 3. Place Defensive Unit
แสดงขั้นตอนการวางหน่วยป้องกัน ตั้งแต่การเลือก ตรวจสอบเงิน ไปจนถึงการวางจริง

### 4. Enemy Movement and Attack
แสดงการเคลื่อนที่ของศัตรู การตรวจสอบการชน และการโจมตีหน่วยป้องกัน

### 5. Magic Tower Attack
แสดงระบบการโจมตีของ Magic tower รวมถึงการยิง MagicBall และการทำ special spell

### 6. Boss Special Skill
แสดงการใช้สกิลพิเศษของ Boss ที่ยิง BossSkillBall รอบตัว 360 องศา

### 7. Game Over
แสดงเงื่อนไขการจบเกม ทั้งแพ้ (บ้านถูกทำลาย) และชนะ (ผ่านทุก wave)
