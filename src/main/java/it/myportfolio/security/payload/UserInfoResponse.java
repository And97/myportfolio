package it.myportfolio.security.payload;

import java.util.List;

public class UserInfoResponse {
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	//private String jwt;

	public UserInfoResponse(Long id, String username, String email, List<String> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	public UserInfoResponse(Long id, String username, String email, List<String> roles, String jwt) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		//this.jwt = jwt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

//	public String getJwt() {
//		return jwt;
//	}
	
	
}
