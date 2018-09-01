package com.ocr.project.segmentation;

import java.awt.image.BufferedImage;

import com.ocr.project.common.Config;


public class SegmentationHelper {

public static boolean rowHasBlackPixel(BufferedImage G, int row) {
        
        for(int x = 0; x < G.getWidth(); ++x){
            int color = Math.abs(G.getRGB(x, row));
            if(color > Config.THRESHOLD) { return true; }
        }        
        return false;
    }
    
    public static boolean columnHasBlackPixel(BufferedImage G, int col) {
        
        for(int y = 0; y < G.getHeight(); ++y) {
            int color = Math.abs(G.getRGB(col, y));
            if(color > Config.THRESHOLD) { return true; }
        }
        return false;
    }
    
    public static BufferedImage trim(BufferedImage G){
        
        // Find corners and return subimage
        
        return null;
    }
}
