package com.towerdefense.core;

import com.towerdefense.utils.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * Game over panel that displays win or lose screen.
 * Shows result image and restart button.
 */
public class GameOverPanel extends JPanel implements MouseListener {

    private Image backgroundImage;
    private Image resultImage;
    private Rectangle restartButtonBounds = new Rectangle();
    private JFrame parentFrame;
    private boolean isWin;

    public GameOverPanel(JFrame frame, boolean isWin) {
        this.parentFrame = frame;
        this.isWin = isWin;
        loadImages();
        setupPanel();
        addMouseListener(this);
    }

    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(new File(Constants.Paths.WALLPAPER_IMAGE));
            if (isWin) {
                resultImage = ImageIO.read(new File(Constants.Paths.WIN_IMAGE));
            } else {
                resultImage = ImageIO.read(new File(Constants.Paths.LOSE_IMAGE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupPanel() {
        setPreferredSize(new Dimension(Constants.Game.WINDOW_WIDTH, Constants.Game.WINDOW_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Draw background
        graphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

        // Draw result image at center
        int resultWidth = 600;
        int resultHeight = 400;
        int resultX = (getWidth() - resultWidth) / 2;
        int resultY = (getHeight() - resultHeight) / 2 - 50;
        graphics.drawImage(resultImage, resultX, resultY, resultWidth, resultHeight, null);

        // Draw restart button
        int buttonWidth = 200;
        int buttonHeight = 60;
        int buttonX = (getWidth() - buttonWidth) / 2;
        int buttonY = resultY + resultHeight + 50;
        restartButtonBounds.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        graphics.setColor(new Color(0, 150, 0));
        graphics.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20);
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 24));
        String buttonText = "RESTART";
        FontMetrics fm = graphics.getFontMetrics();
        int textX = buttonX + (buttonWidth - fm.stringWidth(buttonText)) / 2;
        int textY = buttonY + ((buttonHeight - fm.getHeight()) / 2) + fm.getAscent();
        graphics.drawString(buttonText, textX, textY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (restartButtonBounds != null && restartButtonBounds.contains(e.getPoint())) {
            restartGame();
        }
    }

    private void restartGame() {
        parentFrame.getContentPane().removeAll();
        MenuPanel menuPanel = new MenuPanel(parentFrame);
        parentFrame.add(menuPanel);
        parentFrame.pack();
        parentFrame.setLocationRelativeTo(null);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
