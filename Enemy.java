import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Enemy {

    private Image image;

    private double x, y;
    private final int size = 48;
    private final double speed = 1.2;

    private final ArrayList<Point> path = new ArrayList<>();
    private int pathIndex = 0;

    private final Map map;
    private final House house;

    private boolean attacking = false;
    private int attackCooldown = 0;

    // =============================
    // FIX หลัก
    // =============================
    private Object currentTarget = null;

    // =============================
    // กันซ้อน
    // =============================
    private static int enemyCount = 0;
    private int attackOffset;
    private boolean positionLocked = false;

    public Enemy(Map map) {
        this.map = map;
        this.house = map.getHouse();

        image = new ImageIcon("image\\catEnemy.png").getImage();

        attackOffset = enemyCount * 12;
        enemyCount++;

        buildPath();

        Point start = path.get(0);
        x = start.x;
        y = start.y;
    }

    private void buildPath() {
        int[][] grid = map.getRawMap();
        int tile = map.getTileSize();

        int roadRow = -1;
        int startCol = Integer.MAX_VALUE;

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] == 0 && c < startCol) {
                    startCol = c;
                    roadRow = r;
                }
            }
        }

        for (int col = startCol; col < grid[roadRow].length; col++) {
            if (grid[roadRow][col] == 0) {
                path.add(new Point(col * tile, roadRow * tile));
            }
        }
    }

    public void update() {

    // 1️⃣ ชน Tank
    for (Tank t : map.getTanks()) {
        if (!t.isDead() && getBounds().intersects(t.getBounds())) {

            Rectangle tb = t.getBounds();
            x = tb.x - size - attackOffset;

            currentTarget = t;
            attacking = true;
            positionLocked = true;
            return;
        }
    }

    // 2️⃣ กำลังโจมตี
    if (attacking) {
        attackTarget();
        return;
    }

    // 3️⃣ เดินตามทาง
    if (pathIndex < path.size()) {
        Point target = path.get(pathIndex);

        double dx = target.x - x;
        double dy = target.y - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > speed) {
            x += dx / dist * speed;
            y += dy / dist * speed;
        } else {
            x = target.x;
            y = target.y;
            pathIndex++;
        }
    }

    // 4️⃣ ชน House
    if (!positionLocked && getBounds().intersects(house.getBounds())) {
        Rectangle hb = house.getBounds();
        x = hb.x - size - attackOffset;

        positionLocked = true;
        attacking = true;
        currentTarget = house;
    }
}


    private void attackTarget() {
        attackCooldown++;

        if (attackCooldown >= 60) {

            if (currentTarget instanceof Tank) {
                Tank tank = (Tank) currentTarget;

                if (tank.isDead()) {
                    stopAttacking();
                    return;
                }

                tank.damage(5);

            } else if (currentTarget instanceof House) {
                house.damage(5);
            }

            attackCooldown = 0;
        }
    }

    private void stopAttacking() {
        attacking = false;
        positionLocked = false;
        currentTarget = null;
    }

    public void draw(Graphics g) {
        g.drawImage(image, (int) x, (int) y, size, size, null);
    }

    private Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }
}
