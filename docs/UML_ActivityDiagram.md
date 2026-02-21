# UML Activity Diagrams - Tower Defense Game

## 1. Main Game Flow Activity Diagram

```plantuml
@startuml MainGameFlow
start

:เริ่มโปรแกรม;
:แสดงหน้าเมนู;

:ผู้เล่นคลิก Start;

:สร้าง GamePanel;
:โหลดแผนที่;
:สร้างบ้าน;
:ให้เหรียญเริ่มต้น 550;

:เริ่ม Wave 1;

repeat
    :ประกาศ Wave;
    :คำนวณจำนวนศัตรู;
    
    fork
        :Spawn ศัตรู;
        repeat
            :รอ 0.5 วินาที;
            if (ควร spawn Boss?) then (yes)
                :สร้าง Boss;
            else (no)
                :สร้างศัตรูปกติ;
            endif
        repeat while (ยังมีศัตรูที่ต้อง spawn?)
    fork again
        :ผู้เล่นวางหน่วยป้องกัน;
    fork again
        :อัปเดตเกม;
        :เคลื่อนที่ศัตรู;
        :หน่วยป้องกันโจมตี;
        :ศัตรูโจมตี;
    end fork
    
    :รอให้ศัตรูทั้งหมดตาย;
    
    if (บ้านถูกทำลาย?) then (yes)
        :แสดงหน้าจอแพ้;
        stop
    endif
    
    :ให้รางวัลเหรียญ +200;
    
    if (ผ่าน Wave 5?) then (yes)
        :แสดงหน้าจอชนะ;
        stop
    endif
    
repeat while (เกมยังไม่จบ?)

stop
@enduml
```

## 2. Place Defensive Unit Activity Diagram

```plantuml
@startuml PlaceDefensiveUnit
start

:ผู้เล่นเลือกประเภทหน่วย;
note right
    - Tank (120 เหรียญ)
    - Magic (90 เหรียญ)
    - Archer (180 เหรียญ)
    - Assassin (150 เหรียญ)
end note

:แสดง preview หน่วย;

:ผู้เล่นคลิกตำแหน่ง;

:แปลงพิกเซลเป็นกริด;

if (ตำแหน่งเป็นไทล์หญ้า?) then (no)
    :แสดงข้อความ "ไม่สามารถวางได้";
    stop
endif

if (ตำแหน่งว่าง?) then (no)
    :แสดงข้อความ "มีหน่วยอยู่แล้ว";
    stop
endif

:ดึงราคาหน่วย;

if (เหรียญเพียงพอ?) then (no)
    :แสดงข้อความ "เหรียญไม่พอ";
    stop
endif

:หักเหรียญ;

if (ประเภทหน่วย?) then (Tank)
    :สร้าง Tank;
    :เพิ่มเข้า defensiveTanks;
elseif (Magic) then
    :สร้าง Magic Tower;
    :เพิ่มเข้า magicTowers;
elseif (Archer) then
    :สร้าง Archer Tower;
    :เพิ่มเข้า archerTowers;
else (Assassin)
    :สร้าง Assassin;
    :เพิ่มเข้า assassins;
endif

:วาดหน่วยบนแผนที่;
:อัปเดตการแสดงผล;

stop
@enduml
```

## 3. Enemy Movement and Pathfinding Activity Diagram

