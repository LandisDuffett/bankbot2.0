package service;

import java.util.List;

import exceptions.EmptyFieldException;
import exceptions.ExceedLimitException;
import exceptions.NameNotUniqueException;
import exceptions.SystemException;
import model.CredentialPojo;
import model.UserPojo;

public interface UserService {
	UserPojo addUser(UserPojo userPojo) throws SystemException, EmptyFieldException, ExceedLimitException;
	CredentialPojo addCredential(CredentialPojo credentialPojo) throws SystemException, NameNotUniqueException;
	List<CredentialPojo> getAllCredentials() throws SystemException;
	UserPojo getAUser(int userId) throws SystemException;
	boolean removeOnlineAccount(CredentialPojo credentialPojo) throws SystemException;
	void changePassword(CredentialPojo credentialPojo) throws SystemException;
}
