package it.myportfolio.dto;

public class SimpleShopableImageDTO {
	
	private Long Id;
	private String label;
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
	
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

}
