# HealthBar.java - คอมโพเนนต์แถบเลือดแบบใช้ซ้ำได้

## ภาพรวม
HealthBar เป็นคอมโพเนนต์ที่ใช้แสดงสถานะเลือดของหน่วยต่างๆ สามารถใช้ได้กับ Tank, House, Enemy หรือหน่วยใดก็ตามที่มีระบบเลือด

## คลาสและการสืบทอด
```java
public class HealthBar
```
- คลาสอิสระที่ไม่สืบทอดจากคลาสอื่น
- เป็น utility class สำหรับการแสดงผลแถบเลือด

## ตัวแปรสำคัญ

### ค่าคงที่สี
- `static final Color BACKGROUND_COLOR = Color.RED` - สีพื้นหลัง (เลือดที่หายไป)
- `static final Color HEALTH_COLOR = Color.GREEN` - สีแถบเลือด
- `static final Color BORDER_COLOR = Color.BLACK` - สีขอบ
- `static final int DEFAULT_HEIGHT = 4` - ความสูงเริ่มต้น

### ตัวแปรขนาด
- `final int width` - ความกว้างของแถบเลือด
- `final int height` - ความสูงของแถบเลือด

## Methods อย่างละเอียด

### Constructor แบบกำหนดขนาดเต็ม
```java
public HealthBar(int width, int height)
```
**วัตถุประสงค์**: สร้างแถบเลือดด้วยขนาดที่กำหนด
**พารามิเตอร์**:
- `width` - ความกว้างของแถบเลือดเป็นพิกเซล
- `height` - ความสูงของแถบเลือดเป็นพิกเซล

**การทำงาน**: เก็บค่าขนาดสำหรับใช้ในการวาด

### Constructor แบบความสูงเริ่มต้น
```java
public HealthBar(int width)
```
**วัตถุประสงค์**: สร้างแถบเลือดด้วยความสูงเริ่มต้น
**พารามิเตอร์**: `width` - ความกว้างของแถบเลือดเป็นพิกเซล
**การทำงาน**: เรียก constructor หลักด้วย DEFAULT_HEIGHT (4 พิกเซล)

### render() แบบสีเริ่มต้น
```java
public void render(Graphics graphics, int x, int y, int currentHealth, int maxHealth)
```
**วัตถุประสงค์**: วาดแถบเลือดที่ตำแหน่งที่กำหนด
**พารามิเตอร์**:
- `graphics` - Graphics context สำหรับการวาด
- `x, y` - ตำแหน่งที่จะวาดแถบเลือด
- `currentHealth` - เลือดปัจจุบัน
- `maxHealth` - เลือดสูงสุด

**การทำงาน**:
1. **ตรวจสอบเงื่อนไข**: หากเลือดเต็ม (currentHealth >= maxHealth) ไม่วาดอะไร
2. **คำนวณเปอร์เซ็นต์**: healthPercentage = currentHealth / maxHealth
3. **คำนวณความกว้างเลือด**: healthWidth = width × healthPercentage
4. **วาดพื้นหลัง**: วาดสี่เหลี่ยมสีแดงเต็มความกว้าง
5. **วาดแถบเลือด**: วาดสี่เหลี่ยมสีเขียวตามเปอร์เซ็นต์เลือด
6. **วาดขอบ**: วาดเส้นขอบสีดำ

### render() แบบสีกำหนดเอง
```java
public void render(Graphics graphics, int x, int y, int currentHealth, int maxHealth, 
                  Color backgroundColor, Color healthColor)
```
**วัตถุประสงค์**: วาดแถบเลือดด้วยสีที่กำหนดเอง
**พารามิเตอร์เพิ่มเติม**:
- `backgroundColor` - สีสำหรับพื้นหลัง (เลือดที่หายไป)
- `healthColor` - สีสำหรับส่วนเลือด

**การทำงาน**: เหมือน render() แบบเริ่มต้น แต่ใช้สีที่ส่งเข้ามา

## การใช้งานในเกม

### การใช้งานพื้นฐาน
```java
// สร้างแถบเลือดขนาด 50x4 พิกเซล
HealthBar healthBar = new HealthBar(50);

// วาดแถบเลือดเหนือหน่วย
healthBar.render(graphics, unitX, unitY - 10, currentHP, maxHP);
```

