package com.game.entities.base;

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
}