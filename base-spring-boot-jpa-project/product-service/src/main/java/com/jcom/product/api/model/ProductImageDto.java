package com.jcom.product.api.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class ProductImageDto {

	private List<MultipartFile> images = new ArrayList<>();

	public List<MultipartFile> getImages() {
		return images;
	}

	public void setImages(List<MultipartFile> images) {
		this.images = images;
	}
	
}
