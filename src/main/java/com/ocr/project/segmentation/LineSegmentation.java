package com.ocr.project.segmentation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class LineSegmentation {
	
	private BufferedImage B;
	
//	public LineSegmentation(BufferedImage B){
//		this.B = B;
//	}
	
	public List<BufferedImage> segment(BufferedImage B){
		
		List<BufferedImage> lines = new LinkedList<>();
		int width = B.getWidth();
		int y0 = -1;
		int y1 = -1;
		
		for(int y=0;y<B.getHeight();++y){
			if(SegmentationHelper.rowHasBlackPixel(B,y)){
				if(y0 == -1){y0 = y;}
				y1 = y;
			}else if(y0 != -1 && y0 != y1){
				Image subimage = B.getSubimage(0,y0,width,y1-y0);
				
				System.out.println(subimage);
			}
		}
		return lines;
	}

}
