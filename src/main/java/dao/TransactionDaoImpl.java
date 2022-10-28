package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exceptions.BalanceBelowZeroException;
import exceptions.EmptyListException;
import exceptions.SystemException;
import model.AccountPojo;
import model.CredentialPojo;
import model.TransactionPojo;

public class TransactionDaoImpl implements TransactionDao {

	public TransactionPojo makeDeposit(TransactionPojo transactionPojo) throws SystemException {
		Connection conn = null;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO transactions(account_number, time, transaction_type, transaction_amount, updated_balance, user_id) VALUES("
					+transactionPojo.getAccountNumber()+", '"+transactionPojo.getTime()+"', '"+transactionPojo.getTransactionType()+"', "+
					transactionPojo.getTransactionAmount()+", "+transactionPojo.getUpdatedBalance() + ", "+transactionPojo.getUserId()+") returning transaction_number";
			String query2 = "UPDATE accounts SET pending= (pending +"+transactionPojo.getTransactionAmount()+") WHERE account_no="
					+transactionPojo.getAccountNumber() + "";
			String query3 = "UPDATE accounts SET account_balance=(account_balance +"+transactionPojo.getTransactionAmount()+") WHERE account_no="
					+transactionPojo.getAccountNumber() + "";
			conn.setAutoCommit(false);
			ResultSet resultSet = stmt.executeQuery(query);
			resultSet.next();
			transactionPojo.setTransactionNumber(resultSet.getInt(1));
			int rowsAffected;
			if(transactionPojo.getTransactionType().matches("transfer.*")) {
				rowsAffected = stmt.executeUpdate(query3);
			}
			else {
				rowsAffected = stmt.executeUpdate(query2);
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				if(conn!=null)
					conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println("rollback failed");
			}
			throw new SystemException();
			
		}
		
		return transactionPojo;
	}
	
	public TransactionPojo makeWithdrawal(TransactionPojo transactionPojo) throws SystemException, BalanceBelowZeroException {
		Connection conn = null;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO transactions(account_number, time, transaction_type, transaction_amount, updated_balance, user_id) VALUES("
					+transactionPojo.getAccountNumber()+", '"+transactionPojo.getTime()+"', '"+transactionPojo.getTransactionType()+"', "
					+"(0-ABS("
					+ transactionPojo.getTransactionAmount()
					+ ")), "+transactionPojo.getUpdatedBalance() + ", "+transactionPojo.getUserId()+") returning transaction_number";
			String query2 = "UPDATE accounts SET account_balance=(account_balance -"+transactionPojo.getTransactionAmount()+") WHERE account_no="
					+transactionPojo.getAccountNumber() + "";
			String query3 = "SELECT account_balance, account_type from accounts WHERE account_no="+transactionPojo.getAccountNumber();
			String query4 = "UPDATE accounts SET account_balance=(account_balance - 25) WHERE account_no="+transactionPojo.getAccountNumber();
			ResultSet resultSet2 = stmt.executeQuery(query3);
			resultSet2.next();
			double newBalance = resultSet2.getDouble(1) - transactionPojo.getTransactionAmount();
			if(newBalance > 0 && ((resultSet2.getString(2).equals("savings") && newBalance < 200 && resultSet2.getDouble(1) > 200) || (resultSet2.getString(2).equals("checking") && newBalance < 100 && resultSet2.getDouble(1) > 100))) {
				conn.setAutoCommit(false);
				double amt = transactionPojo.getUpdatedBalance() - 25.0;
				query = "INSERT INTO transactions(account_number, time, transaction_type, transaction_amount, updated_balance) VALUES("
						+transactionPojo.getAccountNumber()+", '"+transactionPojo.getTime()+"', '"+transactionPojo.getTransactionType()+"', "
						+"(0-ABS("
						+ transactionPojo.getTransactionAmount()
						+ ")), "+ amt + ", "+transactionPojo.getUserId()+") returning transaction_number";
				ResultSet resultSet = stmt.executeQuery(query);
				resultSet.next();
				transactionPojo.setTransactionNumber(resultSet.getInt(1));
				transactionPojo.setUpdatedBalance(amt);
				int rowsAffected3 = stmt.executeUpdate(query2);	
				int rowsAffected4 = stmt.executeUpdate(query4);
				conn.commit();
			} else if (newBalance > 0) {
				conn.setAutoCommit(false);
				ResultSet resultSet = stmt.executeQuery(query);
				resultSet.next();
				transactionPojo.setTransactionNumber(resultSet.getInt(1));
				int rowsAffected3 = stmt.executeUpdate(query2);	
				conn.commit();
			}
			else {
				throw new BalanceBelowZeroException();
			}
		} catch (SQLException e) {
			throw new SystemException();
		}
		
		return transactionPojo;
	}
	
	public List<TransactionPojo> viewTransactionsByUser(int userid) throws EmptyListException, SystemException {
		List<TransactionPojo> transByUser = new ArrayList<TransactionPojo>();
		
		Connection conn = null;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT transaction_number, account_number, time, transaction_type, transaction_amount, "
					+ "updated_balance, user_id FROM transactions WHERE user_id="+userid;
			System.out.println("hello");
			ResultSet resultSet = stmt.executeQuery(query);
			System.out.println("uid: "+userid);
			int counter = 0;
			while(resultSet.next()) {
				counter++;
				TransactionPojo transPojo = new TransactionPojo(resultSet.getInt(1), resultSet.getInt(2), 
						resultSet.getString(3), resultSet.getString(4), 
						resultSet.getDouble(5), resultSet.getDouble(6), resultSet.getInt(7));
				transByUser.add(transPojo);
			}
			if(counter == 0) {
				throw new EmptyListException();
			} 
		} catch (SQLException e) {
			throw new SystemException();
		} 
		
		return transByUser;
	}
	
	public List<TransactionPojo> viewTransactionsByAccount(int accno, int userid) throws EmptyListException, SystemException {
		List<TransactionPojo> transByUser = new ArrayList<TransactionPojo>();
		
		Connection conn = null;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT transactions.transaction_number, transactions.account_number, "
					+ "transactions.time, transactions.transaction_type, transactions.transaction_amount, "
					+ "transactions.updated_balance, transactions.user_id FROM transactions WHERE account_number="
					+ "(SELECT account_no FROM accountusers where account_no="+accno+" and user_id="+userid+")";
			ResultSet resultSet = stmt.executeQuery(query);
			int counter = 0;
			while(resultSet.next()) {
				counter++;
				TransactionPojo transPojo = new TransactionPojo(resultSet.getInt(1), resultSet.getInt(2), 
						resultSet.getString(3), resultSet.getString(4), 
						resultSet.getDouble(5), resultSet.getDouble(6), resultSet.getInt(7));
				transByUser.add(transPojo);
			}
			if(counter == 0) {
				throw new EmptyListException();
			} 
		} catch (SQLException e) {
			throw new SystemException();
		} 
		
		return transByUser;
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
}
	
	
