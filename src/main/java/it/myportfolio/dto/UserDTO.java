package it.myportfolio.dto;

import java.util.Set;

import it.myportfolio.model.Role;
import it.myportfolio.model.SalesOrder;
import it.myportfolio.model.ShopableImage;
import it.myportfolio.model.User;

public class UserDTO {

	private Long id;
	private String surname;
	private String name;
	private String email;
	private Set<ShopableImage> shopableImage;
	private Set<SalesOrder> sales;
	private Role role;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<ShopableImage> getShopableImage() {
		return shopableImage;
	}

	public void setShopableImage(Set<ShopableImage> image) {
		this.shopableImage = image;
	}
	
	public Set<SalesOrder> getSales() {
		return sales;
	}
	public void setSales(Set<SalesOrder> sales) {
		this.sales = sales;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public static UserDTO fromUser(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setSurname(user.getSurname());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setShopableImage(user.getShopableImage());
		dto.setSales(dto.getSales());
		dto.setRole(user.getRoles());

		return dto;
	}

}