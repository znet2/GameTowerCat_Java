import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel
        implements Runnable, MouseListener, MouseMotionListener {

    // =============================
    // Game loop
    // =============================
    Thread gameThread;
    final int FPS = 60;

    // =============================
    // Game objects
    // =============================
    Map map;
    ArrayList<Enemy> enemies = new ArrayList<>();
    WaveManager waveManager;

    // =============================
    // Hero Bar (UI)
    // =============================
    Rectangle heroBar;
    Rectangle tankIcon;

    boolean draggingTank = false;
    int dragX, dragY;

    public GamePanel() {
        map = new Map();

        waveManager = new WaveManager(map, enemies);
        waveManager.startNextWave();

        // hero bar อยู่ล่างจอ
        heroBar = new Rectangle(
                0,
                map.getMapHeight(),
                map.getMapWidth(),
                80);

        tankIcon = new Rectangle(
                20,
                map.getMapHeight() + 16,
                48,
                48);

        setPreferredSize(new Dimension(
                map.getMapWidth(),
                map.getMapHeight() + 80));

        setBackground(Color.BLACK);
        setFocusable(true);

        addMouseListener(this);
        addMouseMotionListener(this);

        startGameThread();
    }

    void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // =============================
    // Game loop
    // =============================
    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (true) {
            long current = System.nanoTime();
            delta += (current - lastTime) / drawInterval;
            lastTime = current;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    // =============================
    // Update
    // =============================
    void update() {

        waveManager.update();

        for (int i = enemies.size() - 1; i >= 0; i--) {
            enemies.get(i).update();
        }

        // ⭐ ลบ Tank ที่ตาย ตรงนี้เลย
        map.removeDeadTanks();

        if (waveManager.isWaveFinished()) {
            waveManager.startNextWave();
        }
    }

    // =============================
    // Draw
    // =============================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // map + house + tank
        map.paintComponent(g);

        // enemies
        for (Enemy e : enemies) {
            e.draw(g);
        }

        // hero bar
        drawHeroBar(g);
    }

    private void drawHeroBar(Graphics g) {
        // bar background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(heroBar.x, heroBar.y, heroBar.width, heroBar.height);

        // tank icon
        g.setColor(Color.ORANGE);
        g.fillRect(tankIcon.x, tankIcon.y, tankIcon.width, tankIcon.height);

        g.setColor(Color.BLACK);
        g.drawRect(tankIcon.x, tankIcon.y, tankIcon.width, tankIcon.height);
        g.drawString("Tank", tankIcon.x + 8, tankIcon.y + 30);

        // dragging preview
        if (draggingTank) {
            g.setColor(new Color(255, 165, 0, 150));
            g.fillRect(dragX - 24, dragY - 24, 48, 48);
        }
    }

    // =============================
    // Mouse control
    // =============================
    @Override
    public void mousePressed(MouseEvent e) {
        if (tankIcon.contains(e.getPoint())) {
            draggingTank = true;
            dragX = e.getX();
            dragY = e.getY();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggingTank) {
            dragX = e.getX();
            dragY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (draggingTank) {

            int col = e.getX() / map.getTileSize();
            int row = e.getY() / map.getTileSize();

            // อยู่ในขอบเขต map
            if (row >= 0 && col >= 0 &&
                    row < map.getRawMap().length &&
                    col < map.getRawMap()[0].length) {

                int tile = map.getRawMap()[row][col];

                // วางได้เฉพาะ road (0) และไม่มี Tank อยู่
                if (tile == 0 && !map.hasTankAt(col, row)) {
                    map.placeTank(col, row);
                }
            }

            draggingTank = false;
        }
    }

    // =============================
    // Unused mouse events
    // =============================
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
}
