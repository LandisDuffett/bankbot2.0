package service;

import java.util.List;

import exceptions.BalanceBelowZeroException;
import exceptions.EmptyListException;
import exceptions.SystemException;
import model.AccountPojo;
import model.CredentialPojo;
import model.TransactionPojo;

public interface TransactionService {

	public TransactionPojo makeDeposit(TransactionPojo transactionPojo) throws SystemException;
	
	public TransactionPojo makeWithdrawal(TransactionPojo transactionPojo) throws SystemException, BalanceBelowZeroException;
	
	public List<TransactionPojo> viewTransactionsByUser(int userid) throws EmptyListException, SystemException;
	
	public List<TransactionPojo> viewTransactionsByAccount(int accno, int userid) throws EmptyListException, SystemException;
}
