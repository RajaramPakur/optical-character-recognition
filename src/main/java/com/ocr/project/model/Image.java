package com.ocr.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "image")
public class Image {
	
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String imgname;
	@Column
	private Date date;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImgname() {
		return imgname;
	}
	public void setImgname(String imgname) {
		this.imgname = imgname;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date d) {
		this.date = d;
	}
	
	

}
