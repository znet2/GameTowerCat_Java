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
    private Image restartButtonImage;
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
            if (isWin) {
                backgroundImage = ImageIO.read(new File(Constants.Paths.WIN_BACKGROUND_IMAGE));
                resultImage = ImageIO.read(new File(Constants.Paths.WIN_IMAGE));
            } else {
                backgroundImage = ImageIO.read(new File(Constants.Paths.LOSE_BACKGROUND_IMAGE));
                resultImage = ImageIO.read(new File(Constants.Paths.LOSE_IMAGE));
            }
            restartButtonImage = ImageIO.read(new File(Constants.Paths.RESTART_BUTTON_IMAGE));
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
        int resultWidth = isWin ? Constants.UI.WIN_IMAGE_WIDTH : Constants.UI.LOSE_IMAGE_WIDTH;
        int resultHeight = isWin ? Constants.UI.WIN_IMAGE_HEIGHT : Constants.UI.LOSE_IMAGE_HEIGHT;
        int resultYOffset = isWin ? Constants.UI.WIN_IMAGE_Y_OFFSET : Constants.UI.LOSE_IMAGE_Y_OFFSET;
        int resultX = (getWidth() - resultWidth) / 2;
        int resultY = (getHeight() - resultHeight) / 2 + resultYOffset;
        graphics.drawImage(resultImage, resultX, resultY, resultWidth, resultHeight, null);

        // Draw restart button
        int buttonWidth = Constants.UI.RESTART_BUTTON_WIDTH;
        int buttonHeight = Constants.UI.RESTART_BUTTON_HEIGHT;
        int buttonX = (getWidth() - buttonWidth) / 2;
        int buttonY = resultY + resultHeight + Constants.UI.RESTART_BUTTON_Y_OFFSET;
        restartButtonBounds.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        graphics.drawImage(restartButtonImage, buttonX, buttonY, buttonWidth, buttonHeight, null);
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
