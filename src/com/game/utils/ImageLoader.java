package com.game.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for loading images from resources.
 * Works both when running from IDE and from JAR file.
 */
public final class ImageLoader {

    // Prevent instantiation
    private ImageLoader() {
    }

    /**
     * Loads an image from the given path.
     * First tries to load from classpath (works in JAR),
     * then falls back to file system if needed.
     * 
     * @param path the path to the image file
     * @return the loaded BufferedImage, or null if loading fails
     */
    public static BufferedImage loadImage(String path) {
        try {
            // Try to load from classpath first (works in JAR)
            InputStream stream = ImageLoader.class.getClassLoader().getResourceAsStream(path);
            if (stream != null) {
                BufferedImage image = ImageIO.read(stream);
                stream.close();
                return image;
            }
            
            // Fallback: try loading from file system (for development)
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                return ImageIO.read(file);
            }
            
            System.err.println("Image not found: " + path);
            return null;
            
        } catch (IOException e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            return null;
        }
    }
}
