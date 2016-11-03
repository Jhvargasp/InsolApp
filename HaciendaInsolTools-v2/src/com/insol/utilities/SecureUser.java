package com.insol.utilities;

import com.data.toolkit.utility.Encrypt;

public class SecureUser {

	private String username;
	private String password;
	

	public SecureUser(String encodedUser) {
		
		String decodedUser  = Encrypt.decode(encodedUser);
		
		if(decodedUser.contains(":")){
			
			String[] uInfo = decodedUser.split(":");
			
			this.username = uInfo[0];
			this.password = uInfo[1];
			
		} else {
			System.err.println("No user information in FileNetUset Constructor");
		}
	}
	
	public SecureUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSecuredUser() {
		return Encrypt.encode(this.username + ":" + this.password);
	}
}
