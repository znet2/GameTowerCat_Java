package com.game.utils;

/**
 * Utility class for common mathematical operations used in the game.
 * Provides helper methods for distance calculations and coordinate conversions.
 */
public class MathUtils {
    
    // Prevent instantiation
    private MathUtils() {}
    
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