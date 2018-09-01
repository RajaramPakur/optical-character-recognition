package com.ocr.project.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/**
 *
 * @author RajaramPakur
 */
public class Binarisation {

    private static BufferedImage grayscale, binarized, filteredImg, enhancedImg;

    /**
     *
     * @param original
     * @return
     */
    public static BufferedImage GetBmp(BufferedImage original) {

        grayscale = toGray(original);

        enhancedImg = EnhanceImg(grayscale);

        filteredImg = MedianFilter.Filter(enhancedImg);

        binarized = binarize(filteredImg);
        // noiseremove(binarized);
        return binarized;
        // writeImage(output_f,parts[1]);

    }

    /**
     *
     * @param Image
     */
    public static void noiseremove(BufferedImage Image) {
        int Height = Image.getHeight();
        int Width = Image.getWidth();
        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                int red = new Color(Image.getRGB(j, i)).getRed();
                int T = 0;
                if (j - 1 >= 0) {
                    if (red != (new Color(Image.getRGB(j - 1, i)).getRed())) {
                        T++;
                    }
                } else {
                    T++;
                }
                if (i - 1 >= 0) {
                    if (red != (new Color(Image.getRGB(j, i - 1)).getRed())) {
                        T++;
                    }
                } else {
                    T++;
                }
                if (j + 1 < Width) {
                    if (red != (new Color(Image.getRGB(j + 1, i)).getRed())) {
                        T++;
                    }
                } else {
                    T++;
                }
                if (i + 1 < Height) {
                    if (red != (new Color(Image.getRGB(j, i + 1)).getRed())) {
                        T++;
                    }
                } else {
                    T++;
                }

                if (T == 4) {
                    int newPixel = new Color(Image.getRGB(j, i + 1)).getRed();
                    int alpha = new Color(Image.getRGB(j, i + 1)).getAlpha();
                    newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                    Image.setRGB(j, i, newPixel);

                }
            }
        }

    }

    // Return histogram of grayscale image
    /**
     *
     * @param input
     * @return
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
     * The luminance method
     *
     * @param original
     * @return
     */
    public static BufferedImage toGray(BufferedImage original) {

        int alpha, red, green, blue;
        int newPixel;

        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                float sum = (float) (0.2989 * red + 0.5870 * green + 0.1140 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, sum, sum, sum);

                // Write pixels into image
                lum.setRGB(i, j, newPixel);

            }
        }

        return lum;

    }

    private static int colorToRGB(int alpha, float red, float green, float blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }

    /**
     * Get binary treshold using Otsu's method
     *
     * @param original
     * @return
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

    private static BufferedImage binarize(BufferedImage original) {

        int newPixel;

        int threshold = otsuTreshold(original);

        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels
                Color color = new Color(original.getRGB(i, j));
                int avgColor = ((color.getBlue() + color.getGreen() + color.getRed()) / 3);
                if (avgColor > threshold) {
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

    private static BufferedImage EnhanceImg(BufferedImage grayImg) {

        enhancedImg = grayImg;
        float avgColor = 0, meanIn = 0;
        for (int y = 0; y < grayImg.getHeight(); y++) {
            for (int x = 0; x < grayImg.getWidth(); x++) {

                Color color = new Color(grayImg.getRGB(x, y));
                avgColor = (float) (avgColor + ((color.getBlue() + color.getGreen() + color.getRed()) / 3));
            }
        }

        meanIn = avgColor / (grayImg.getHeight() * grayImg.getWidth());
        // System.out.println(meanIn);
        if (meanIn < 120) {
            enhancedImg = DecBrightness(grayImg);
        }

        return enhancedImg;
    }

    /**
     * for brightness control
     *
     * @param B
     * @return
     */
    public static BufferedImage DecBrightness(BufferedImage B) {

        RescaleOp op = new RescaleOp(1.2f, 0, null);
        BufferedImage bright = op.filter(B, null);

        return IncContrast(bright);
    }

    /**
     * for contrast control
     *
     * @param B
     * @return
     */
    public static BufferedImage IncContrast(BufferedImage B) {

        RescaleOp rescale = new RescaleOp(2f, 10.0f, null);
        BufferedImage incCon = rescale.filter(B, null);
        return incCon;
    }

}
