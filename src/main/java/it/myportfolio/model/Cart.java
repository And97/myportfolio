package it.myportfolio.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @OneToOne
	    private User user;

	    @ManyToMany
	    @JoinTable(
	            name = "cart_shopable_image",
	            joinColumns = @JoinColumn(name = "cart_id"),
	            inverseJoinColumns = @JoinColumn(name = "shopable_image_id")
	    )
	    private List<ShopableImage> shopableImages;
	    

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public List<ShopableImage> getImages() {
			return shopableImages;
		}

		public void setImages(List<ShopableImage> shopableImages) {
			this.shopableImages = shopableImages;
		}

}
