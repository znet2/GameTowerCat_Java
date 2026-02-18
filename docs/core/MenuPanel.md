# MenuPanel.java - หน้าเมนูหลักของเกม

## ภาพรวม
MenuPanel เป็นหน้าจอแรกที่ผู้เล่นจะเห็นเมื่อเปิดเกม แสดงภาพพื้นหลัง โลโก้ และปุ่ม Start สำหรับเริ่มเกม

## คลาสและการสืบทอด
```java
public class MenuPanel extends JPanel
```
- สืบทอดจาก `JPanel` สำหรับการแสดงผล
- ใช้ `MouseAdapter` สำหรับจัดการการคลิกเมาส์ (แทน MouseListener เพื่อหลีกเลี่ยง empty methods)

## ตัวแปรสำคัญ

### ตัวแปรรูปภาพ
- `Image wallpaperImage` - รูปพื้นหลังของเมนู
- `Image startButtonImage` - รูปปุ่ม Start
- `Image logoImage` - รูปโลโก้เกม

### ตัวแปรตำแหน่งและการจัดการ
- `Rectangle startButtonBounds` - พื้นที่ของปุ่ม Start สำหรับตรวจจับการคลิก
- `JFrame parentFrame` - อ้างอิงไปยัง JFrame หลักของเกม

## Methods อย่างละเอียด

### Constructor
```java
public MenuPanel(JFrame frame)
```
**วัตถุประสงค์**: สร้าง MenuPanel และตั้งค่าเริ่มต้น
**การทำงาน**:
1. เก็บอ้างอิงของ JFrame หลัก
2. เรียก `loadImages()` เพื่อโหลดรูปภาพ
3. เรียก `setupPanel()` เพื่อตั้งค่า panel
4. เพิ่ม MouseAdapter พร้อม override mouseClicked() เพื่อจัดการการคลิก

### loadImages()
```java
private void loadImages()
```
**วัตถุประสงค์**: โหลดรูปภาพทั้งหมดที่ใช้ในเมนู
**การทำงาน**:
1. โหลดรูปพื้นหลังจาก `Constants.Paths.WALLPAPER_IMAGE`
2. โหลดรูปปุ่ม Start จาก `Constants.Paths.START_BUTTON_IMAGE`
3. โหลดรูปโลโก้จาก `Constants.Paths.LOGO_GAME_IMAGE`
4. จัดการ Exception หากโหลดรูปไม่สำเร็จ

### setupPanel()
```java
private void setupPanel()
```
**วัตถุประสงค์**: ตั้งค่าขนาดของ panel
**การทำงาน**:
- กำหนดขนาด panel ให้เท่ากับขนาดหน้าต่างเกม (1920x1080)

### paintComponent(Graphics graphics)
```java
@Override
protected void paintComponent(Graphics graphics)
```
**วัตถุประสงค์**: วาดองค์ประกอบทั้งหมดของเมนูบนหน้าจอ
**การทำงาน**:
1. เรียก `super.paintComponent(graphics)` เพื่อล้างหน้าจอ
2. วาดรูปพื้นหลังให้เต็มหน้าจอ
3. คำนวณตำแหน่งโลโก้ให้อยู่กึ่งกลางด้านบน
4. วาดโลโก้ด้วยขนาดที่กำหนดใน Constants
5. คำนวณตำแหน่งปุ่ม Start ให้อยู่กึ่งกลางหน้าจอ
6. อัปเดต `startButtonBounds` สำหรับการตรวจจับคลิก
7. วาดปุ่ม Start ด้วยขนาดที่กำหนด

### handleMouseClick(MouseEvent e)
```java
private void handleMouseClick(MouseEvent e)
```
**วัตถุประสงค์**: จัดการเหตุการณ์การคลิกเมาส์
**การทำงาน**:
1. ตรวจสอบว่าตำแหน่งที่คลิกอยู่ในพื้นที่ปุ่ม Start หรือไม่
2. หากใช่ เรียก `startGame()` เพื่อเริ่มเกม
**หมายเหตุ**: ถูกเรียกจาก MouseAdapter.mouseClicked()

### startGame()
```java
private void startGame()
```
**วัตถุประสงค์**: เปลี่ยนจากหน้าเมนูไปยังหน้าเกม
**การทำงาน**:
1. ลบ MenuPanel ออกจาก JFrame
2. สร้าง GamePanel ใหม่
3. เพิ่ม GamePanel เข้าไปใน JFrame
4. เรียก `pack()` เพื่อปรับขนาดหน้าต่าง
5. จัดตำแหน่งหน้าต่างให้อยู่กึ่งกลางหน้าจอ
6. เรียก `revalidate()` เพื่ออัปเดตการแสดงผล
7. ให้ GamePanel ได้รับ focus สำหรับการรับ input

## การใช้งาน
MenuPanel จะถูกสร้างขึ้นเมื่อเกมเริ่มต้นหรือเมื่อผู้เล่นกดปุ่ม Restart จากหน้า GameOver โดยจะแสดงหน้าเมนูที่สวยงามพร้อมปุ่มสำหรับเริ่มเกมใหม่

## ความสัมพันธ์กับคลาสอื่น
- **Main.java**: สร้าง MenuPanel เป็นหน้าจอแรก
- **GamePanel.java**: MenuPanel จะสร้าง GamePanel เมื่อเริ่มเกม
- **GameOverPanel.java**: GameOverPanel จะสร้าง MenuPanel เมื่อ restart
- **Constants.java**: ใช้ค่าคงที่สำหรับขนาดและ path ของรูปภาพ