import java.awt.*;

public class Tank extends GameObject {

    private int hp = 200;

    public Tank(int col, int row, int tileSize, Image image) {
        super(col, row, tileSize, 1, 1, image);
    }

    public void damage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
        System.out.println("Tank HP: " + hp);
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getCol(int tileSize) {
        return x / tileSize;
    }

    public int getRow(int tileSize) {
        return y / tileSize;
    }
}
