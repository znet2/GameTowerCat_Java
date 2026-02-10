import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Map extends JPanel {

    private int tileSize = 32;
    private ArrayList<GameObject> objects = new ArrayList<>();
    private Image houseImg;

    private ArrayList<Tank> tanks = new ArrayList<>();
    private Image tankImg;

    int[][] rawMap = {
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 4, 4 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 4, 1, 1 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 4, 1, 1, 1 },
            { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                    4, 1, 1, 1, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 0, 0 },
            { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                    3, 1, 1, 1, 0 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 3, 1, 1, 1 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 3, 1, 1 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 3, 3 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 }
    };

    private Image grass, road, water, water_up, water_down, water_left, water_right, tree;

    public Map() {

        try {
            grass = ImageIO.read(getClass().getResourceAsStream("image\\grass.png"));
            road = ImageIO.read(getClass().getResourceAsStream("image\\dirt.png"));
            water = ImageIO.read(getClass().getResourceAsStream("image\\water.png"));
            water_up = ImageIO.read(getClass().getResourceAsStream("image\\water_up.png"));
            water_down = ImageIO.read(getClass().getResourceAsStream("image\\water_down.png"));
            water_left = ImageIO.read(getClass().getResourceAsStream("image\\water_left.png"));
            water_right = ImageIO.read(getClass().getResourceAsStream("image\\water_right.png"));
            tree = ImageIO.read(getClass().getResourceAsStream("image\\water.png"));
            houseImg = ImageIO.read(getClass().getResource("image\\house.png"));
            tankImg = ImageIO.read(getClass().getResource("image\\tank.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        objects.add(new House(37, 6, tileSize, houseImg));

        setPreferredSize(new Dimension(
                rawMap[0].length * tileSize,
                rawMap.length * tileSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1️⃣ วาดพื้น
        for (int row = 0; row < rawMap.length; row++) {
            for (int col = 0; col < rawMap[row].length; col++) {
                int tileType = rawMap[row][col];
                Image tileImage;

                switch (tileType) {
                    case 0:
                        tileImage = road;
                        break;
                    case 1:
                        tileImage = grass;
                        break;
                    case 2:
                        tileImage = water;
                        break;
                    case 3:
                        tileImage = water_up;
                        break;
                    case 4:
                        tileImage = water_down;
                        break;
                    case 5:
                        tileImage = water_left;
                        break;
                    case 6:
                        tileImage = water_right;
                        break;
                    case 7:
                        tileImage = tree;
                        break;
                    default:
                        tileImage = grass;
                }

                g.drawImage(tileImage,
                        col * tileSize,
                        row * tileSize,
                        tileSize,
                        tileSize,
                        this);
            }
        }

        // 2️⃣ วาดสิ่งปลูกสร้าง
        for (GameObject obj : objects) {
            obj.draw(g);
        }

        // 3️⃣ วาด Tank (บนสุด)
        for (Tank t : tanks) {
            t.draw(g);
        }
    }

    public int[][] getRawMap() {
        return rawMap;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMapWidth() {
        return rawMap[0].length * tileSize;
    }

    public int getMapHeight() {
        return rawMap.length * tileSize;
    }

    public House getHouse() {
        return (House) objects.get(0);
    }

    public void draw(Graphics g) {
        paintComponent(g);
    }

    public int getTileAtPixel(double px, double py) {
        int col = (int) (px / tileSize);
        int row = (int) (py / tileSize);

        if (row < 0 || col < 0 ||
                row >= rawMap.length ||
                col >= rawMap[0].length) {
            return -1;
        }
        return rawMap[row][col];
    }

    public void placeTank(int col, int row) {
        tanks.add(new Tank(col, row, tileSize, tankImg));
        repaint();
    }

    public ArrayList<Tank> getTanks() {
        return tanks;
    }

    public void removeDeadTanks() {
        tanks.removeIf(Tank::isDead);
    }

    public boolean hasTankAt(int col, int row) {
        for (Tank t : tanks) {
            if (t.getCol(tileSize) == col &&
                    t.getRow(tileSize) == row) {
                return true;
            }
        }
        return false;
    }
}