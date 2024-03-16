package it.myportfolio.model;

import java.util.Date;

import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales_order")
public class SalesOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;

	private Date timestamp;

    @Column(name = "purchased_image")
    private List<Long> purchasedImage;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    
	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
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
	
	

}
