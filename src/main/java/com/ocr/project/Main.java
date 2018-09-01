package com.ocr.project;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.mortennobel.imagescaling.ResampleOp;
import com.ocr.project.common.ImageHelper;
import com.ocr.project.feature_extraction.FeatureExtraction;
import com.ocr.project.filter.Binarisation;
import com.ocr.project.filter.Preprocessing;
import com.ocr.project.neural_network.NeuralNetwork;
import com.ocr.project.normalization.Normalization;
import com.ocr.project.segmentation.Segmentation;
import com.ocr.project.trails.PreProcessing;

public class Main {



	public static BufferedImage identify(BufferedImage G) throws IOException {

		StringBuilder str = new StringBuilder();
		BufferedImage binaryImg = Binarisation.GetBmp(G);

		List<BufferedImage> segmentedLetters = new LinkedList<>();
		segmentedLetters = Segmentation.segment(binaryImg);
		// segmentedLetters.add(binaryImg);
		ResampleOp resamOp = new ResampleOp(12, 12);
		int i = 0;
		int count = 0;

		// int finalText[] = null;
		Arrays.stream(new File("D:\\java class\\spring\\OCR\\src\\main\\webapp\\resources\\normalized\\").listFiles())
				.forEach(File::delete);

		for (BufferedImage j : segmentedLetters) {

			int letter = 0;
			float error[] = new float[62];
			BufferedImage modifiedImage = resamOp.filter(j, null);
			File f1 = new File("D:\\java class\\spring\\OCR\\src\\main\\webapp\\resources\\normalized\\" + i + ".jpg");
			ImageIO.write(modifiedImage, "jpg", f1);

			File f = new File("D:\\java class\\spring\\OCR\\src\\main\\webapp\\resources\\segment\\" + i + ".jpg");

			ImageIO.write(j, "jpg", f);
			i++;

			// template matching
			for (int k = 0; k < 62; k++) {

				int err = 0;
				File input = new File("D:\\java class\\spring\\OCR\\src\\main\\webapp\\resources\\template\\" + k + ".jpg");
				BufferedImage tempImg = ImageIO.read(input);

				for (int p = 0; p < 12; p++) {
					for (int q = 0; q < 12; q++) {

						Color original = new Color(modifiedImage.getRGB(p, q));
						Color template = new Color(tempImg.getRGB(p, q));

						int avgOriginal = (original.getBlue() + original.getGreen() + original.getRed() / 3);
						int avgTemplate = (template.getBlue() + template.getGreen() + template.getRed() / 3);
						int different = avgTemplate - avgOriginal;

						if (different > 0 || different < 0) {
							err = err + 1;
						}

					}
				}

				float e = (err * 100 / 144);

				error[k] = e;

			}

			float smallest = error[0];
			for (int l = 0; l < 62; l++) {

				if (error[l] < smallest) {

					smallest = error[l];
					letter = l;
				}
			}

			char text = Indexing.match(letter);
			str.append(text);

		}

		FileUtils.writeStringToFile(
				new File("D:\\java class\\spring\\OCR\\src\\main\\webapp\\resources\\images\\text.txt"),
				str.toString());
		return binaryImg;
	}
}