```plantuml
@startuml EnemyMovement
start

:สร้างศัตรู;

:หาจุดเริ่มต้น (ถนนซ้ายสุด);
:หาจุดหมาย (ใกล้บ้าน);

:ใช้ BFS สร้างเส้นทาง;

if (หาเส้นทางสำเร็จ?) then (no)
    :สร้างเส้นทางสำรอง;
endif

:ตั้งตำแหน่งเริ่มต้น;

repeat
    :อัปเดตแอนิเมชัน;
    
    if (กำลังโจมตีอยู่?) then (yes)
        :เพิ่ม attackTimer;
        
        if (ถึงเวลาโจมตี?) then (yes)
            :โจมตีเป้าหมาย;
            :รีเซ็ต attackTimer;
            
            if (เป้าหมายตาย?) then (yes)
                :หยุดโจมตี;
            endif
        endif
    else (no)
        :ตรวจสอบการชนกับหน่วยป้องกัน;
        
        if (ชนกับ Tank?) then (yes)
            :เริ่มโจมตี Tank;
        elseif (ชนกับ Magic?) then (yes)
            :เริ่มโจมตี Magic;
        elseif (ชนกับ Archer?) then (yes)
            :เริ่มโจมตี Archer;
        elseif (ชนกับบ้าน?) then (yes)
            :เริ่มโจมตีบ้าน;
        else (no)
            :ตรวจสอบว่าทางข้างหน้าถูกบล็อก;
            
            if (มีศัตรูตัวอื่นขวางทาง?) then (yes)
                :รอ (ไม่เคลื่อนที่);
            else (no)
                :เคลื่อนที่ไปจุดถัดไป;
                
                if (ถึงจุดถัดไป?) then (yes)
                    :เพิ่ม pathIndex;
                endif
            endif
        endif
    endif
    
    if (HP <= 0?) then (yes)
        :ตั้งสถานะเป็นตาย;
        :ให้รางวัลเหรียญ;
        stop
    endif
    
    if (ถึงจุดสิ้นสุดเส้นทาง?) then (yes)
        :ตั้งสถานะเป็นตาย;
        stop
    endif
    
repeat while (ยังมีชีวิต?)

stop
@enduml
```

## 4. Magic Tower Attack Activity Diagram

```plantuml
@startuml MagicTowerAttack
start

:สร้าง Magic Tower;
:attackCounter = 0;

repeat
    :อัปเดต magic balls ทั้งหมด;
    
    fork
        repeat :ball in magicBalls
            :เคลื่อนที่ไปหาเป้าหมาย;
            
            if (ถึงเป้าหมาย?) then (yes)
                :ทำความเสียหาย;
                :ลบ ball;
            elseif (เป้าหมายตาย?) then (yes)
                :ลบ ball;
            endif
        repeat while (มี ball เหลือ?)
    end fork
    
    :อัปเดตแอนิเมชัน;
    
    if (มีเป้าหมายอยู่?) then (no)
        :หาเป้าหมายใหม่;
        
        repeat :enemy in enemies
            :คำนวณระยะห่าง;
            
            if (อยู่ในระยะโจมตี 250?) then (yes)
                if (ใกล้ที่สุด?) then (yes)
                    :ล็อกเป้าหมาย;
                endif
            endif
        repeat while (ยังมีศัตรู?)
    endif
    
    if (มีเป้าหมาย?) then (yes)
        :เพิ่ม attackTimer;
        
        if (attackTimer >= 60?) then (yes)
            :รีเซ็ต attackTimer;
            
            if (attackCounter >= 4?) then (yes)
                :เปลี่ยนเป็นรูป bomb;
                :ยิง Super Magic Ball;
                note right
                    ความเสียหาย: 400
                end note
                :attackCounter = 0;
            else (no)
                :ยิง Normal Magic Ball;
                note right
                    ความเสียหาย: 250
                end note
                :attackCounter++;
            endif
        endif
    endif
    
    if (HP <= 0?) then (yes)
        :ตั้งสถานะเป็นตาย;
        stop
    endif
    
repeat while (ยังมีชีวิต?)

stop
@enduml
```

## 5. Boss Special Skill Activity Diagram

