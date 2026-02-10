import java.awt.*;
import java.awt.Image;

public class House extends GameObject {

    private int hp = 100;

    public House(int col, int row, int tileSize, Image image) {
        super(col, row, tileSize, 3, 4, image);
    }

    public void damage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
        System.out.println("House HP: " + hp);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getHp() {
        return hp;
    }
}
