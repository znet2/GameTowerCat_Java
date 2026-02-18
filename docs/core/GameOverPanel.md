# GameOverPanel.java - หน้าจอแสดงผลลัพธ์เกม

## ภาพรวม
GameOverPanel เป็นหน้าจอที่แสดงเมื่อเกมจบ โดยจะแสดงผลลัพธ์ว่าชนะหรือแพ้ พร้อมปุ่ม Restart สำหรับเริ่มเกมใหม่

## คลาสและการสืบทอด
```java
public class GameOverPanel extends JPanel
```
- สืบทอดจาก `JPanel` สำหรับการแสดงผล
- ใช้ `MouseAdapter` สำหรับจัดการการคลิกปุ่ม Restart (แทน MouseListener เพื่อหลีกเลี่ยง empty methods)

## ตัวแปรสำคัญ

### ตัวแปรรูปภาพ
- `Image backgroundImage` - รูปพื้นหลัง (ชนะหรือแพ้)
- `Image resultImage` - รูปแสดงผลลัพธ์ (win.png หรือ lose.png)
- `Image restartButtonImage` - รูปปุ่ม Restart (restart.png)

### ตัวแปรการจัดการ
- `Rectangle restartButtonBounds` - พื้นที่ปุ่ม Restart สำหรับตรวจจับการคลิก
- `JFrame parentFrame` - อ้างอิงไปยัง JFrame หลักของเกม
- `boolean isWin` - สถานะว่าชนะหรือแพ้

## Methods อย่างละเอียด

### Constructor
```java
public GameOverPanel(JFrame frame, boolean isWin)
```
**วัตถุประสงค์**: สร้าง GameOverPanel พร้อมกำหนดสถานะชนะ/แพ้
**พารามิเตอร์**:
- `frame` - JFrame หลักของเกม
- `isWin` - true หากชนะ, false หากแพ้
**การทำงาน**:
1. เก็บอ้างอิงของ JFrame และสถานะชนะ/แพ้
2. เรียก `loadImages()` เพื่อโหลดรูปภาพที่เหมาะสม
3. เรียก `setupPanel()` เพื่อตั้งค่า panel
4. เพิ่ม MouseAdapter พร้อม override mouseClicked() เพื่อจัดการการคลิก

### loadImages()
```java
private void loadImages()
```
**วัตถุประสงค์**: โหลดรูปภาพตามสถานะชนะ/แพ้
**การทำงาน**:
1. ตรวจสอบสถานะ `isWin`
2. **หากชนะ**:
   - โหลดพื้นหลังจาก `Constants.Paths.WIN_BACKGROUND_IMAGE`
   - โหลดรูปผลลัพธ์จาก `Constants.Paths.WIN_IMAGE`
3. **หากแพ้**:
   - โหลดพื้นหลังจาก `Constants.Paths.LOSE_BACKGROUND_IMAGE`
   - โหลดรูปผลลัพธ์จาก `Constants.Paths.LOSE_IMAGE`
4. โหลดปุ่ม Restart จาก `Constants.Paths.RESTART_BUTTON_IMAGE`
5. จัดการ Exception หากโหลดรูปไม่สำเร็จ

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
**วัตถุประสงค์**: วาดองค์ประกอบทั้งหมดของหน้า Game Over
**การทำงาน**:
1. เรียก `super.paintComponent(graphics)` เพื่อล้างหน้าจอ
2. **วาดพื้นหลัง**: วาดรูปพื้นหลังให้เต็มหน้าจอ
3. **วาดรูปผลลัพธ์**:
   - คำนวณขนาดตามสถานะ (ชนะ/แพ้มีขนาดต่างกัน)
   - คำนวณ Y offset ตามสถานะ
   - คำนวณตำแหน่งให้อยู่กึ่งกลางหน้าจอ
   - วาดรูปผลลัพธ์
4. **วาดปุ่ม Restart**:
   - คำนวณตำแหน่งให้อยู่ใต้รูปผลลัพธ์
   - อัปเดต `restartButtonBounds` สำหรับการตรวจจับคลิก
   - วาดรูป `restart.png`

### handleMouseClick(MouseEvent e)
```java
private void handleMouseClick(MouseEvent e)
```
**วัตถุประสงค์**: จัดการเหตุการณ์การคลิกเมาส์
**การทำงาน**:
1. ตรวจสอบว่าตำแหน่งที่คลิกอยู่ในพื้นที่ปุ่ม Restart หรือไม่
2. หากใช่ เรียก `restartGame()` เพื่อเริ่มเกมใหม่
**หมายเหตุ**: ถูกเรียกจาก MouseAdapter.mouseClicked()

### restartGame()
```java
private void restartGame()
```
**วัตถุประสงค์**: เริ่มเกมใหม่โดยกลับไปหน้าเมนู
**การทำงาน**:
1. ลบ GameOverPanel ออกจาก JFrame
2. สร้าง MenuPanel ใหม่
3. เพิ่ม MenuPanel เข้าไปใน JFrame
4. เรียก `pack()` เพื่อปรับขนาดหน้าต่าง
5. จัดตำแหน่งหน้าต่างให้อยู่กึ่งกลางหน้าจอ
6. เรียก `revalidate()` และ `repaint()` เพื่ออัปเดตการแสดงผล

## การทำงานของระบบ

### การแสดงผลตามสถานะ
1. **เมื่อชนะ (isWin = true)**:
   - ใช้พื้นหลัง `winBackground.png`
   - แสดงรูป `win.png` ขนาด 600x400 พิกเซล
   - Y offset = -50 (ขยับขึ้นเล็กน้อย)

2. **เมื่อแพ้ (isWin = false)**:
   - ใช้พื้นหลัง `loseBackground.png`
   - แสดงรูป `lose.png` ขนาด 600x700 พิกเซล
   - Y offset = -50 (ขยับขึ้นเล็กน้อย)

### การจัดตำแหน่ง
- รูปผลลัพธ์อยู่กึ่งกลางหน้าจอ (แนวนอนและแนวตั้ง)
- ปุ่ม Restart อยู่ใต้รูปผลลัพธ์ 50 พิกเซล
- ปุ่ม Restart ขนาด 200x60 พิกเซล ใช้รูปภาพ `restart.png`

## การใช้งาน
GameOverPanel จะถูกสร้างขึ้นโดย GamePanel เมื่อ:
1. บ้านถูกทำลาย (แพ้)
2. ผ่าน wave ทั้งหมดแล้ว (ชนะ)

## ความสัมพันธ์กับคลาสอื่น
- **GamePanel.java**: สร้าง GameOverPanel เมื่อเกมจบ
- **MenuPanel.java**: GameOverPanel จะสร้าง MenuPanel เมื่อ restart
- **Constants.java**: ใช้ค่าคงที่สำหรับขนาดและ path ของรูปภาพ

## จุดเด่นของการออกแบบ
1. **ยืดหยุ่น**: รองรับทั้งสถานะชนะและแพ้ในคลาสเดียว
2. **การแสดงผลที่แตกต่าง**: ใช้รูปภาพและขนาดที่เหมาะสมกับแต่ละสถานะ
3. **ง่ายต่อการใช้งาน**: ปุ่ม Restart ที่ชัดเจนและใช้งานง่าย (ใช้รูปภาพ restart.png)
4. **การจัดการหน่วยความจำ**: โหลดเฉพาะรูปภาพที่จำเป็นตามสถานะ
5. **Clean Code**: ลบโค้ด fallback ที่ไม่จำเป็นออก ทำให้โค้ดกระชับและอ่านง่าย