```plantuml
@startuml BossSkill
start

:สร้าง Boss;
:skillCooldownTimer = 600;

repeat
    :อัปเดต skill balls;
    
    fork
        repeat :ball in skillBalls
            :เคลื่อนที่ตามทิศทาง;
            
            if (ออกนอกหน้าจอ?) then (yes)
                :ลบ ball;
            else (no)
                :ตรวจสอบการชนกับหน่วยป้องกัน;
                
                if (ชนกับ Tank?) then (yes)
                    :ทำความเสียหาย 750;
                    :ลบ ball;
                elseif (ชนกับ Magic?) then (yes)
                    :ทำความเสียหาย 750;
                    :ลบ ball;
                elseif (ชนกับ Archer?) then (yes)
                    :ทำความเสียหาย 750;
                    :ลบ ball;
                elseif (ชนกับบ้าน?) then (yes)
                    :ทำความเสียหาย 750;
                    :ลบ ball;
                endif
            endif
        repeat while (มี ball เหลือ?)
    end fork
    
    if (กำลังใช้สกิล?) then (yes)
        :เพิ่ม skillAnimationTimer;
        
        if (skillAnimationTimer >= 60?) then (yes)
            :หยุดแอนิเมชันสกิล;
            :เปลี่ยนกลับเป็นรูปปกติ;
        endif
    else (no)
        :ลด skillCooldownTimer;
        
        if (skillCooldownTimer <= 0?) then (yes)
            :เริ่มใช้สกิล;
            :เปลี่ยนเป็นรูป skill;
            
            :รับรายการหน่วยป้องกันทั้งหมด;
            
            :คำนวณตำแหน่งกลาง Boss;
            
            repeat :i = 0 to 15
                :คำนวณมุม = i × (360/16);
                :สร้าง BossSkillBall;
                note right
                    - ตำแหน่ง: ตรงกลาง Boss
                    - ทิศทาง: ตามมุม
                    - ความเร็ว: 3.0
                    - ความเสียหาย: 750
                end note
                :เพิ่มเข้า skillBalls;
            repeat while (i < 16?)
            
            :skillCooldownTimer = 600;
        endif
    endif
    
    :เรียก super.update();
    note right
        อัปเดตการเคลื่อนที่
        และการโจมตีปกติ
    end note
    
    if (HP <= 0?) then (yes)
        :ตั้งสถานะเป็นตาย;
        :ให้รางวัลเหรียญ 30;
        stop
    endif
    
repeat while (ยังมีชีวิต?)

stop
@enduml
```

## 6. Wave Management Activity Diagram

```plantuml
@startuml WaveManagement
start

:currentWaveNumber = 1;

repeat
    :ประกาศ Wave;
    note right
        แสดงข้อความ "Wave X"
        เป็นเวลา 3 วินาที
    end note
    
    :คำนวณจำนวนศัตรู;
    note right
        ศัตรูปกติ = 3 + (wave × 3)
        Boss = (wave - 3) / 2 + 1
        (ถ้า wave >= 3)
    end note
    
    if (wave >= 3?) then (yes)
        :คำนวณจำนวน Boss;
        note right
            Wave 3: 1 Boss
            Wave 4: 0 Boss
            Wave 5: 2 Boss
            Wave 6: 0 Boss
            Wave 7: 3 Boss
        end note
    else (no)
        :Boss = 0;
    endif
    
    :รวมจำนวนศัตรูทั้งหมด;
    :enemiesSpawnedInWave = 0;
    :isCurrentlySpawning = true;
    
    repeat
        :รอ 0.5 วินาที (30 frames);
        
        :คำนวณจำนวนศัตรูปกติ;
        note right
            normalCount = total - bossCount
        end note
        
        if (enemiesSpawned >= normalCount?) then (yes)
            :สร้าง Boss;
            :เพิ่มเข้า activeEnemies;
        else (no)
            :สร้างศัตรูปกติ;
            :เพิ่มเข้า activeEnemies;
        endif
        
        :enemiesSpawnedInWave++;
        
    repeat while (enemiesSpawned < total?)
    
    :isCurrentlySpawning = false;
    :currentWaveNumber++;
    
    :รอให้ศัตรูทั้งหมดตาย;
    
    if (บ้านถูกทำลาย?) then (yes)
        :จบเกม (แพ้);
        stop
    endif
    
    :ให้รางวัลเหรียญ +200;
    
    if (currentWave > 5?) then (yes)
        :จบเกม (ชนะ);
        stop
    endif
    
repeat while (เกมยังไม่จบ?)

stop
@enduml
```

## 7. Coin Management Activity Diagram

