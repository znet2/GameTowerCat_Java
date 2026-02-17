# Constants.java - ศูนย์รวมค่าคงที่ของเกม

## ภาพรวม
Constants เป็นคลาสที่เก็บค่าคงที่ทั้งหมดของเกม จัดกลุ่มตามหมวดหมู่เพื่อให้ง่ายต่อการจัดการและแก้ไข

## โครงสร้างคลาส
```java
public final class Constants
```
- `final class` - ป้องกันการสืบทอด
- `private constructor` - ป้องกันการสร้าง instance
- `static nested classes` - จัดกลุ่มค่าคงที่ตามหมวดหมู่

## หมวดหมู่ค่าคงที่

### Game Configuration
```java
public static final class Game
```
**วัตถุประสงค์**: ค่าคงที่พื้นฐานของเกม

#### ค่าคงที่
- `TARGET_FPS = 60` - เป้าหมาย frame rate
- `TITLE = "Tower Defense"` - ชื่อเกม
- `NANOSECONDS_PER_FRAME = 1_000_000_000.0 / TARGET_FPS` - nanosecond ต่อ frame
- `WINDOW_WIDTH = 1920` - ความกว้างหน้าต่าง (Full HD)
- `WINDOW_HEIGHT = 1080` - ความสูงหน้าต่าง (Full HD)

#### การใช้งาน
- **Game Loop**: ใช้ NANOSECONDS_PER_FRAME สำหรับ delta time
- **Window Setup**: ใช้ WINDOW_WIDTH/HEIGHT สำหรับขนาดหน้าต่าง
- **Title Bar**: ใช้ TITLE สำหรับชื่อหน้าต่าง

### Map and Tile Configuration
```java
public static final class Map
```
**วัตถุประสงค์**: ค่าคงที่เกี่ยวกับแผนที่และไทล์

#### ค่าคงที่พื้นฐาน
- `TILE_SIZE = 32` - ขนาดไทล์เป็นพิกเซล
- `HOUSE_COLUMN = 53` - คอลัมน์ของบ้าน
- `HOUSE_ROW = 9` - แถวของบ้าน

#### ประเภทไทล์
- `TILE_ROAD = 0` - ถนน (ศัตรูเดินได้, วางหน่วยได้)
- `TILE_GRASS = 1` - หญ้า (ศัตรูเดินไม่ได้)
- `TILE_WATER = 2` - น้ำ (ตกแต่ง)
- `TILE_WATER_UP = 3` - น้ำขอบบน
- `TILE_WATER_DOWN = 4` - น้ำขอบล่าง
- `TILE_WATER_LEFT = 5` - น้ำขอบซ้าย
- `TILE_WATER_RIGHT = 6` - น้ำขอบขวา
- `TILE_TREE = 7` - ต้นไม้

#### การใช้งาน
- **Map Rendering**: ใช้ TILE_SIZE สำหรับการวาดไทล์
- **Pathfinding**: ใช้ TILE_ROAD สำหรับการหาเส้นทาง
- **Collision**: ใช้ประเภทไทล์สำหรับการตรวจสอบการชน

### Entity Configuration
```java
public static final class Entities
```
**วัตถุประสงค์**: ค่าคงที่ของหน่วยต่างๆ ในเกม

#### Common Configuration
- `MINIMUM_HEALTH = 0` - เลือดต่ำสุด
- `ATTACK_ANIMATION_DURATION = 15` - ระยะเวลา animation การโจมตี (frames)
- `SPELL_ANIMATION_DURATION = 30` - ระยะเวลา animation เวทมนตร์ (frames)

#### Tank Configuration
- `TANK_INITIAL_HEALTH = 16000` - เลือดเริ่มต้น Tank
- `TANK_COST = 120` - ราคา Tank
- `TANK_X_OFFSET = 0` - offset การแสดงผลแนวนอน
- `TANK_Y_OFFSET = -30` - offset การแสดงผลแนวตั้ง