### การใช้งานกับสีกำหนดเอง
```java
// สร้างแถบเลือดสำหรับ Magic tower (สีฟ้า)
HealthBar magicHealthBar = new HealthBar(64, 6);

// วาดด้วยสีฟ้าแทนสีเขียว
magicHealthBar.render(graphics, magicX, magicY - 12, 
                     currentHP, maxHP, Color.RED, Color.CYAN);
```

### การใช้งานในคลาสหน่วย
```java
// ใน Tank.java
private void drawHealthBar(Graphics graphics) {
    if (currentHealth < Constants.Entities.TANK_INITIAL_HEALTH) {
        HealthBar healthBar = new HealthBar(objectWidth);
        healthBar.render(graphics, positionX, positionY + Y_OFFSET - 8, 
                        currentHealth, Constants.Entities.TANK_INITIAL_HEALTH);
    }
}
```

## คุณสมบัติของ HealthBar

### การแสดงผลแบบมีเงื่อนไข
- **แสดงเฉพาะเมื่อเลือดไม่เต็ม**: ไม่วาดแถบเลือดหากเลือดเต็ม 100%
- **ประหยัดการแสดงผล**: ลดการวาดที่ไม่จำเป็น
- **UI ที่สะอาด**: ไม่มีแถบเลือดรกหน้าจอ

### การคำนวณที่แม่นยำ
- **เปอร์เซ็นต์เลือด**: ใช้ double เพื่อความแม่นยำ
- **ความกว้างแถบ**: คำนวณตามสัดส่วนที่แท้จริง
- **การปัดเศษ**: ใช้ (int) cast สำหรับพิกัดพิกเซล

### ความยืดหยุ่นในการใช้งาน
- **ขนาดปรับได้**: กำหนดความกว้างและความสูงได้
- **สีปรับได้**: ใช้สีเริ่มต้นหรือกำหนดเองได้
- **ตำแหน่งอิสระ**: วาดได้ที่ตำแหน่งใดก็ได้

## การออกแบบแถบเลือด

### โครงสร้างการวาด
```
┌─────────────────────────────────┐ ← ขอบสีดำ
│████████████░░░░░░░░░░░░░░░░░░░░│ ← เขียว = เลือด, แดง = พื้นหลัง
└─────────────────────────────────┘
```

### การคำนวณขนาด
```java
// ตัวอย่าง: เลือด 30/50 (60%)
double healthPercentage = 30.0 / 50.0; // 0.6
int healthWidth = (int)(100 * 0.6);    // 60 pixels
```

### การจัดตำแหน่ง
```java
// วาดเหนือหน่วย 8 พิกเซล
int barX = unitX;
int barY = unitY - 8;

// วาดกึ่งกลางเหนือหน่วย
int barX = unitX + (unitWidth - barWidth) / 2;
int barY = unitY - 12;
```

## ตัวอย่างการใช้งานในหน่วยต่างๆ

### Tank
```java
// ใน Tank.java
private void drawHealthBar(Graphics graphics) {
    if (currentHealth < Constants.Entities.TANK_INITIAL_HEALTH) {
        HealthBar healthBar = new HealthBar(objectWidth);
        healthBar.render(graphics, positionX, positionY + Y_OFFSET - 8, 
                        currentHealth, getMaxHealth());
    }
}
```

### Enemy
```java
// ใน Enemy.java
private void drawHealthBar(Graphics graphics) {
    if (currentHealth < Constants.Entities.ENEMY_INITIAL_HEALTH) {
        HealthBar healthBar = new HealthBar(Constants.Entities.ENEMY_SIZE);
        healthBar.render(graphics, 
                        (int)positionX + Constants.Entities.ENEMY_X_OFFSET,
                        (int)positionY + Constants.Entities.ENEMY_Y_OFFSET - 8,
                        currentHealth, getMaxHealth());
    }
}
```

