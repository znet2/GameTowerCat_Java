# Defensive.java - Interface สำหรับหน่วยป้องกัน

## ภาพรวม
Defensive เป็น interface ที่กำหนดพฤติกรรมพื้นฐานสำหรับหน่วยป้องกันทั้งหมด รวมถึงการรับความเสียหาย การตรวจสอบสถานะ และระบบเลือด

## ประเภท Interface
```java
public interface Defensive
```
- Interface สำหรับหน่วยป้องกันที่สามารถรับความเสียหายได้
- ไม่โจมตี แต่ทำหน้าที่เป็นอุปสรรคและดูดซับความเสียหาย

## Methods ที่ต้อง Implement

### takeDamage(int damageAmount)
```java
void takeDamage(int damageAmount)
```
**วัตถุประสงค์**: รับความเสียหายและลดเลือด
**พารามิเตอร์**: `damageAmount` - จำนวนความเสียหายที่ได้รับ
**การทำงาน**: 
- ลดเลือดปัจจุบันตามจำนวนความเสียหาย
- อาจมีการคำนวณค่าป้องกันเพิ่มเติม
- ตรวจสอบไม่ให้เลือดต่ำกว่า 0

### isDestroyed()
```java
boolean isDestroyed()
```
**วัตถุประสงค์**: ตรวจสอบว่าหน่วยถูกทำลายหรือยัง
**การทำงาน**: return true หากเลือดหมดหรือหน่วยถูกทำลาย
**การใช้งาน**: ใช้สำหรับการลบหน่วยออกจากเกมและการตรวจสอบสถานะ

### getCurrentHealth()
```java
int getCurrentHealth()
```
**วัตถุประสงค์**: ดึงเลือดปัจจุบัน
**การทำงาน**: return จำนวนเลือดที่เหลืออยู่
**การใช้งาน**: ใช้สำหรับการแสดงผล UI และการคำนวณ

### getMaxHealth()
```java
int getMaxHealth()
```
**วัตถุประสงค์**: ดึงเลือดสูงสุด
**การทำงาน**: return จำนวนเลือดสูงสุดของหน่วย
**การใช้งาน**: ใช้สำหรับการคำนวณเปอร์เซ็นต์เลือดและการแสดงผล

### getDefenseRating() (Default Method)
```java
default int getDefenseRating() {
    return 0; // Default no defense bonus
}
```
**วัตถุประสงค์**: ดึงค่าป้องกัน/เกราะ (ไม่บังคับ)
**การทำงาน**: return ค่าป้องกันที่ลดความเสียหายที่เข้ามา
**ค่าเริ่มต้น**: 0 (ไม่มีโบนัสป้องกัน)
**การใช้งาน**: ใช้สำหรับการคำนวณความเสียหายจริงที่ได้รับ

## การใช้งาน Interface

### การ Implement
```java
public class Tank extends GameObject implements Defensive {
    private int currentHealth = 100;
    private int maxHealth = 100;
    
    @Override
    public void takeDamage(int damageAmount) {
        currentHealth -= damageAmount;
        if (currentHealth < 0) currentHealth = 0;
    }
    
    @Override
    public boolean isDestroyed() {
        return currentHealth <= 0;
    }
    
    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    @Override
    public int getMaxHealth() {
        return maxHealth;
    }
    
    @Override
    public int getDefenseRating() {
        return 5; // Tank มีค่าป้องกัน 5
    }
}
```

### การใช้งานผ่าน Interface
```java
public void attackDefensiveUnit(Defensive target, int damage) {
    if (!target.isDestroyed()) {
        int actualDamage = Math.max(1, damage - target.getDefenseRating());
        target.takeDamage(actualDamage);
        
        if (target.isDestroyed()) {
            // Remove unit from game
        }
    }
}
```

## คลาสที่ Implement Interface นี้

