package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exceptions.EmptyFieldException;
import exceptions.NameNotUniqueException;
import exceptions.SystemException;
import model.CredentialPojo;
import model.UserPojo;

public class UserDaoImpl implements UserDao {

	public UserPojo addUser(UserPojo userPojo) throws SystemException, EmptyFieldException {
		Connection conn;

			try {
				conn = DBUtil.makeConnection();
				Statement stmt = conn.createStatement();
				String query = "INSERT INTO users(first_name, last_name) VALUES('"					
						+ userPojo.getFirst_name() + "', '" + userPojo.getLast_name() + "') returning user_id";
				ResultSet resultSet = stmt.executeQuery(query);
				resultSet.next();
				userPojo.setUser_id(resultSet.getInt(1));
			} catch (SQLException e) {
				throw new SystemException();
			}
			
		 
		return userPojo;
	}
	
	public CredentialPojo addCredential(CredentialPojo credentialPojo) throws SystemException, NameNotUniqueException {
		Connection conn;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO credentials(user_name, user_password, user_pin, user_maiden, user_id) "
					+ "VALUES('" + credentialPojo.getUser_name() + "', '"+credentialPojo.getUser_password() + "', "+
					credentialPojo.getUser_pin()+", '"+credentialPojo.getUser_maiden()+"', "+credentialPojo.getUser_id()+")";
			int rowsAffected = stmt.executeUpdate(query);
		} catch (SQLException e) {
			throw new SystemException();
		}
		
		return credentialPojo;
	}
	
	public List<CredentialPojo> getAllCredentials() throws SystemException {
		List<CredentialPojo> allCredentials = new ArrayList<CredentialPojo>();
		
		Connection conn = null;
		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM credentials";
			ResultSet resultSet = stmt.executeQuery(query);
			int counter = 0;
			while(resultSet.next()) {
				counter++;
				CredentialPojo credentialPojo = new CredentialPojo(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getInt(5));
				allCredentials.add(credentialPojo);
			}
		} catch (SQLException e) {
			throw new SystemException();
		} 
		return allCredentials;
	}

  public UserPojo getAUser(int userId) throws SystemException {
	  Connection conn = null;
	  UserPojo userPojo = null;
	  try {
		  conn = DBUtil.makeConnection();
		  Statement stmt = conn.createStatement();
		  String query = "SELECT * FROM users WHERE user_id="+userId;
		  ResultSet resultSet = stmt.executeQuery(query);
		  if(resultSet.next()) {
			  userPojo = new UserPojo(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
		  }
	  } catch (SQLException e) {
		  throw new SystemException();
	  }
	  return userPojo;
  }
	
  public boolean removeOnlineAccount(CredentialPojo credentialPojo) throws SystemException {
	  boolean message = false;
	  UserPojo userPojo = getAUser(credentialPojo.getUser_id());
	  Connection conn = null;
	  try {
		  conn = DBUtil.makeConnection();
		  Statement stmt = conn.createStatement();
		  
		  String query1 = "INSERT INTO inactive_users(first_name, last_name, user_name, user_password, user_pin, user_maiden) "
		  		+ "VALUES('"+userPojo.getFirst_name()+"', '"+userPojo.getLast_name()+"', '"+credentialPojo.getUser_name()+"','"+credentialPojo.getUser_password()+"',"+credentialPojo.getUser_pin()+",'"+credentialPojo.getUser_maiden()+"')";
		 
		  String query2 = "DELETE FROM users WHERE user_id=" + credentialPojo.getUser_id();
		  conn.setAutoCommit(false);
		  int rowsAffected1 = stmt.executeUpdate(query1);
		  int rowsAffected2 = stmt.executeUpdate(query2);
		  conn.commit();
		  if(rowsAffected1 == 1 && rowsAffected2 == 1) {
			  message = true;
		  } 
	  } catch (SQLException e) {
		  throw new SystemException();
	  }
	 
	  return message;
  }
  
  public void changePassword(CredentialPojo credentialPojo) throws SystemException {
	  Connection conn = null;
	  try {
		  conn = DBUtil.makeConnection();
		  Statement stmt = conn.createStatement();
		  String query = "UPDATE credentials SET user_password='"+credentialPojo.getUser_password()+"' WHERE user_id="+
		  credentialPojo.getUser_id();
		  int rowsAffected = stmt.executeUpdate(query);
	  } catch (SQLException e) {
		  throw new SystemException();
	  }
  }

}
