package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exceptions.EmptyListException;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyPresentException;
import exceptions.SystemException;
import model.AccountPojo;
import model.CredentialPojo;
import model.UserPojo;

public class AccountDaoImpl implements AccountDao {

	@Override
	public boolean linkToAccount(UserPojo userPojo, AccountPojo accountPojo) throws SystemException, InvalidInputException, RecordAlreadyPresentException {
		// TODO Auto-generated method stub
		boolean success = false;
		Connection conn = null;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM accounts WHERE account_no="+accountPojo.getAccountNumber()+" AND access_code="+accountPojo.getAccessCode();
			ResultSet resultSet = stmt.executeQuery(query);
			if(resultSet.next()) {
				String query7 = "SELECT * FROM accountusers WHERE account_no="+resultSet.getInt(1)+" AND user_id="+userPojo.getUser_id();
				String query2 = "INSERT INTO accountusers(account_no, user_id) VALUES("+resultSet.getInt(1)+", "+userPojo.getUser_id()+")";
				ResultSet resultSet7 = stmt.executeQuery(query7);
				int rowsAffected = 0;
				if(!resultSet7.next())
					rowsAffected = stmt.executeUpdate(query2);
				if(rowsAffected == 1)
					success = true;
				else {
					throw new RecordAlreadyPresentException();
				}
			} else {
				throw new InvalidInputException();
			}
		} catch (SQLException e) {
			throw new SystemException();
		} 
		return success;
	}
	
	public List<AccountPojo> getUserAccounts(CredentialPojo credentialPojo) throws EmptyListException, SystemException {
		List<AccountPojo> allUserAccounts = new ArrayList<AccountPojo>();
		
		Connection conn = null;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT accounts.account_no, accounts.account_type, accounts.account_balance, accounts.pending FROM accounts INNER JOIN accountusers "
					+ "ON accounts.account_no=accountusers.account_no AND user_id="+credentialPojo.getUser_id();
			ResultSet resultSet = stmt.executeQuery(query);
			int counter = 0;
			while(resultSet.next()) {
				counter++;
				AccountPojo accountPojo = new AccountPojo(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), 0, resultSet.getDouble(4));
				allUserAccounts.add(accountPojo);
			}
			if(counter == 0) {
				throw new EmptyListException();
			} 
		} catch (SQLException e) {
			throw new SystemException();
		} 
		
		return allUserAccounts;
	}
	
	public AccountPojo getAnAccount(CredentialPojo credentialPojo, AccountPojo accountPojo) throws EmptyListException, SystemException {
		AccountPojo retAccountPojo;
		Connection conn = null;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT accounts.account_no, accounts.account_type, accounts.account_balance, accounts.pending FROM accounts INNER JOIN accountusers ON "
					+ "accounts.account_no=accountusers.account_no AND user_id = (SELECT user_id FROM credentials WHERE credentials.user_id="+credentialPojo.getUser_id()+"AND "
							+ "credentials.user_pin="+credentialPojo.getUser_pin()+") AND accounts.account_no = "+accountPojo.getAccountNumber()+""; 
			ResultSet resultSet = stmt.executeQuery(query);
			if(resultSet.next()) {
				retAccountPojo = new AccountPojo(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), 0, resultSet.getDouble(4));
			} else {
				throw new EmptyListException();
			} 
		} catch (SQLException e) {
			throw new SystemException();
		} 
		
		return retAccountPojo;
	}
	
	public double getBalance(int accountNo) throws EmptyListException, SystemException {
		Connection conn = null;
		double balance;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT account_balance FROM accounts WHERE account_no="+accountNo; 
			ResultSet resultSet = stmt.executeQuery(query);
			if(resultSet.next()) 
				balance = resultSet.getDouble(1);
			else {
				throw new EmptyListException();
			}
		} catch (SQLException e) {
			throw new SystemException();
		} 
		
		return balance;
	}
	
}
