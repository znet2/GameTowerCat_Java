# ImageLoader

## Package
`com.game.utils`

## Description
Utility class สำหรับโหลดรูปภาพจาก resources ที่ทำงานได้ทั้งเมื่อรันจาก IDE และจากไฟล์ JAR

## Purpose
- โหลดรูปภาพจาก classpath (ทำงานใน JAR file)
- Fallback ไปโหลดจาก file system (สำหรับการพัฒนา)
- จัดการ error handling สำหรับการโหลดรูปภาพ

## Class Structure

```java
public final class ImageLoader {
    // Prevent instantiation
    private ImageLoader() {}
    
    public static BufferedImage loadImage(String path)
}
```

## Methods

### loadImage(String path)
โหลดรูปภาพจาก path ที่กำหนด

**Parameters:**
- `path` - path ไปยังไฟล์รูปภาพ (เช่น "image/tank.png")

**Returns:**
- `BufferedImage` - รูปภาพที่โหลดได้
- `null` - ถ้าโหลดไม่สำเร็จ

**Algorithm:**
1. พยายามโหลดจาก classpath ก่อน (ใช้ `getResourceAsStream`)
2. ถ้าไม่สำเร็จ ลอง fallback ไปโหลดจาก file system
3. ถ้ายังไม่สำเร็จ แสดง error message และ return null

## Usage Example

```java
// โหลดรูปภาพ tank
BufferedImage tankImage = ImageLoader.loadImage(Constants.Paths.TANK_IMAGE);

// โหลดรูปภาพพร้อม null check
BufferedImage image = ImageLoader.loadImage("image/example.png");
if (image != null) {
    // ใช้รูปภาพ
} else {
    // จัดการกรณีโหลดไม่สำเร็จ
}
```

## Used By
- `MenuPanel` - โหลดรูปภาพ menu
- `GameOverPanel` - โหลดรูปภาพ game over
- `Map` - โหลด tile images และ unit images
- `Tank` - โหลดรูปภาพ defend
- `Magic` - โหลดรูปภาพ bomb และ magic balls
- `Archer` - โหลดรูปภาพ arrow และ attack
- `Assassin` - โหลดรูปภาพ attack
- `Enemy` - โหลดรูปภาพ idle, walk, attack
- `EnemyBoss` - โหลดรูปภาพ idle, fly, attack, skill

## Technical Details

### Why This Approach?
เมื่อรันโปรแกรมจาก JAR file ไม่สามารถใช้ `new File()` เพื่อโหลดไฟล์ที่อยู่ภายใน JAR ได้ ต้องใช้ `getResourceAsStream()` แทน

### Classpath Loading
```java
InputStream stream = ImageLoader.class.getClassLoader().getResourceAsStream(path);
BufferedImage image = ImageIO.read(stream);
```

### File System Fallback
```java
java.io.File file = new java.io.File(path);
if (file.exists()) {
    return ImageIO.read(file);
}
```

## Error Handling
- แสดง error message ไปที่ `System.err` เมื่อโหลดไม่สำเร็จ
- Print stack trace สำหรับ IOException
- Return null แทนที่จะ throw exception

## Design Pattern
**Utility Class Pattern:**
- Constructor เป็น private เพื่อป้องกันการสร้าง instance
- ทุก method เป็น static
- Class เป็น final เพื่อป้องกันการ inherit

## Benefits
1. **JAR Compatibility** - ทำงานได้ทั้งใน IDE และ JAR file
2. **Centralized Loading** - จัดการการโหลดรูปภาพในที่เดียว
3. **Error Handling** - จัดการ error แบบ consistent
4. **Easy to Use** - API ที่เรียบง่าย เพียง 1 method
5. **Fallback Support** - รองรับทั้ง development และ production

## Related Classes
- `Constants.Paths` - เก็บ path ของรูปภาพทั้งหมด
- `ImageIO` - Java standard library สำหรับอ่านรูปภาพ

## Notes
- ควรใช้ path แบบ relative (เช่น "image/tank.png") ไม่ใช่ absolute path
- รูปภาพทั้งหมดต้องอยู่ในโฟลเดอร์ `image/` ที่ root ของ classpath
- เมื่อสร้าง JAR ต้อง copy โฟลเดอร์ `image/` เข้าไปด้วย

## Version History
- **v1.0** - Initial implementation สำหรับรองรับ JAR file deployment