### House (ใน GamePanel)
```java
// ใน GamePanel.java
private void drawHouseHealthBar(Graphics graphics) {
    int currentHealth = gameMap.getHouse().getHealth();
    int maxHealth = Constants.Entities.HOUSE_INITIAL_HEALTH;
    
    HealthBar houseHealthBar = new HealthBar(200, 20);
    houseHealthBar.render(graphics, getWidth() - 220, 20, 
                         currentHealth, maxHealth);
}
```

## ข้อดีของการใช้ HealthBar

### Code Reuse
- ใช้ซ้ำได้กับหน่วยทุกประเภท
- ลดการเขียนโค้ดซ้ำสำหรับการวาดแถบเลือด
- มีการทำงานที่สอดคล้องกันทั่วทั้งเกม

### Consistency
- แถบเลือดทุกอันมีรูปลักษณ์เหมือนกัน
- สีและรูปแบบมาตรฐาน
- พฤติกรรมการแสดงผลเหมือนกัน

### Flexibility
- ปรับขนาดได้ตามต้องการ
- เปลี่ยนสีได้
- วาดได้ที่ตำแหน่งใดก็ได้

### Performance
- ไม่สร้าง object ใหม่ทุกครั้ง (สามารถ reuse ได้)
- การคำนวณเรียบง่าย
- วาดเฉพาะเมื่อจำเป็น

## ความสัมพันธ์กับคลาสอื่น

### Tank.java / Magic.java / Archer.java
- ใช้ HealthBar เพื่อแสดงเลือดเหนือหน่วย
- เรียกใน draw() method ของแต่ละหน่วย

### Enemy.java
- ใช้ HealthBar เพื่อแสดงเลือดเหนือศัตรู
- แสดงเฉพาะเมื่อเลือดไม่เต็ม

### GamePanel.java
- ใช้ HealthBar สำหรับแสดงเลือดบ้าน
- วาดที่มุมขวาบนของหน้าจอ

## จุดเด่นของการออกแบบ

### Utility Class
- เป็นคลาสที่มีหน้าที่เฉพาะเจาะจง
- ไม่ขึ้นกับคลาสอื่น
- ใช้งานได้อิสระ

### Configurable
- รองรับการปรับแต่งขนาดและสี
- มี constructor หลายแบบ
- ยืดหยุ่นในการใช้งาน

### Visual Feedback
- ให้ข้อมูลสถานะที่ชัดเจน
- ใช้สีที่เข้าใจง่าย (เขียว = ดี, แดง = อันตราย)
- มีขอบเพื่อความชัดเจน

### Memory Efficient
- ไม่เก็บ state ใดๆ
- สามารถ reuse instance ได้
- การคำนวณทำเฉพาะเมื่อวาด

## การปรับปรุงที่เป็นไปได้

### เพิ่มแอนิเมชัน
```java
private long lastUpdateTime = 0;
private int currentDisplayHealth;
private int targetHealth;

public void updateAnimation() {
    if (currentDisplayHealth != targetHealth) {
        // Smooth transition ระหว่างค่าเลือด
        int diff = targetHealth - currentDisplayHealth;
        currentDisplayHealth += Math.signum(diff) * Math.min(Math.abs(diff), 2);
    }
}
```

### เพิ่มเอฟเฟกต์พิเศษ
```java
public void render(Graphics graphics, int x, int y, int currentHealth, int maxHealth, 
                  boolean showDamageEffect) {
    // วาดแถบเลือดปกติ
    // ...
    
    if (showDamageEffect) {
        // วาดเอฟเฟกต์กะพริบเมื่อได้รับความเสียหาย
        graphics.setColor(new Color(255, 255, 255, 128));
        graphics.fillRect(x, y, width, height);
    }
}
```

### เพิ่มประเภทแถบเลือด
```java
public enum HealthBarType {
    NORMAL,    // เขียว-แดง
    MAGIC,     // ฟ้า-แดง  
    BOSS,      // ทอง-แดง
    SHIELD     // เงิน-เทา
}

public void render(Graphics graphics, int x, int y, int currentHealth, int maxHealth, 
                  HealthBarType type) {
    Color healthColor = getHealthColorForType(type);
    Color bgColor = getBackgroundColorForType(type);
    render(graphics, x, y, currentHealth, maxHealth, bgColor, healthColor);
}
```