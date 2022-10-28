package service;

import exceptions.SystemException;
import model.SessionPojo;

public interface SessionService {
	SessionPojo addSession(SessionPojo sessionPojo) throws SystemException;
	void updateSession(SessionPojo sessionPojo) throws SystemException;
}