#### Magic Configuration
- `MAGIC_INITIAL_HEALTH = 5000` - เลือดเริ่มต้น Magic
- `MAGIC_DEFENSE_RATING = 0` - ค่าป้องกัน Magic
- `MAGIC_COST = 100` - ราคา Magic
- `MAGIC_ATTACK_DAMAGE = 250` - ความเสียหายการโจมตีปกติ
- `MAGIC_SPELL_DAMAGE = 400` - ความเสียหายเวทมนตร์พิเศษ
- `MAGIC_ATTACK_COOLDOWN_FRAMES = 60` - cooldown การโจมตี (1 วินาที)
- `MAGIC_ATTACK_RANGE = 250` - ระยะโจมตี
- `MAGIC_ATTACKS_BEFORE_SPELL = 4` - จำนวนการโจมตีก่อนใช้เวทมนตร์
- `MAGIC_X_OFFSET = 0` - offset การแสดงผลแนวนอน
- `MAGIC_Y_OFFSET = -30` - offset การแสดงผลแนวตั้ง

#### Archer Configuration
- `ARCHER_INITIAL_HEALTH = 5000` - เลือดเริ่มต้น Archer
- `ARCHER_DEFENSE_RATING = 0` - ค่าป้องกัน Archer
- `ARCHER_COST = 200` - ราคา Archer
- `ARCHER_ATTACK_DAMAGE = 100` - ความเสียหาย
- `ARCHER_ATTACK_COOLDOWN_FRAMES = 30` - cooldown การโจมตี (0.5 วินาที)
- `ARCHER_ATTACK_RANGE = 400` - ระยะโจมตี
- `ARCHER_X_OFFSET = 0` - offset การแสดงผลแนวนอน
- `ARCHER_Y_OFFSET = -30` - offset การแสดงผลแนวตั้ง

#### Assassin Configuration
- `ASSASSIN_COST = 150` - ราคา Assassin
- `ASSASSIN_ATTACK_DAMAGE = 500` - ความเสียหาย Assassin
- `ASSASSIN_ATTACK_RANGE = 80` - ระยะโจมตีใกล้ (melee)
- `ASSASSIN_ATTACK_COOLDOWN = 45` - cooldown การโจมตี (0.75 วินาที)
- `ASSASSIN_X_OFFSET = 0` - offset การแสดงผลแนวนอน
- `ASSASSIN_Y_OFFSET = -30` - offset การแสดงผลแนวตั้ง

**หมายเหตุ**: X_OFFSET และ Y_OFFSET ใช้สำหรับปรับตำแหน่งการแสดงผลให้ตัวละครแต่ละตัวยืนตรงกันในแมพ เนื่องจากขนาดรูปภาพไม่เท่ากัน

#### House Configuration
- `HOUSE_INITIAL_HEALTH = 10000` - เลือดเริ่มต้นบ้าน
- `HOUSE_WIDTH_TILES = 7` - ความกว้างบ้านเป็นไทล์
- `HOUSE_HEIGHT_TILES = 7` - ความสูงบ้านเป็นไทล์

#### Enemy Configuration
- `ENEMY_SIZE = 64` - ขนาดศัตรูเป็นพิกเซล
- `ENEMY_SPEED = 0.5` - ความเร็วศัตรู
- `ENEMY_INITIAL_HEALTH = 3000` - เลือดเริ่มต้นศัตรู
- `ENEMY_ATTACK_DAMAGE = 500` - ความเสียหายศัตรู
- `ENEMY_ATTACK_COOLDOWN_FRAMES = 60` - cooldown การโจมตี (1 วินาที)
- `ENEMY_X_OFFSET = -25` - offset การแสดงผลแนวนอน
- `ENEMY_Y_OFFSET = -25` - offset การแสดงผลแนวตั้ง
- `ENEMY_POSITION_OFFSET_MULTIPLIER = 12` - ระยะห่างระหว่างศัตรูเมื่อโจมตีเป้าหมายเดียวกัน (ป้องกันการซ้อนทับ)

### Projectile Configuration
```java
public static final class Projectiles
```
**วัตถุประสงค์**: ค่าคงที่เกี่ยวกับ projectiles (ลูกธนู, ลูกไฟ)

#### Arrow Configuration
- `ARROW_SPEED = 5.0` - ความเร็วลูกธนู (พิกเซลต่อ frame)
- `ARROW_SIZE = 24` - ขนาดลูกธนู (พิกเซล)
- `ARROW_FALLBACK_COLOR = Color(139, 69, 19)` - สีน้ำตาลสำหรับ fallback
- `ARROW_FALLBACK_SIZE = 6` - ขนาดวงกลม fallback

