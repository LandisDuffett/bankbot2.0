package dao;

import exceptions.SystemException;
import model.SessionPojo;

public interface SessionDao {
	public SessionPojo addSession(SessionPojo sessionPojo) throws SystemException;
	public void updateSession(SessionPojo sessionPojo) throws SystemException;
}
