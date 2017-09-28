package com.company.dept.model;

public class Account {
	
	public Account() {}
	
	public Account(String line){
		String[] input = line.split("\\s+");
		this.setUserId(input[0]);
		this.setPassword(input[1]);
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	private String userId;
	private String password;
}
