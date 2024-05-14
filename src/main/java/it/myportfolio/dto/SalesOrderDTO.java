package it.myportfolio.dto;

import java.util.Date;


public class SalesOrderDTO {
	
	
	private Long Id;
	private Date timestamp;
    private String username;
    private Float price;
    private Long userID;

    
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}

	
	


}

