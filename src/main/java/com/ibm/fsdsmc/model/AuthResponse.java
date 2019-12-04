/**
 * @author PingXue
 *
 */
package com.ibm.fsdsmc.model;

public class AuthResponse {

//	private String jwtToken;
//	
//	public String getJwtToken() {
//		return jwtToken;
//	}
//	public void setJwtToken(String jwtToken) {
//		this.jwtToken = jwtToken;
//	}
	
	private String usertype;
	private String email;	

	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	  
}
