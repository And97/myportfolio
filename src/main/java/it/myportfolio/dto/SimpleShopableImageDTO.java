package it.myportfolio.dto;

public class SimpleShopableImageDTO {
	
	private Long Id;
	private String label;
	//private String thumbnailURL;
	private float price;


	public Long getID() {
		return Id;
	}
	public void setID(Long Id) {
		this.Id = Id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

//	public String getThumbnailURL() {
//		return thumbnailURL;
//	}
//	public void setThumbnailURL(String thumbnailURL) {
//		this.thumbnailURL = thumbnailURL;
//	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

}
