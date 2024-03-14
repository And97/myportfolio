package it.myportfolio.dto;


import java.util.Set;

import it.myportfolio.model.SalesOrder;
import it.myportfolio.model.ShopableImage;
import it.myportfolio.model.User;

public class UserPersonalDetailsDTO {

	
	private String surname;
	private String name;
	private String email;
	
	

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



	public static UserPersonalDetailsDTO fromUser(User user) {
		UserPersonalDetailsDTO dto = new UserPersonalDetailsDTO();
		
		dto.setSurname(user.getSurname());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		
		return dto;
	}

}