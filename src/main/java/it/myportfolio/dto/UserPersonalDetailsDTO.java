package it.myportfolio.dto;

import java.util.Set;

import it.myportfolio.model.Role;

public class UserPersonalDetailsDTO {

	private Long Id;
	private String surname;
	private String name;
	private String email;
	private Set<Role>  role;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
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

	public Set<Role>  getRole() {
		return role;
	}

	public void setRole(Set<Role> set) {
		this.role = set;
	}
	
	

}