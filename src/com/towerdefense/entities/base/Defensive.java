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
}