```plantuml
@startuml CoinManagement
start

:เริ่มเกม;
:currentCoins = 550;

partition "รับเหรียญ" {
    fork
        :ศัตรูปกติตาย;
        :+15 เหรียญ;
    fork again
        :Boss ตาย;
        :+30 เหรียญ;
    fork again
        :ผ่าน Wave;
        :+200 เหรียญ;
    end fork
    
    :อัปเดตจำนวนเหรียญ;
}

partition "ใช้เหรียญ" {
    :ผู้เล่นต้องการวางหน่วย;
    
    if (ประเภทหน่วย?) then (Tank)
        :ราคา = 120;
    elseif (Magic) then
        :ราคา = 90;
    elseif (Archer) then
        :ราคา = 180;
    else (Assassin)
        :ราคา = 150;
    endif
    
    if (currentCoins >= ราคา?) then (yes)
        :currentCoins -= ราคา;
        :วางหน่วย;
    else (no)
        :แสดงข้อความ "เหรียญไม่พอ";
    endif
}

partition "แสดงผล" {
    :วาดข้อความ "Coins: X";
    note right
        ตำแหน่ง: มุมล่างขวา
        สี: เหลือง
        พื้นหลัง: ดำโปร่งแสง
    end note
}

if (เกมจบ?) then (yes)
    stop
else (no)
    :วนกลับไปรับ/ใช้เหรียญ;
endif

@enduml
```

## 8. Game Over Check Activity Diagram

```plantuml
@startuml GameOverCheck
start

repeat
    :อัปเดตเกม;
    
    fork
        :อัปเดตศัตรู;
    fork again
        :อัปเดตหน่วยป้องกัน;
    fork again
        :อัปเดต Wave;
    end fork
    
    partition "ตรวจสอบเงื่อนไขแพ้" {
        :ดึง HP บ้าน;
        
        if (HP บ้าน <= 0?) then (yes)
            :isGameRunning = false;
            :สร้าง GameOverPanel(false);
            :แสดงหน้าจอแพ้;
            note right
                - รูปพื้นหลัง: loseBackground
                - รูปผลลัพธ์: lose
                - ปุ่ม: restart
            end note
            stop
        endif
    }
    
    partition "ตรวจสอบเงื่อนไขชนะ" {
        :ดึง Wave ปัจจุบัน;
        
        if (currentWave > 5?) then (yes)
            :ตรวจสอบว่าศัตรูหมดแล้ว;
            
            if (activeEnemies.isEmpty()?) then (yes)
                :isGameRunning = false;
                :สร้าง GameOverPanel(true);
                :แสดงหน้าจอชนะ;
                note right
                    - รูปพื้นหลัง: winBackground
                    - รูปผลลัพธ์: win
                    - ปุ่ม: restart
                end note
                stop
            endif
        endif
    }
    
    partition "ดำเนินเกมต่อ" {
        if (Wave จบ?) then (yes)
            :ให้รางวัลเหรียญ +200;
            :เริ่ม Wave ถัดไป;
        endif
        
        :ลบหน่วยที่ตายออก;
        :ลบศัตรูที่ตายออก;
    }
    
repeat while (isGameRunning?)

stop
@enduml
```

## คำอธิบาย Activity Diagrams

### 1. Main Game Flow
แสดงขั้นตอนหลักของเกมตั้งแต่เริ่มจนจบ รวมถึง loop หลักของการเล่น

### 2. Place Defensive Unit
แสดงขั้นตอนการวางหน่วยป้องกัน รวมถึงการตรวจสอบเงื่อนไขต่างๆ

### 3. Enemy Movement and Pathfinding
แสดงการเคลื่อนที่ของศัตรู การหาเส้นทาง และการโจมตี

### 4. Magic Tower Attack
แสดงระบบการโจมตีของ Magic Tower รวมถึงการนับจำนวนครั้งเพื่อใช้ spell

### 5. Boss Special Skill
แสดงการทำงานของสกิลพิเศษของ Boss ที่ยิงลูกไฟรอบตัว

### 6. Wave Management
แสดงการจัดการ Wave รวมถึงการคำนวณจำนวนศัตรูและ Boss

### 7. Coin Management
แสดงระบบการรับและใช้เหรียญในเกม

### 8. Game Over Check
แสดงการตรวจสอบเงื่อนไขชนะ/แพ้ในทุก frame
