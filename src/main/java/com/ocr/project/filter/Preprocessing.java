package com.ocr.project.filter;

import java.awt.image.BufferedImage;

/**
 * 
 * @author RajaramPakur
 */
public interface Preprocessing {
	
	public BufferedImage GrayImage(BufferedImage B);

	public BufferedImage BinaryImage(BufferedImage B);
	
	public BufferedImage DecBrightness(BufferedImage B);
	
	public BufferedImage IncContrast(BufferedImage B);
	
	

}
