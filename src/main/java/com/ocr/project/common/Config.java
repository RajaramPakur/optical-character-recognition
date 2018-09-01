package com.ocr.project.common;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Config {
	

    // Image Settings
    public static final int THRESHOLD = 127;
    public static final int IMAGE_HEIGHT = 28;
    public static final int IMAGE_WIDTH = 28;

    // Neural Network Settings
    public static final int OUTPUT_NEURONS = 10;
    public static final double ERROR_THRESHOLD = 0.015;
    public static final double EPOCH_THRESHOLD = 750;
    public static final String NN_FILENAME = "neural_network.eg";
    public static final char[] CHARACTER_LOOKUP = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    
    // MNIST Database
    public static final String MNIST_TRAIN_IMAGES = "src/mnist/resources/train-images-idx3-ubyte";
    public static final String MNIST_TRAIN_LABELS = "src/mnist/resources/train-labels-idx1-ubyte";
    public static final String MNIST_TEST_IMAGES = "src/mnist/resources/t10k-images-idx3-ubyte";
    public static final String MNIST_TEST_LABELS = "src/mnist/resources/t10k-labels-idx1-ubyte";
    
    // Logging
    public static Logger getLogger(String name) {
        
        Logger l = Logger.getLogger(name);
        try{
            Handler h = new FileHandler("data.log");
            Formatter f = new SimpleFormatter();
            h.setFormatter(f);
            l.addHandler(h);
        } catch(IOException | SecurityException e) { System.out.println(e); }
        
        return l;
    }
    
    
    /**
     * This method will threshold the image. It will generate a binary image.
     * This method will calculate the mean of the pixels under a mask of dimension: size x size.
     * 
     * If value of pixel(x,y) > mean then set Pixel (x,y) to WHITE
     * else set Pixel (x,y) to BLACK.
     * 
     * @param img the Image object passed on which thresholding is performed.
     * @param maskSize The size of the mask. [3,5,7,9,...]
     * @param C The constant value that is subtracted from mean. [3,7,10,...]
     */
    public static BufferedImage adaptiveThreshold_Mean(BufferedImage img, int maskSize, int C){
        
        //image img dimension
        int width = img.getWidth();
        int height = img.getHeight();
        
        //output array it will hold the result of threshold that will be saaved back to img
        int output[] = new int[width*height];
        
        //variables
        int blue, mean, count;
        
        /** find mean and threshold the pixel */
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                blue = 0;
                count = 0;
                Color color = new Color(img.getRGB(x, y));
                for(int r = y - (maskSize / 2); r <= y + (maskSize / 2); r++){
                    for(int c = x - (maskSize / 2); c <= x + (maskSize / 2); c++){
                        if(r < 0 || r >= height || c < 0 || c >= width){
                            /** Some portion of the mask is outside the image. */
                            continue;
                        }else{
                            try{
                            	
                                blue += color.getBlue();
                                count++;
                            }catch(ArrayIndexOutOfBoundsException e){
                            }
                        }
                    }
                }
                
                /** get mean pixel value */
                mean = blue/count - C;
                
                //adaptive threshold - mean
                if(color.getBlue()>= mean){
                    output[x+y*width] = 0xffffffff;     //WHITE
                }else{
                    output[x+y*width] = 0xff000000;     //BLACK
                }
            }
        }
        
        //save output value in image img
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                img.setRGB(x, y, output[x+y*width]);
            }
        }
        
        return img;
    }
    
    /**
     * This method will threshold the image. It will generate a binary image.
     * 
     * @param img the Image object passed on which thresholding is performed.
     * @param thresholdValue value to be compared with the average pixel value.
     */
    public static BufferedImage toBinary(BufferedImage img, int thresholdValue){
        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
            	Color color = new Color(img.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                double tmp = (0.2126*r + 0.7152*g + 0.0722*b);
                int newColor;
                if(tmp >= thresholdValue){
					newColor = 0;
				}else{
					 newColor = 255;
				}
				Color a = new Color(255-newColor,255-newColor,255-newColor);
				img.setRGB(x, y, a.getRGB());
//                if(tmp >= thresholdValue){
////                    img.setPixel(x,y,255,255,255,255);  //set WHITE
//                }else{
////                	img.setPixel(x,y,255,0,0,0);  //set BLACK                    
//                }
            }
        }
        
        return img;
    }


}