### Tank.java
- **เลือดเริ่มต้น**: 10,000
- **ค่าป้องกัน**: 0
- **พฤติกรรมพิเศษ**: เปลี่ยนรูปภาพเมื่อถูกโจมตีครั้งแรก

### Magic.java
- **เลือดเริ่มต้น**: 50
- **ค่าป้องกัน**: 0
- **พฤติกรรมพิเศษ**: สามารถโจมตีศัตรูได้ด้วย

### Archer.java
- **เลือดเริ่มต้น**: 75
- **ค่าป้องกัน**: 0
- **พฤติกรรมพิเศษ**: ยิงลูกธนุโจมตีศัตรู

## ข้อดีของการใช้ Interface

### ความยืดหยุ่น (Flexibility)
- สามารถเพิ่มหน่วยป้องกันประเภทใหม่ได้ง่าย
- ไม่จำเป็นต้องแก้ไขโค้ดเดิม
- รองรับการขยายระบบในอนาคต

### การจัดการแบบ Polymorphism
```java
List<Defensive> defensiveUnits = new ArrayList<>();
defensiveUnits.add(new Tank(...));
defensiveUnits.add(new Magic(...));
defensiveUnits.add(new Archer(...));

// สามารถจัดการทุกหน่วยแบบเดียวกัน
for (Defensive unit : defensiveUnits) {
    if (unit.getCurrentHealth() < unit.getMaxHealth() * 0.2) {
        // หน่วยเลือดต่ำ - ต้องการการช่วยเหลือ
    }
}
```

### การแยกความรับผิดชอบ (Separation of Concerns)
- Interface กำหนดสิ่งที่หน่วยป้องกันต้องทำได้
- คลาสที่ implement จัดการรายละเอียดการทำงาน
- ระบบอื่นไม่ต้องรู้รายละเอียดการทำงานภายใน

## การออกแบบที่ดี

### Default Methods
- ใช้ default method สำหรับ `getDefenseRating()`
- ไม่บังคับให้ทุกคลาสต้อง implement
- สามารถ override ได้หากต้องการพฤติกรรมพิเศษ

### การตั้งชื่อที่ชัดเจน
- `takeDamage()` - ชัดเจนว่าทำอะไร
- `isDestroyed()` - ใช้ boolean prefix `is`
- `getCurrentHealth()` / `getMaxHealth()` - แยกแยะชัดเจน

### การจัดการ Edge Cases
- ตรวจสอบไม่ให้เลือดติดลบ
- จัดการกรณีความเสียหายเป็น 0 หรือติดลบ
- รองรับการคำนวณค่าป้องกัน

## ความสัมพันธ์กับระบบอื่น

### Enemy.java
- ใช้ Defensive interface เพื่อโจมตีหน่วยป้องกัน
- เรียก `takeDamage()` เมื่อโจมตี
- ตรวจสอบ `isDestroyed()` เพื่อหยุดโจมตี

### Map.java
- ใช้ `isDestroyed()` เพื่อลบหน่วยที่ตายแล้ว
- จัดการ cleanup ของหน่วยป้องกัน

### GamePanel.java
- ใช้สำหรับการแสดงผลแถบเลือด
- ตรวจสอบสถานะหน่วยป้องกัน

## จุดเด่นของการออกแบบ

### Consistency
- ทุกหน่วยป้องกันมีพฤติกรรมพื้นฐานเหมือนกัน
- ง่ายต่อการเข้าใจและใช้งาน

### Extensibility
- เพิ่มหน่วยใหม่ได้โดยไม่แก้ไขโค้ดเดิม
- รองรับการพัฒนาในอนาคต

### Maintainability
- แยกการทำงานออกจากกันชัดเจน
- ง่ายต่อการดูแลและแก้ไข

### Type Safety
- ใช้ interface เพื่อรับประกันว่าหน่วยมีความสามารถที่จำเป็น
- ป้องกันข้อผิดพลาดในการเรียกใช้ method