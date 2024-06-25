package it.myportfolio.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="shopable_image")
public class ShopableImage  extends ImageProject{
	
//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name="id")
//	private Long Id;
//	
//	private String label;
//	
	@Column(name="is_sold", columnDefinition = "boolean default false")
	private boolean isSold;
	
//	@Column(nullable = false)
//	private String URL;
//	
//	private String thumbnailURL;
	
	@Column(nullable = false)
	private float price;

	@ManyToMany(mappedBy = "shopableImages")
    private List<Cart> cart;

//	public Long getId() {
//		return Id;
//	}
//
//	public void setId(Long Id) {
//		this.Id = Id;
//	}
//
//	public String getLabel() {
//		return label;
//	}
//
//	public void setLabel(String label) {
//		this.label = label;
//	}
//
//	public String getURL() {
//		return URL;
//	}
//
//	public void setURL(String URL) {
//		this.URL = URL;
//	}
//
//	public String getThumbnailURL() {
//		return thumbnailURL;
//	}
//
//	public void setThumbnailURL(String thumbnailURL) {
//		this.thumbnailURL = thumbnailURL;
//	}

	public boolean isSold() {
		return isSold;
	}

	public void setSold(boolean isSold) {
		this.isSold = isSold;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}
	
}
