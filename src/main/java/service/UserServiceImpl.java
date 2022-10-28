package service;

import java.util.List;

import dao.UserDao;
import dao.UserDaoImpl;
import exceptions.EmptyFieldException;
import exceptions.ExceedLimitException;
import exceptions.NameNotUniqueException;
import exceptions.SystemException;
import model.CredentialPojo;
import model.UserPojo;

public class UserServiceImpl implements UserService{

	UserDao userDao;
	
	public UserServiceImpl() {
		userDao = new UserDaoImpl();
	}

	public UserPojo addUser(UserPojo userPojo) throws SystemException, EmptyFieldException, ExceedLimitException {
		if(userPojo.getFirst_name() == "" || userPojo.getLast_name() == "") {
			throw new EmptyFieldException();
		} 
		else if (userPojo.getFirst_name().length() > 20 || userPojo.getLast_name().length()
				> 20) {
			throw new ExceedLimitException();
		}
		else {
			return userDao.addUser(userPojo);
		}
	}
	
	public CredentialPojo addCredential(CredentialPojo credentialPojo) throws SystemException, NameNotUniqueException {
		List<CredentialPojo> credList = userDao.getAllCredentials();
		for(CredentialPojo item: credList) {
			if(item.getUser_name().equals(credentialPojo.getUser_name())) {
				throw new NameNotUniqueException();
			}
		}
		return userDao.addCredential(credentialPojo);
	}
	
	public UserPojo getAUser(int userId) throws SystemException {
		return userDao.getAUser(userId);
	}
	
	public List<CredentialPojo> getAllCredentials() throws SystemException {
		return userDao.getAllCredentials();
	}
	
	public boolean removeOnlineAccount(CredentialPojo credentialPojo) throws SystemException {
		return userDao.removeOnlineAccount(credentialPojo);
	}
	
	public void changePassword(CredentialPojo credentialPojo) throws SystemException {
		userDao.changePassword(credentialPojo);
	}
	
}
