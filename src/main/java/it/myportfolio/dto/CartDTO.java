package it.myportfolio.dto;

import java.util.List;


public class CartDTO {

	private Long id;

	private Long userId;

	private List<ShopableImageDTO> shopableImages;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<ShopableImageDTO> getImages() {
		return shopableImages;
	}

	public void setImages(List<ShopableImageDTO> shopableImages) {
		this.shopableImages = shopableImages;
	}
}
