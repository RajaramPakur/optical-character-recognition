package com.ocr.project;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.ocr.project.daos.FileDao;
import com.ocr.project.model.Image;

@Controller
public class ProcessController {

	@Autowired
	private FileDao fDao;

	@RequestMapping(value = "/process", method = RequestMethod.GET)
	public String file() {
		return "index";

	}

	@RequestMapping(value = "/process", method = RequestMethod.POST)
	public String getFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		byte[] bytes;
		BufferedImage bufferedImg = null;

		if (!file.isEmpty()) {

			bytes = file.getBytes();

			// write in file.
			FileOutputStream out = new FileOutputStream(
					"D:\\java class\\spring\\OCR\\src\\main\\webapp\\resources\\images\\" + file.getOriginalFilename());
			out.write(bytes);

			// to insert the image name in database
			Date d = new Date();
			Image image = new Image();
			image.setImgname(file.getOriginalFilename());
			image.setDate(d);
			fDao.uploadFile(image);
			out.close();

			// To use the OCR functions here create the buffered image and
			// process the ocr processes
			File input = new File(
					"D:\\java class\\spring\\OCR\\src\\main\\webapp\\resources\\images\\" + file.getOriginalFilename());
			bufferedImg = ImageIO.read(input);

			BufferedImage st = Main.identify(bufferedImg);
			File f1 = new File("D:\\java class\\spring\\OCR\\src\\main\\webapp\\resources\\images\\gray.jpg");

			ImageIO.write(st, "jpg", f1);

			model.addAttribute("successMsg", "File upload successfully");
			model.addAttribute("image", file.getOriginalFilename());
		} else {
			model.addAttribute("emptyMsg", "Please select the file");
		}

		return "index";

	}

}
