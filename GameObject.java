import java.awt.*;

public abstract class GameObject {

    protected int x, y;
    protected int width, height;
    protected Image image;

    public GameObject(int col, int row, int tileSize,
                      int widthTiles, int heightTiles,
                      Image image) {

        this.x = col * tileSize;
        this.y = row * tileSize;
        this.width = widthTiles * tileSize;
        this.height = heightTiles * tileSize;
        this.image = image;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
