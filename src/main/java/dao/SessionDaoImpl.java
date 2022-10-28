package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exceptions.SystemException;
import model.SessionPojo;

public class SessionDaoImpl implements SessionDao {

	public SessionPojo addSession(SessionPojo sessionPojo) throws SystemException {
		Connection conn;

		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO sessions(user_name, login_time, logout_time) VALUES('"
					+sessionPojo.getUserName() + "', '"+ sessionPojo.getLoginTime()+"', '"+sessionPojo.getLogoutTime()+"') returning session_id";
			ResultSet resultSet = stmt.executeQuery(query);
			resultSet.next();
			sessionPojo.setSessionNumber(resultSet.getInt(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new SystemException();
		}

		return sessionPojo;
	}
	
	public void updateSession(SessionPojo sessionPojo) throws SystemException {
		Connection conn;

		try {
			conn = DBUtil.makeConnection();
			Statement stmt = conn.createStatement();
			String query = "UPDATE sessions SET logout_time='"+sessionPojo.getLogoutTime()+"' WHERE session_id="+sessionPojo.getSessionNumber();
			int rowsAffected = stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new SystemException();
		}
	}
}
