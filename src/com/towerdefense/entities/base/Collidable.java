package com.towerdefense.entities.base;

import java.awt.*;

/**
 * Interface for objects that can participate in collision detection.
 * Provides collision bounds for interaction with other game objects.
 */
public interface Collidable {
    
    // Gets the collision bounds for this object
    // Used for collision detection with other game objects
    // @return Rectangle representing the object's collision area
    Rectangle getCollisionBounds();
    
    // Checks if this object collides with another collidable object
    // @param other - the other collidable object to check against
    // @return true if collision occurs, false otherwise
    default boolean collidesWith(Collidable other) {
        return getCollisionBounds().intersects(other.getCollisionBounds());
    }
}