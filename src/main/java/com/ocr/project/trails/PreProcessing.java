package com.ocr.project.trails;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import com.ocr.project.common.Config;

public class PreProcessing {

	// to change the rgb image to grayImage
	public BufferedImage grayImg(BufferedImage B) {

		int width = B.getWidth();
		int height = B.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// to get the rbg color of buffered image
				Color rgb = new Color(B.getRGB(x, y));
				// get the average color of the buffered image
				int avgColor = ((rgb.getBlue() + rgb.getGreen() + rgb.getRed()) / 3);
				// set the avg color as new color and change into gray
				Color avg = new Color(avgColor, avgColor, avgColor);
				B.setRGB(x, y, avg.getRGB());

			}

		}

		// another process to change into grayscale image

		BufferedImage bi = bwImg(B);

		return bi;
	}

	// To change the gray image to b/wImage
	public static BufferedImage bwImg(BufferedImage B) {

		// BufferedImage bi = new BufferedImage(B.getWidth(), B.getHeight(),
		// BufferedImage.TYPE_BYTE_BINARY);
		// Graphics2D graphics = bi.createGraphics();
		// graphics.drawImage(B, 0, 0, null);

		int newColor;
		for (int y = 0; y < B.getHeight(); y++) {
			for (int x = 0; x < B.getWidth(); x++) {

				Color color = new Color(B.getRGB(x, y));
				int avgColor = ((color.getBlue() + color.getGreen() + color.getRed()) / 3);
				if (avgColor < 100) {
					newColor = 0;
				} else {
					newColor = 255;
				}
				Color a = new Color(255 - newColor, 255 - newColor, 255 - newColor);
				B.setRGB(x, y, a.getRGB());

			}
		}
		return B;
	}

	public static List<BufferedImage> label(BufferedImage G) {

		List<BufferedImage> separatedChars = new LinkedList<>();
		// BufferedImage B = grayImg(G);
		List<BufferedImage> lines = line(G);

		for (BufferedImage line : lines) {
			List<BufferedImage> characters = character(line);

			for (BufferedImage character : characters) {
				// ImageHelper.printImage(character, "character.png");
				BufferedImage trimmed = trim(character);
				// ImageHelper.printImage(trimmed, "trimmed.png");
				separatedChars.add(trimmed);
			}
		}
		return separatedChars;
	}

	public static List<BufferedImage> line(BufferedImage G) {

		int width = G.getWidth() - 1;
		int y0 = -1;
		int y1 = -1;
		List<BufferedImage> lines = new LinkedList<>();

		for (int y = 0; y < G.getHeight(); ++y) {
			if (rowHasBlackPixel(G, y)) {
				if (y0 == -1) {
					y0 = y;
				}
				y1 = y;
			} else if (y0 != -1 && y0 != y1) {
				BufferedImage subimage = G.getSubimage(0, y0, width, y1 - y0);
				lines.add(subimage);
				y0 = y1 = -1;
			}
		}
		// In case the image ends on a black pixel we want to get the last line
		if (y0 != -1 && y0 != y1) {
			BufferedImage subimage = G.getSubimage(0, y0, width, y1 - y0);
			lines.add(subimage);
		}

		return lines;
	}

	private static List<BufferedImage> character(BufferedImage G) {
		int height = G.getHeight() - 1;
		int x0 = -1;
		int x1 = -1;
		List<BufferedImage> characters = new LinkedList<>();

		for (int x = 0; x < G.getWidth(); ++x) {
			if (colHasBlackPixel(G, x)) {
				if (x0 == -1) {
					x0 = x;
				}
				x1 = x;
			} else if (x0 != -1 && x0 != x1) {
				// System.out.println(String.format("x1:%d x0:%d", x1, x0));
				BufferedImage subimage = G.getSubimage(x0, 0, x1 - x0, height);
				characters.add(subimage);
				x0 = x1 = -1;
			}
		}
		// In case the image ends on a black pixel we want to get the last character
		if (x0 != -1 && x0 != x1) {
			BufferedImage subimage = G.getSubimage(x0, 0, x1 - x0, height);
			characters.add(subimage);
		}

		return characters;
	}

	private static boolean colHasBlackPixel(BufferedImage G, int col) {
		for (int y = 0; y < G.getHeight(); ++y) {
			int color = Math.abs(G.getRGB(col, y));
			if (color > Config.THRESHOLD) {
				return true;
			}
		}
		return false;
	}

	private static BufferedImage trim(BufferedImage G) {
		int width = G.getWidth() - 1;
		int y0 = -1;
		int y1 = -1;

		for (int y = 0; y < G.getHeight(); ++y) {
			if (rowHasBlackPixel(G, y)) {
				if (y0 == -1) {
					y0 = y;
				}
				y1 = y;
			} else if (y0 != -1 && y0 != y1) {
				return G.getSubimage(0, y0, width, y1 - y0);
			}
		}

		return G.getSubimage(0, y0, width, y1 - y0);
	}

	private static boolean rowHasBlackPixel(BufferedImage G, int row) {
		for (int x = 0; x < G.getWidth(); ++x) {
			int color = Math.abs(G.getRGB(x, row));
			if (color > Config.THRESHOLD) {
				return true;
			}
		}
		return false;
	}

}
