package com.towerdefense.managers;

import com.towerdefense.utils.Constants;
import java.awt.*;

/**
 * Manages the player's coin currency system for the tower defense game.
 * Handles coin earning, spending, validation, and display logic.
 */
public class CoinManager {

    // Current coin state
    private int currentCoins;
    private int passiveIncomeTimer = 0;

    // Constructor that initializes the coin system
    // Sets the player's starting coin amount
    public CoinManager() {
        this.currentCoins = Constants.Economy.STARTING_COINS;
    }

    // Gets the current number of coins the player has
    // Used for display and validation purposes
    // @return current coin count
    public int getCurrentCoins() {
        return currentCoins;
    }

    // Updates the coin manager each frame
    // Handles passive income generation over time
    public void update() {
        passiveIncomeTimer++;

        if (passiveIncomeTimer >= Constants.Economy.PASSIVE_INCOME_INTERVAL_FRAMES) {
            addCoins(Constants.Economy.PASSIVE_INCOME_AMOUNT);
            passiveIncomeTimer = 0;
        }
    }

    // Adds coins to the player's total
    // Used when enemies are defeated or other rewards are earned
    // @param amount - number of coins to add (must be positive)
    public void addCoins(int amount) {
        if (amount > 0) {
            currentCoins += amount;
        }
    }

    // Attempts to spend coins for purchases
    // Validates that player has enough coins before spending
    // @param amount - number of coins to spend
    // @return true if purchase was successful, false if insufficient funds
    public boolean spendCoins(int amount) {
        if (canAfford(amount)) {
            currentCoins -= amount;
            return true;
        }
        return false;
    }

    // Checks if the player can afford a purchase
    // Validates coin availability without actually spending
    // @param amount - cost to check
    // @return true if player has enough coins, false otherwise
    public boolean canAfford(int amount) {
        return currentCoins >= amount && amount >= 0;
    }

    // Awards coins for defeating an enemy
    // Standardized reward system for enemy kills
    public void awardCoinsForEnemyKill() {
        addCoins(Constants.Economy.COINS_PER_ENEMY_KILL);
    }

    // Gets the cost of placing a tank
    // Used for UI feedback and purchase validation
    // @return cost of a tank in coins
    public int getTankCost() {
        return Constants.Entities.TANK_COST;
    }

    // Attempts to purchase a tank
    // Handles the transaction for tank placement
    // @return true if tank was purchased successfully, false if insufficient funds
    public boolean purchaseTank() {
        return spendCoins(Constants.Entities.TANK_COST);
    }

    // Renders the coin display on the screen
    // Shows current coin count with background and formatting
    // @param graphics - Graphics context for drawing
    // @param screenWidth - width of the game screen for positioning
    // @param screenHeight - height of the game screen for positioning
    public void renderCoinDisplay(Graphics graphics, int screenWidth, int screenHeight) {
        String coinText = "Coins: " + currentCoins;

        // Set up font and get text dimensions
        graphics.setFont(Constants.UI.COIN_FONT);
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(coinText);
        int textHeight = fontMetrics.getHeight();

        // Calculate position (bottom-right corner with padding)
        int textX = screenWidth - textWidth - Constants.UI.COIN_DISPLAY_PADDING;
        int textY = screenHeight - Constants.UI.COIN_DISPLAY_PADDING;

        // Draw background rectangle for better visibility
        int backgroundX = textX - Constants.UI.COIN_DISPLAY_PADDING / 2;
        int backgroundY = textY - textHeight + fontMetrics.getDescent();
        int backgroundWidth = textWidth + Constants.UI.COIN_DISPLAY_PADDING;
        int backgroundHeight = textHeight;

        graphics.setColor(Constants.UI.COIN_BACKGROUND_COLOR);
        graphics.fillRect(backgroundX, backgroundY, backgroundWidth, backgroundHeight);

        // Draw the coin text
        graphics.setColor(Constants.UI.COIN_TEXT_COLOR);
        graphics.drawString(coinText, textX, textY);
    }

    // Resets coins to starting amount
    // Used for game restart or new game scenarios
    public void resetCoins() {
        currentCoins = Constants.Economy.STARTING_COINS;
    }

    // Gets the reward amount for killing an enemy
    // Used for UI feedback and game balance information
    // @return coins awarded per enemy kill
    public int getEnemyKillReward() {
        return Constants.Economy.COINS_PER_ENEMY_KILL;
    }
}