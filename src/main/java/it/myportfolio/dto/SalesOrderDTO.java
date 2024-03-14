package it.myportfolio.dto;

import java.util.Date;
import java.util.List;


import it.myportfolio.model.SalesOrder;
import it.myportfolio.model.User;

public class SalesOrderDTO {
	
	
	private Long Id;
	private Date timestamp;
	private List<Long> purchasedImage;
    private User user;

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

	public List<Long> getPurchasedImage() {
		return purchasedImage;
	}

	public void setPurchasedImage(List<Long> purchasedImage) {
		this.purchasedImage = purchasedImage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public static SalesOrderDTO fromSalesOrder(SalesOrder salesOrder) {
		SalesOrderDTO dto = new SalesOrderDTO();
		dto.setId(salesOrder.getId());
		dto.setTimestamp(salesOrder.getTimestamp());
		dto.setPurchasedImage(salesOrder.getPurchasedImage());
		return dto;
	}

}

