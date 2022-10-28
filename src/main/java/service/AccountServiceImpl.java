package service;

import java.util.List;

import dao.AccountDao;
import dao.AccountDaoImpl;
import dao.SessionDao;
import dao.SessionDaoImpl;
import exceptions.EmptyListException;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyPresentException;
import exceptions.SystemException;
import model.AccountPojo;
import model.CredentialPojo;
import model.UserPojo;

public class AccountServiceImpl implements AccountService {
	
	AccountDao accountDao;
	
	public AccountServiceImpl() {
		accountDao = new AccountDaoImpl();
	}

	public boolean linkToAccount(UserPojo userPojo, AccountPojo accountPojo) throws SystemException, InvalidInputException, RecordAlreadyPresentException {
		return accountDao.linkToAccount(userPojo, accountPojo);
	}
	
	public List<AccountPojo> getUserAccounts(CredentialPojo credentialPojo) throws EmptyListException, SystemException {
		return accountDao.getUserAccounts(credentialPojo);
	}
	
	public AccountPojo getAnAccount(CredentialPojo credentialPojo, AccountPojo accountPojo) throws EmptyListException, SystemException {
		return accountDao.getAnAccount(credentialPojo, accountPojo);
	}
	
	public double getBalance(int accountNo) throws EmptyListException, SystemException {
		return accountDao.getBalance(accountNo);
	}
}
