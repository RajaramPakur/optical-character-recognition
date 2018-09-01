package com.ocr.project.daos;

import com.ocr.project.model.Image;

public interface FileDao {
	
	public void uploadFile(Image image);
	
	public Image getById(Long id);
	
	public void deleteFile(Long id);

}
