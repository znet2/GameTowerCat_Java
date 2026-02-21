package com.game.core;

import com.game.utils.Constants;
import com.game.utils.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main menu panel that displays before the game starts.
 * Shows wallpaper background and start button.
 */
public class MenuPanel extends JPanel {

    private Image wallpaperImage;
    private Image startButtonImage;
    private Image logoImage;
    private Rectangle startButtonBounds = new Rectangle();
    private JFrame parentFrame;

    public MenuPanel(JFrame frame) {
        this.parentFrame = frame;
        loadImages();
        setupPanel();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    private void loadImages() {
        wallpaperImage = ImageLoader.loadImage(Constants.Paths.WALLPAPER_IMAGE);
        startButtonImage = ImageLoader.loadImage(Constants.Paths.START_BUTTON_IMAGE);
        logoImage = ImageLoader.loadImage(Constants.Paths.LOGO_GAME_IMAGE);
    }

    private void setupPanel() {
        setPreferredSize(new Dimension(Constants.Game.WINDOW_WIDTH, Constants.Game.WINDOW_HEIGHT));
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
        startButtonBounds.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        // Draw start button (scaled to constant size)
        graphics.drawImage(
                startButtonImage,
                startButtonBounds.x,
                startButtonBounds.y,
                buttonWidth,
                buttonHeight,
                null);
    }

    private void handleMouseClick(MouseEvent e) {
        // Check if start button was clicked
        if (startButtonBounds != null && startButtonBounds.contains(e.getPoint())) {
            startGame();
        }
    }

    private void startGame() {
        // Remove menu panel and add game panel
        parentFrame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(parentFrame);
        parentFrame.add(gamePanel);
        parentFrame.pack();
        parentFrame.setLocationRelativeTo(null);
        parentFrame.revalidate();
        gamePanel.requestFocusInWindow();
    }
}
