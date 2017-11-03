package cn.ffcs.uom.zkdemo.pojo;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class FormResult implements Serializable {
	private String username;
	private String password;
	private Date date;
	private boolean remember;

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
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}
}
