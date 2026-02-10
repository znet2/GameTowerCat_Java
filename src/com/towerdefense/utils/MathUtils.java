package com.towerdefense.utils;

import java.awt.Point;

/**
 * Utility class for common mathematical operations used in the game.
 * Provides helper methods for distance calculations, vector math, and other utilities.
 */
public class MathUtils {
    
    // Prevent instantiation
    private MathUtils() {}
    
    // Calculates the Euclidean distance between two points
    // @param point1 - first point
    // @param point2 - second point
    // @return distance between the points
    public static double calculateDistance(Point point1, Point point2) {
        double deltaX = point2.x - point1.x;
        double deltaY = point2.y - point1.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    
    // Calculates the distance between two coordinate pairs
    // @param x1 - x coordinate of first point
    // @param y1 - y coordinate of first point
    // @param x2 - x coordinate of second point
    // @param y2 - y coordinate of second point
    // @return distance between the points
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    
    // Normalizes a vector to unit length
    // @param deltaX - x component of the vector
    // @param deltaY - y component of the vector
    // @return Point containing the normalized vector components
    public static Point normalizeVector(double deltaX, double deltaY) {
        double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (length == 0) {
            return new Point(0, 0);
        }
        return new Point((int) (deltaX / length), (int) (deltaY / length));
    }
    
    // Clamps a value between a minimum and maximum
    // @param value - value to clamp
    // @param min - minimum allowed value
    // @param max - maximum allowed value
    // @return clamped value
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    // Clamps a double value between a minimum and maximum
    // @param value - value to clamp
    // @param min - minimum allowed value
    // @param max - maximum allowed value
    // @return clamped value
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    // Converts grid coordinates to pixel coordinates
    // @param gridPosition - position in grid units
    // @param tileSize - size of each tile in pixels
    // @return pixel position
    public static int gridToPixel(int gridPosition, int tileSize) {
        return gridPosition * tileSize;
    }
    
    // Converts pixel coordinates to grid coordinates
    // @param pixelPosition - position in pixels
    // @param tileSize - size of each tile in pixels
    // @return grid position
    public static int pixelToGrid(int pixelPosition, int tileSize) {
        return pixelPosition / tileSize;
    }
    
    // Checks if a point is within bounds
    // @param x - x coordinate to check
    // @param y - y coordinate to check
    // @param width - width of the bounds
    // @param height - height of the bounds
    // @return true if point is within bounds, false otherwise
    public static boolean isWithinBounds(int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }
}