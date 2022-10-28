package model;

import java.time.LocalDateTime;

public class SessionPojo {

	private int sessionNumber;
	private String userName;
	private String loginTime;
	private String logoutTime;
	
	public SessionPojo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SessionPojo(int sessionNumber, String userName, String loginTime, String logoutTime) {
		super();
		this.sessionNumber = sessionNumber;
		this.userName = userName;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
	}

	public int getSessionNumber() {
		return sessionNumber;
	}

	public void setSessionNumber(int sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}

	@Override
	public String toString() {
		return "Session [sessionNumber=" + sessionNumber + ", userName=" + userName + ", loginTime=" + loginTime
				+ ", logoutTime=" + logoutTime + "]";
	}
	
	
	
}
