package it.myportfolio.model;



import java.util.HashSet;
import java.util.Set;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="user")
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long Id;
	
	private String surname;
	private String name;
	private String email;
	private String password;
	
	
	@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopableimage_id")
    private Set<ShopableImage> shopableImage = new HashSet<>();
	
	//@JsonIgnore 
	@ManyToMany(mappedBy = "users")
	private Set<Work> visibleWorks;
	
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<SalesOrder> sales;
    
    @Enumerated(EnumType.STRING)
    private Role roles;
	

    public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Set<ShopableImage> getShopableImage() {
		return shopableImage;
	}

	public void setShopableImage(Set<ShopableImage> image) {
		this.shopableImage = image;
	}
	
	public Set<Work> getVisibleWorks() {
		return visibleWorks;
	}
	public void setVisibleWorks(Set<Work> visibleWorks) {
		this.visibleWorks = visibleWorks;
	}

	public Set<SalesOrder> getSales() {
		return sales;
	}
	public void setSales(Set<SalesOrder> sales) {
		this.sales = sales;
	}
	
	public Role getRoles() {
		return roles;
	}
	public void setRoles(Role roles) {
		this.roles = roles;
	}
	
	
	
	
	
	
}
