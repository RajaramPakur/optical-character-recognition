package com.ocr.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "imgfeature")
public class Feature {
	
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String feature;
	
	@OneToOne
	@JoinColumn
	private Image image;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public Image getImage_id() {
		return image;
	}

	public void setImage_id(Image image) {
		this.image = image;
	}
	
	
	

}
