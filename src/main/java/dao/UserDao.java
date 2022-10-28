package dao;

import java.util.List;

import exceptions.EmptyFieldException;
import exceptions.NameNotUniqueException;
import exceptions.SystemException;
import model.CredentialPojo;
import model.UserPojo;

public interface UserDao {
	UserPojo addUser(UserPojo userPojo) throws SystemException, EmptyFieldException;
	CredentialPojo addCredential(CredentialPojo credentialPojo) throws SystemException, NameNotUniqueException;
	List<CredentialPojo> getAllCredentials() throws SystemException;
	UserPojo getAUser(int userId) throws SystemException;
	boolean removeOnlineAccount(CredentialPojo credentialPojo) throws SystemException;
	void changePassword(CredentialPojo credentialPojo) throws SystemException;
}
