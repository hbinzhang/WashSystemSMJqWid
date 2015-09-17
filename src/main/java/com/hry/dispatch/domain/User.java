package com.hry.dispatch.domain;

public class User {
	
    private String userId;

    private String userName;

    private String password;

    private String displayName;
    
    private String companyName;
    
    private String companyEs;

	public User() {
		super();
	}

	public User(String userId, String userName, String password, String displayName, String companyName,
			String companyEs) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.displayName = displayName;
		this.companyName = companyName;
		this.companyEs = companyEs;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyEs() {
		return companyEs;
	}

	public void setCompanyEs(String companyEs) {
		this.companyEs = companyEs;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", password=" + password + ", displayName="
				+ displayName + ", companyName=" + companyName + ", companyEs=" + companyEs + "]";
	}

}