#### Magic Ball Configuration
- `MAGIC_BALL_SPEED = 6.0` - ความเร็วลูกไฟ (พิกเซลต่อ frame, เร็วกว่า Arrow)
- `MAGIC_BALL_SIZE = 20` - ขนาดลูกไฟ (พิกเซล, เล็กกว่า Arrow)
- `MAGIC_BALL_FALLBACK_COLOR = Color(255, 0, 255)` - สีม่วงแดงสำหรับ fallback
- `MAGIC_BALL_FALLBACK_SIZE = 10` - ขนาดวงกลม fallback
- `ENEMY_INITIAL_HEALTH = 50` - เลือดเริ่มต้นศัตรู
- `ENEMY_ATTACK_DAMAGE = 5` - ความเสียหายศัตรู
- `ENEMY_ATTACK_COOLDOWN_FRAMES = 60` - cooldown การโจมตี (1 วินาที)
- `ENEMY_X_OFFSET = -25` - offset การแสดงผลแนวนอน
- `ENEMY_Y_OFFSET = -25` - offset การแสดงผลแนวตั้ง
- `ENEMY_POSITION_OFFSET_MULTIPLIER = 12` - ระยะห่างระหว่างศัตรูเมื่อโจมตีเป้าหมายเดียวกัน (ป้องกันการซ้อนทับ)

### UI Configuration
```java
public static final class UI
```
**วัตถุประสงค์**: ค่าคงที่เกี่ยวกับ User Interface

#### Hero Bar (แถบ UI ด้านล่าง)
- `HERO_BAR_HEIGHT = 80` - ความสูงแถบ UI
- `TANK_ICON_SIZE = 48` - ขนาดไอคอนหน่วย
- `TANK_ICON_MARGIN = 20` - ระยะห่างระหว่างไอคอน
- `TANK_ICON_TOP_MARGIN = 16` - ระยะห่างจากด้านบน

#### Menu Configuration
- `START_BUTTON_WIDTH = 510` - ความกว้างปุ่ม Start
- `START_BUTTON_HEIGHT = 240` - ความสูงปุ่ม Start
- `START_BUTTON_Y_OFFSET = 150` - offset ปุ่ม Start จากกึ่งกลาง (บวก = ลง, ลบ = ขึ้น)
- `LOGO_WIDTH = 750` - ความกว้างโลโก้
- `LOGO_HEIGHT = 400` - ความสูงโลโก้
- `LOGO_TOP_MARGIN = 150` - ระยะห่างโลโก้จากด้านบน

#### Game Over Screen
**Win Image**:
- `WIN_IMAGE_WIDTH = 870` - ความกว้างรูปชนะ
- `WIN_IMAGE_HEIGHT = 460` - ความสูงรูปชนะ
- `WIN_IMAGE_Y_OFFSET = -150` - offset รูปชนะจากกึ่งกลาง (บวก = ลง, ลบ = ขึ้น)

**Lose Image**:
- `LOSE_IMAGE_WIDTH = 850` - ความกว้างรูปแพ้
- `LOSE_IMAGE_HEIGHT = 560` - ความสูงรูปแพ้
- `LOSE_IMAGE_Y_OFFSET = -150` - offset รูปแพ้จากกึ่งกลาง (บวก = ลง, ลบ = ขึ้น)

**Restart Button**:
- `RESTART_BUTTON_WIDTH = 400` - ความกว้างปุ่ม Restart
- `RESTART_BUTTON_HEIGHT = 200` - ความสูงปุ่ม Restart
- `RESTART_BUTTON_Y_OFFSET = 45` - offset ปุ่ม Restart จากรูปผลลัพธ์

#### Colors
- `HERO_BAR_COLOR = Color.DARK_GRAY` - สีแถบ UI
- `TANK_ICON_COLOR = Color.ORANGE` - สีไอคอน Tank (ซื้อได้)
- `TANK_ICON_DISABLED_COLOR = Color.GRAY` - สีไอคอน Tank (ซื้อไม่ได้)
- `MAGIC_ICON_COLOR = Color.CYAN` - สีไอคอน Magic
- `MAGIC_ICON_DISABLED_COLOR = Color.GRAY` - สีไอคอน Magic (ซื้อไม่ได้)
- `DRAG_PREVIEW_COLOR = new Color(255, 165, 0, 150)` - สีตัวอย่างการลาก

#### Coin Display
- `COIN_TEXT_COLOR = Color.YELLOW` - สีข้อความเหรียญ
- `COIN_BACKGROUND_COLOR = new Color(0, 0, 0, 128)` - สีพื้นหลังเหรียญ
- `COIN_FONT = new Font("Arial", Font.BOLD, 16)` - ฟอนต์เหรียญ
- `COIN_DISPLAY_PADDING = 10` - padding การแสดงผลเหรียญ

