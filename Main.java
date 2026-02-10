import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Tower Defense");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GamePanel panel = new GamePanel(); 
        window.add(panel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
