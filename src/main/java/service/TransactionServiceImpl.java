package service;

import java.util.List;

import dao.TransactionDao;
import dao.TransactionDaoImpl;
import exceptions.BalanceBelowZeroException;
import exceptions.EmptyListException;
import exceptions.SystemException;
import model.AccountPojo;
import model.CredentialPojo;
import model.TransactionPojo;

public class TransactionServiceImpl implements TransactionService {

	TransactionDao transactionDao;
	
	public TransactionServiceImpl() {
		transactionDao = new TransactionDaoImpl();
	}
	
	public TransactionPojo makeDeposit(TransactionPojo transactionPojo) throws SystemException {
		return transactionDao.makeDeposit(transactionPojo);
	}
	
	public TransactionPojo makeWithdrawal(TransactionPojo transactionPojo) throws SystemException, BalanceBelowZeroException {
		return transactionDao.makeWithdrawal(transactionPojo);
	}
	
	public List<TransactionPojo> viewTransactionsByUser(int userid) throws EmptyListException, SystemException {
		return transactionDao.viewTransactionsByUser(userid);
	}
	
	public List<TransactionPojo> viewTransactionsByAccount(int accno, int userid) throws EmptyListException, SystemException {
		return transactionDao.viewTransactionsByAccount(accno, userid);
	}
	
}
