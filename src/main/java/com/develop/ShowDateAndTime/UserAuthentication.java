package com.develop.ShowDateAndTime;

import java.sql.SQLException;

public class UserAuthentication {
	//	private String username;
	//	private String password;
	//	
	//	public String getUsername() {
	//		return username;
	//	}
	//	public void setUsername(String username) {
	//		this.username = username;
	//	}
	//	public String getPassword() {
	//		return password;
	//	}
	//	public void setPassword(String password) {
	//		this.password = password;
	//	}
	public static boolean authenticate(String uname,String pwd) throws SQLException, ClassNotFoundException {
		return SqlDAL.authenticateUser(uname, pwd);
	}

}