### Economy Configuration
```java
public static final class Economy
```
**วัตถุประสงค์**: ค่าคงที่เกี่ยวกับระบบเศรษฐกิจ

#### ค่าคงที่
- `STARTING_COINS = 640` - เหรียญเริ่มต้น
- `COINS_PER_ENEMY_KILL = 15` - เหรียญรางวัลการฆ่าศัตรูแต่ละตัว
- `COINS_PER_WAVE_COMPLETE = 200` - เหรียญโบนัสเมื่อจบแต่ละ wave

#### การใช้งาน
- **Game Start**: ใช้ STARTING_COINS สำหรับเหรียญเริ่มต้น
- **Enemy Kill**: ได้รับ COINS_PER_ENEMY_KILL ทุกครั้งที่ฆ่าศัตรู
- **Wave Complete**: ได้รับ COINS_PER_WAVE_COMPLETE เมื่อจบแต่ละ wave

### Wave Configuration
```java
public static final class Waves
```
**วัตถุประสงค์**: ค่าคงที่เกี่ยวกับระบบ wave

#### ค่าคงที่
- `BASE_ENEMIES_PER_WAVE = 3` - จำนวนศัตรูพื้นฐานต่อ wave
- `ENEMIES_INCREASE_PER_WAVE = 3` - จำนวนศัตรูเพิ่มต่อ wave
- `SPAWN_DELAY_FRAMES = 30` - ช่วงเวลาการสร้างศัตรู (0.5 วินาที)
- `MAX_WAVES = 5` - จำนวน wave ทั้งหมดเพื่อชนะ

#### สูตรการคำนวณ
```
จำนวนศัตรูใน wave = BASE_ENEMIES_PER_WAVE + (wave_number * ENEMIES_INCREASE_PER_WAVE)
Wave 1: 3 + (1 * 3) = 6 ตัว
Wave 2: 3 + (2 * 3) = 9 ตัว
Wave 3: 3 + (3 * 3) = 12 ตัว
Wave 4: 3 + (4 * 3) = 15 ตัว
Wave 5: 3 + (5 * 3) = 18 ตัว
รวมทั้งหมด: 6 + 9 + 12 + 15 + 18 = 60 ตัว
```

### File Paths
```java
public static final class Paths
```
**วัตถุประสงค์**: path ของไฟล์รูปภาพทั้งหมด

#### Base Path
- `IMAGES = "image/"` - โฟลเดอร์รูปภาพหลัก

#### Entity Images
- `ENEMY_IMAGE = IMAGES + "catEnemy.png"` - รูปศัตรู
- `TANK_IMAGE = IMAGES + "tank.png"` - รูป Tank ปกติ
- `TANK_DEFEND_IMAGE = IMAGES + "tankDef.png"` - รูป Tank ป้องกัน
- `HOUSE_IMAGE = IMAGES + "castle.png"` - รูปบ้าน
- `MAGIC_IMAGE = IMAGES + "magic.png"` - รูป Magic tower
- `MAGIC_BOMB_IMAGE = IMAGES + "magicBomb.png"` - รูป Magic เวทมนตร์
- `NORMAL_MAGIC_BALL_IMAGE = IMAGES + "normalMagicBall.png"` - ลูกไฟปกติ
- `SUPER_MAGIC_BALL_IMAGE = IMAGES + "superMagicBall.png"` - ลูกไฟพิเศษ
- `ARCHER_IMAGE = IMAGES + "archer.png"` - รูป Archer ปกติ
- `ARCHER_ATTACK_IMAGE = IMAGES + "archerAttack.png"` - รูป Archer โจมตี
- `ARROW_IMAGE = IMAGES + "arrow.png"` - ลูกธนุ
- `ASSASSIN_IMAGE = IMAGES + "assasin.png"` - รูป Assassin (ไฟล์สะกดผิด)
- `ASSASSIN_ATTACK_IMAGE = IMAGES + "assasinAttack.png"` - รูป Assassin โจมตี

#### Projectile Images (ย้ายไปอยู่ใน Entity Images แล้ว)

