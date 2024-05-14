package it.myportfolio.dto;

import java.util.Date;
import java.util.List;


public class DetailsSalesOrderDTO {
	
	
	private Long Id;
	private Date timestamp;
    private List<ShopableImageDTO> purchaseImage;
    private Float totalPrice;
    private String username;
    private int piece;
    
    
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public List<ShopableImageDTO> getPurchaseImage() {
		return purchaseImage;
	}
	public void setPurchaseImage(List<ShopableImageDTO> purchaseImage) {
		this.purchaseImage = purchaseImage;
	}
	public Float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Float price) {
		this.totalPrice = price;
	}
		
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getPiece() {
		return piece;
	}
	public void setPiece(int piece) {
		this.piece = piece;
	} 
	
	

}

