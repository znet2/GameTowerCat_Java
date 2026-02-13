package com.towerdefense.core;

import com.towerdefense.utils.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * Main menu panel that displays before the game starts.
 * Shows wallpaper background and start button.
 */
public class MenuPanel extends JPanel implements MouseListener {

    private Image wallpaperImage;
    private Image startButtonImage;
    private Image logoImage;
    private Rectangle startButtonBounds;
    private JFrame parentFrame;

    public MenuPanel(JFrame frame) {
        this.parentFrame = frame;
        loadImages();
        setupPanel();
        addMouseListener(this);
    }

    private void loadImages() {
        try {
            wallpaperImage = ImageIO.read(new File(Constants.Paths.WALLPAPER_IMAGE));
            startButtonImage = ImageIO.read(new File(Constants.Paths.START_BUTTON_IMAGE));
            logoImage = ImageIO.read(new File(Constants.Paths.LOGO_GAME_IMAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupPanel() {
        setPreferredSize(new Dimension(wallpaperImage.getWidth(null), wallpaperImage.getHeight(null)));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Draw wallpaper background
        graphics.drawImage(wallpaperImage, 0, 0, getWidth(), getHeight(), null);

        // Draw logo at top center
        int logoWidth = Constants.UI.LOGO_WIDTH;
        int logoHeight = Constants.UI.LOGO_HEIGHT;
        int logoX = (getWidth() - logoWidth) / 2;
        int logoY = Constants.UI.LOGO_TOP_MARGIN;
        graphics.drawImage(logoImage, logoX, logoY, logoWidth, logoHeight, null);

        // Calculate start button position (center + Y offset)
        int buttonWidth = Constants.UI.START_BUTTON_WIDTH;
        int buttonHeight = Constants.UI.START_BUTTON_HEIGHT;
        int buttonX = (getWidth() - buttonWidth) / 2;
        int buttonY = (getHeight() - buttonHeight) / 2 + Constants.UI.START_BUTTON_Y_OFFSET;
        startButtonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);

        // Draw start button (scaled to constant size)
        graphics.drawImage(
                startButtonImage,
                startButtonBounds.x,
                startButtonBounds.y,
                buttonWidth,
                buttonHeight,
                null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Check if start button was clicked
        if (startButtonBounds.contains(e.getPoint())) {
            startGame();
        }
    }

    private void startGame() {
        // Remove menu panel and add game panel
        parentFrame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel();
        parentFrame.add(gamePanel);
        parentFrame.pack();
        parentFrame.setLocationRelativeTo(null);
        parentFrame.revalidate();
        gamePanel.requestFocusInWindow();
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
