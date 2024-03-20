package it.myportfolio.dto;

import it.myportfolio.model.ShopableImage;


public class ShopableImageDTO {
	
	private Long ID;
	private String label;
	private boolean isSold;
	private String URL;
	private String thumbnailURL;
	private float price;


	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isSold() {
		return isSold;
	}
	public void setSold(boolean isSold) {
		this.isSold = isSold;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getThumbnailURL() {
		return thumbnailURL;
	}
	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

	public static ShopableImageDTO fromShopableImage(ShopableImage shopableImage) {
		ShopableImageDTO dto = new ShopableImageDTO();
		dto.setID(shopableImage.getId());
		dto.setLabel(shopableImage.getLabel());
		dto.setURL(shopableImage.getURL());
		dto.setThumbnailURL(shopableImage.getThumbnailURL());
		dto.setPrice(shopableImage.getPrice());
		return dto;
	}
	
	
	
	


}
