package service;

import java.util.List;

import exceptions.EmptyListException;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyPresentException;
import exceptions.SystemException;
import model.AccountPojo;
import model.CredentialPojo;
import model.UserPojo;

public interface AccountService {

	public boolean linkToAccount(UserPojo userPojo, AccountPojo accountPojo) throws SystemException, InvalidInputException, RecordAlreadyPresentException;
	public List<AccountPojo> getUserAccounts(CredentialPojo credentialPojo) throws EmptyListException, SystemException;
	public AccountPojo getAnAccount(CredentialPojo credentialPojo, AccountPojo accountPojo) throws EmptyListException, SystemException;
	public double getBalance(int accountNo) throws EmptyListException, SystemException;
}
