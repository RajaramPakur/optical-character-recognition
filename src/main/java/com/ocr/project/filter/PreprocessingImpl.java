package com.ocr.project.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import org.springframework.stereotype.Repository;

/**
 *
 * @author RajaramPakur
 */
@Repository
public class PreprocessingImpl implements Preprocessing {

    /**
     *
     * @param B
     * @return
     */
    @Override
    public BufferedImage GrayImage(BufferedImage B) {

        int width = B.getWidth();
        int height = B.getHeight();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                Color c = new Color(B.getRGB(j, i));
                int red = (int) (c.getRed() * 0.2989);
                int green = (int) (c.getGreen() * 0.5870);
                int blue = (int) (c.getBlue() * 0.1140);
                Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);

                B.setRGB(j, i, newColor.getRGB());
            }
        }
        return B;
    }

    @Override
    public BufferedImage BinaryImage(BufferedImage original) {

        int red;
        int newPixel;

        int threshold = otsuTreshold(original);

        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                //int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if (red > threshold) {
                    newPixel = 255;
                } else {
                    newPixel = 0;
                }

                Color a = new Color(newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, a.getRGB());

            }
        }

        return binarized;

    }

    /**
     *
     * @param input
     * @return histogram of grayscale image
     */
    public static int[] imageHistogram(BufferedImage input) {

        int[] histogram = new int[256];

        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int red = new Color(input.getRGB(i, j)).getRed();
                histogram[red]++;
            }
        }

        return histogram;

    }

    /**
     *
     * @param original
     * @return Otsu's threshold value
     */
    private static int otsuTreshold(BufferedImage original) {

        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) {
                continue;
            }
            wF = total - wB;

            if (wF == 0) {
                break;
            }

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;

    }

    @Override
    public BufferedImage DecBrightness(BufferedImage B) {

        RescaleOp op = new RescaleOp(0.9f, 0, null);
        BufferedImage bright = op.filter(B, null);

        return bright;
    }

    @Override
    public BufferedImage IncContrast(BufferedImage B) {

        RescaleOp rescale = new RescaleOp(1.0f, 20.0f, null);
        BufferedImage incCon = rescale.filter(B, null);
        return incCon;
    }

}
