package com.towerdefense.main;

import com.towerdefense.core.MenuPanel;
import com.towerdefense.utils.Constants;
import javax.swing.*;

/**
 * Main entry point for the Tower Defense game.
 * Sets up the game window and initializes the menu panel.
 */
public class Main {

    // Entry point for the application
    // Creates and displays the main game window
    public static void main(String[] args) {
        createAndShowGameWindow();
    }

    // Creates the main game window and sets it up for display
    // Handles window configuration, menu panel creation, and final display setup
    private static void createAndShowGameWindow() {
        JFrame gameWindow = new JFrame(Constants.Game.TITLE);
        configureWindow(gameWindow);

        MenuPanel menuPanel = new MenuPanel(gameWindow);
        gameWindow.add(menuPanel);

        finalizeWindow(gameWindow);
    }

    // Configures basic window properties
    // Sets close operation and prevents window resizing
    // @param window - the JFrame to configure
    private static void configureWindow(JFrame window) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
    }

    // Finalizes window setup and makes it visible
    // Packs components, centers window, and displays it
    // @param window - the JFrame to finalize and show
    private static void finalizeWindow(JFrame window) {
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}