package com.towerdefense.entities.base;

/**
 * Interface for defensive units that can take damage and block enemies.
 * Defensive units do not attack but serve as obstacles and damage absorbers.
 */
public interface Defensive {

    // Takes damage and reduces health
    // @param damageAmount - amount of damage to apply
    void takeDamage(int damageAmount);

    // Checks if the defensive unit is destroyed
    // @return true if unit is dead/destroyed, false otherwise
    boolean isDestroyed();

    // Gets the current health of the defensive unit
    // @return current health points
    int getCurrentHealth();

    // Gets the maximum health of the defensive unit
    // @return maximum health points
    int getMaxHealth();

    // Gets the defensive resistance/armor value (optional)
    // @return resistance value that reduces incoming damage
    default int getDefenseRating() {
        return 0; // Default no defense bonus
    }

    // Gets the health percentage for UI display
    // @return health as a percentage (0.0 to 1.0)
    default double getHealthPercentage() {
        return (double) getCurrentHealth() / getMaxHealth();
    }

    // Legacy method for compatibility with existing enemy damage system
    // Delegates to the takeDamage method
    // @param damageAmount - amount of damage to apply
    default void damage(int damageAmount) {
        takeDamage(damageAmount);
    }

    // Legacy method for compatibility with existing game systems
    // Delegates to the isDestroyed method
    // @return true if unit is dead, false otherwise
    default boolean isDead() {
        return isDestroyed();
    }
}