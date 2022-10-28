package model;

import java.util.InputMismatchException;

import exceptions.MismatchException;

public class CredentialPojo {

	private String user_name;
	private String user_password;
	private int user_pin;
	private String user_maiden;
	private int user_id;
	
	public CredentialPojo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CredentialPojo(String user_name, String user_password, int user_pin, String user_maiden, int user_id) {
		super();
		this.user_name = user_name;
		this.user_password = user_password;
		this.user_pin = user_pin;
		this.user_maiden = user_maiden;
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public int getUser_pin() {
		return user_pin;
	}

	public void setUser_pin(int user_pin) throws InputMismatchException {
		this.user_pin = user_pin;
	}

	public String getUser_maiden(){
		return user_maiden;
	}

	public void setUser_maiden(String user_maiden) {
		this.user_maiden = user_maiden;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "Credential [user_name=" + user_name + ", user_password=" + user_password + ", user_pin=" + user_pin
				+ ", user_maiden=" + user_maiden + ", user_id=" + user_id + "]";
	}
	
	
}
