package com.ocr.project.daos;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ocr.project.model.Image;

@Repository
public class FileDaoImpl implements FileDao {

	@Resource
	private SessionFactory SessionFactory;

	@Override
	@Transactional
	public void uploadFile(Image image) {

		Session session = SessionFactory.getCurrentSession();
		session.save(image);

	}

	@Override
	@Transactional
	public Image getById(Long id) {

		Session session = SessionFactory.getCurrentSession();

		Image a = (Image) session.get(Image.class, id);
		return a;
	}

	@Override
	@Transactional
	public void deleteFile(Long id) {
		
		Session session = SessionFactory.getCurrentSession();

		Image a = (Image) session.get(Image.class, id);
		
		session.delete(a);

	}

}
