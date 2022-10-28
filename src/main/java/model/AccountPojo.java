package model;

public class AccountPojo {

	private int accountNumber;
	private String accountType;
	private double accountBalance;
	private int accessCode;
	private double pending;
	
	

	public AccountPojo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountPojo(int accountNumber, String accountType, double accountBalance, int accessCode, double pending) {
		super();
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.accountBalance = accountBalance;
		this.accessCode = accessCode;
		this.pending = pending;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getPending() {
		return pending;
	}

	public void setPending(double pending) {
		this.pending = pending;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public int getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(int accessCode) {
		this.accessCode = accessCode;
	}

	@Override
	public String toString() {
		return "AccountPojo [accountNumber=" + accountNumber + ", accountType=" + accountType + ", accountBalance="
				+ accountBalance + ", accessCode=" + accessCode + "]";
	}
	
	
}
