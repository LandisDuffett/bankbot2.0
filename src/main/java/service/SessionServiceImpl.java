package service;

import dao.SessionDao;
import dao.SessionDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
import exceptions.SystemException;
import model.SessionPojo;

public class SessionServiceImpl implements SessionService {
	
	SessionDao sessionDao;
	
	public SessionServiceImpl() {
		sessionDao = new SessionDaoImpl();
	}
	
	public SessionPojo addSession(SessionPojo sessionPojo) throws SystemException {
		return sessionDao.addSession(sessionPojo);
	}
	
	public void updateSession(SessionPojo sessionPojo) throws SystemException {
		sessionDao.updateSession(sessionPojo);
	}
}