#### UI Images
- `WALLPAPER_IMAGE = IMAGES + "wallpaper.png"` - พื้นหลังเมนู
- `START_BUTTON_IMAGE = IMAGES + "startbotton.png"` - ปุ่ม Start
- `LOGO_GAME_IMAGE = IMAGES + "logoGame.png"` - โลโก้เกม
- `WIN_IMAGE = IMAGES + "win.png"` - รูปชนะ
- `LOSE_IMAGE = IMAGES + "lose.png"` - รูปแพ้
- `WIN_BACKGROUND_IMAGE = IMAGES + "winBackground.png"` - พื้นหลังชนะ
- `LOSE_BACKGROUND_IMAGE = IMAGES + "loseBackground.png"` - พื้นหลังแพ้
- `RESTART_BUTTON_IMAGE = IMAGES + "restart.png"` - ปุ่ม Restart

#### Tile Images
- `GRASS_TILE = IMAGES + "grass.png"` - ไทล์หญ้า
- `ROAD_TILE = IMAGES + "dirt.png"` - ไทล์ถนน
- `WATER_TILE = IMAGES + "water.png"` - ไทล์น้ำ
- `WATER_UP_TILE = IMAGES + "water_up.png"` - ไทล์น้ำขอบบน
- `WATER_DOWN_TILE = IMAGES + "water_down.png"` - ไทล์น้ำขอบล่าง
- `WATER_LEFT_TILE = IMAGES + "water_left.png"` - ไทล์น้ำขอบซ้าย
- `WATER_RIGHT_TILE = IMAGES + "water_right.png"` - ไทล์น้ำขอบขวา
- `TREE_TILE = IMAGES + "water.png"` - ไทล์ต้นไม้ (ใช้รูปน้ำ)

## การใช้งานและประโยชน์

### ข้อดีของการใช้ Constants
1. **จัดการง่าย**: แก้ไขค่าได้ที่เดียว
2. **ป้องกันข้อผิดพลาด**: ไม่ต้องจำค่าตัวเลข
3. **อ่านโค้ดง่าย**: ชื่อตัวแปรอธิบายความหมาย
4. **การปรับสมดุล**: แก้ไขค่าเกมได้ง่าย

### การจัดกลุ่ม
- **Game**: ค่าพื้นฐานของเกม
- **Map**: ค่าเกี่ยวกับแผนที่
- **Entities**: ค่าเกี่ยวกับหน่วยต่างๆ
- **Projectiles**: ค่าเกี่ยวกับ projectiles (ลูกธนู, ลูกไฟ)
- **UI**: ค่าเกี่ยวกับ interface
- **Economy**: ค่าเกี่ยวกับเศรษฐกิจ
- **Waves**: ค่าเกี่ยวกับ wave system
- **Paths**: path ของไฟล์รูปภาพ

### การเข้าถึง
```java
// ตัวอย่างการใช้งาน
int windowWidth = Constants.Game.WINDOW_WIDTH;
int tankCost = Constants.Entities.TANK_COST;
double arrowSpeed = Constants.Projectiles.ARROW_SPEED;
String enemyImage = Constants.Paths.ENEMY_IMAGE;
Color heroBarColor = Constants.UI.HERO_BAR_COLOR;
```

## ความสัมพันธ์กับคลาสอื่น

### ทุกคลาสในเกม
- ใช้ Constants สำหรับค่าต่างๆ แทนการ hard-code
- ลดการผิดพลาดจากการพิมพ์ตัวเลขผิด
- ทำให้การปรับแต่งเกมง่ายขึ้น

### การปรับสมดุลเกม
- แก้ไขค่าใน Constants เพื่อปรับความยาก
- ทดสอบค่าใหม่ได้ง่าย
- ไม่ต้องหาค่าในไฟล์หลายๆ ที่

## จุดเด่นของการออกแบบ

### การจัดระเบียบ
- แยกกลุ่มตามหน้าที่ชัดเจน
- ใช้ nested classes เพื่อจัดกลุ่ม
- ตั้งชื่อตัวแปรที่อธิบายความหมาย

### ความปลอดภัย
- ใช้ final class ป้องกันการสืบทอด
- ใช้ private constructor ป้องกันการสร้าง instance
- ใช้ static final สำหรับค่าคงที่

### ความยืดหยุ่น
- สามารถเพิ่มกลุ่มใหม่ได้ง่าย
- แก้ไขค่าได้ที่เดียว
- รองรับการขยายระบบในอนาคต

### การบำรุงรักษา
- ค้นหาและแก้ไขค่าได้ง่าย
- ลดโอกาสเกิดข้อผิดพลาด
- ทำให้โค้ดอ่านง่ายและเข้าใจง่าย